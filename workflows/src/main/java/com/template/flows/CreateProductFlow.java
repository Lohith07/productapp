package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;

import com.template.contracts.ProductContract;
import com.template.states.ProductState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import java.util.Arrays;

@InitiatingFlow
@StartableByRPC
public class CreateProductFlow extends FlowLogic<SignedTransaction> {

        // private variables
        private Integer ProductID;
        private String Name;
        private String Description;
        private Integer Quantity;

        public CreateProductFlow(Integer ProductID, String Name, String Description, Integer Quantity) {

                this.ProductID = ProductID;
                this.Name = Name;
                this.Description = Description;
                this.Quantity = Quantity;

        }

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {

                Party me = getOurIdentity();
                
                final Party notary = getServiceHub().getNetworkMapCache()
                                .getNotary(CordaX500Name.parse("O=Notary,L=London,C=GB"));

                final ProductState output = new ProductState(ProductID, Name, Description, Quantity,
                                new UniqueIdentifier(),
                                me);

                final Command<ProductContract.Commands.Create> txCommand = new Command<>(
                                new ProductContract.Commands.Create(),
                                Arrays.asList(me.getOwningKey()));

                final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                                .addOutputState(output, ProductContract.ID)
                                .addCommand(txCommand);

                txBuilder.verify(getServiceHub());

                final SignedTransaction partSignedTx = getServiceHub().signInitialTransaction(txBuilder);

                return subFlow(new FinalityFlow(partSignedTx, Arrays.asList()));
        }
}