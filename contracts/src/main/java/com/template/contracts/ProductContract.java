package com.template.contracts;

import com.template.states.ProductState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class ProductContract implements Contract {

    public static final String ID = "com.template.contracts.ProductContract";

    @Override
    public void verify(LedgerTransaction tx) {

        final CommandData commandData = tx.getCommands().get(0).getValue();
        if (commandData instanceof Commands.Create) {
            // Retrieve the output state of the transaction
            ProductState output = tx.outputsOfType(ProductState.class).get(0);
            CommandWithParties<ProductContract.Commands> command = requireSingleCommand(tx.getCommands(),
                    ProductContract.Commands.class);

            String own = output.getowner().toString();
            

            requireThat(require -> {

                require.using("owner must be required signer.",
                        command.getSigners().contains(output.getowner().getOwningKey()));

                require.using("owner must be Supplier.",
                        (own.equals("O=Supplier, L=London, C=GB")));

                require.using("Only one output states should be created.",
                        tx.getOutputs().size() == 1);

                require.using("The Quantity value must be non-negative.",
                        output.getQuantity() > 0);
                return null;
            });
        }

        else if (commandData instanceof Commands.Update) {
            // Retrieve the output state of the transaction

            ProductState output = tx.outputsOfType(ProductState.class).get(0);
            CommandWithParties<ProductContract.Commands> command = requireSingleCommand(tx.getCommands(),
                    ProductContract.Commands.class);

            String own = output.getowner().toString();

            requireThat(require -> {

                require.using("owner must be required signer.",
                        command.getSigners().contains(output.getowner().getOwningKey()));

                require.using("owner must be Supplier.",
                        (own.equals("O=Supplier, L=London, C=GB")));

                require.using("The Quantity value must be non-negative.",
                        output.getQuantity() > 0);
                return null;
            });
        }

        else if (commandData instanceof Commands.Transfer) {
            // Retrieve the output state of the transaction
            
            ProductState output = tx.outputsOfType(ProductState.class).get(0);
            String own = output.getowner().toString();
            CommandWithParties<ProductContract.Commands> command = requireSingleCommand(tx.getCommands(),
                    ProductContract.Commands.class);

            requireThat(require -> {

                require.using("The Quantity value must be non-negative.",
                        output.getQuantity() > 0);
                return null;
            });
        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {

        class Create implements Commands {
        }

        class Update implements Commands {
        }

        class Transfer implements Commands {
        }
    }
}