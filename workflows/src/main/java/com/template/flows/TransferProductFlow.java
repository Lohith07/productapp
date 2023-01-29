package com.template.flows;

import java.util.Arrays;
import java.util.List;

import com.template.contracts.ProductContract;
import com.template.states.ProductState;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import static net.corda.core.contracts.ContractsDSL.requireThat;

public class TransferProductFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<SignedTransaction> {

        private final int ProductID;
        private final Party newowner;

        public Initiator(int ProductID, Party newowner) {
            this.ProductID = ProductID;
            this.newowner = newowner;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {

            final Party notary = getServiceHub().getNetworkMapCache()
                    .getNotary(CordaX500Name.parse("O=Notary,L=London,C=GB"));

            Party me = getOurIdentity();

            List<StateAndRef<ProductState>> stateStateAndRef = getServiceHub().getVaultService()
                    .queryBy(ProductState.class).getStates();

            StateAndRef<ProductState> inputStateAndRef = stateStateAndRef.stream().filter(productStateAndRef -> {
                ProductState productState = productStateAndRef.getState().getData();
                return productState.getProductID().equals(ProductID);
            }).findAny().orElseThrow(() -> new IllegalArgumentException("Product Not Found"));

            ProductState inputState = inputStateAndRef.getState().getData();
            // FlowSession ownerSession = initiateFlow(me);
            FlowSession newOwnerSession = initiateFlow(newowner);

            final ProductState output = new ProductState(inputState.getProductID(), inputState.getName(),
                    inputState.getDescription(), inputState.getQuantity(),
                    inputState.getLinearId(),
                    newowner);

            final Command<ProductContract.Commands.Transfer> txCommand = new Command<>(
                    new ProductContract.Commands.Transfer(),
                    Arrays.asList(me.getOwningKey(),newowner.getOwningKey()));

            final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                    .addInputState(inputStateAndRef).addOutputState(output, ProductContract.ID)
                    .addCommand(txCommand);

            txBuilder.verify(getServiceHub());

            final SignedTransaction partSignedTx = getServiceHub().signInitialTransaction(txBuilder);


            final SignedTransaction fullySignedTx = subFlow(
                    new CollectSignaturesFlow(partSignedTx, Arrays.asList(newOwnerSession)));

            return subFlow(new FinalityFlow(fullySignedTx, Arrays.asList(newOwnerSession)));
        }
    }

    @InitiatedBy(Initiator.class)
    public static class Acceptor extends FlowLogic<SignedTransaction> {

        private final FlowSession otherPartySession;

        public Acceptor(FlowSession otherPartySession) {
            this.otherPartySession = otherPartySession;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            class SignTxFlow extends SignTransactionFlow {
                private SignTxFlow(FlowSession otherPartyFlow) {
                    super(otherPartyFlow);
                }

                @Override
                protected void checkTransaction(SignedTransaction stx) {
                    requireThat(require -> {
//                        ProductState output = stx.getTx().getOutputs().get(0).getData();
                        ProductState output = (ProductState) stx.getTx().getOutputs().get(0).getData();
                        require.using("This must be an IOU transaction.", output instanceof ProductState);
                        ProductState iou = (ProductState) output;
                        return null;
                    });
                }
            }
            final SignTxFlow signTxFlow = new SignTxFlow(otherPartySession);
            final SecureHash txId = subFlow(signTxFlow).getId();

            return subFlow(new ReceiveFinalityFlow(otherPartySession,txId));
        }
    }
}
