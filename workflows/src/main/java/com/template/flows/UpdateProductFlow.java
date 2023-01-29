package com.template.flows;

import java.util.Arrays;
import java.util.List;

import com.template.contracts.ProductContract;
import com.template.states.ProductState;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

@InitiatingFlow
@StartableByRPC
public class UpdateProductFlow extends FlowLogic<SignedTransaction> {

        private Integer ProductID;
        private Integer Quantity;

        public UpdateProductFlow(Integer ProductID, Integer Quantity) {
                this.ProductID = ProductID;
                this.Quantity = Quantity;

        }

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {
                Party me = getOurIdentity();

                List<StateAndRef<ProductState>> stateStateAndRef = getServiceHub().getVaultService()
                                .queryBy(ProductState.class).getStates();

                StateAndRef<ProductState> inputStateAndRef = stateStateAndRef.stream().filter(productStateAndRef -> {
                        ProductState productState = productStateAndRef.getState().getData();
                        return productState.getProductID().equals(ProductID);
                }).findAny().orElseThrow(() -> new IllegalArgumentException("Product Not Found"));

                ProductState inputState = inputStateAndRef.getState().getData();

                final Party notary = getServiceHub().getNetworkMapCache()
                                .getNotary(CordaX500Name.parse("O=Notary,L=London,C=GB"));

                final ProductState output = new ProductState(inputState.getProductID(), inputState.getName(),
                                inputState.getDescription(), Quantity,
                                inputState.getLinearId(),
                                me);

                final Command<ProductContract.Commands.Update> txCommand = new Command<>(
                                new ProductContract.Commands.Update(),
                                Arrays.asList(me.getOwningKey()));

                final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                                .addInputState(inputStateAndRef).addOutputState(output, ProductContract.ID)
                                .addCommand(txCommand);

                txBuilder.verify(getServiceHub());

                final SignedTransaction partSignedTx = getServiceHub().signInitialTransaction(txBuilder);
                
                return subFlow(new FinalityFlow(partSignedTx, Arrays.asList()));

        }

}
