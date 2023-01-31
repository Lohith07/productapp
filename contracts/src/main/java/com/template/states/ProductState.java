package com.template.states;

import com.template.contracts.ProductContract;
//import com.template.schemas.ProductSchema2;

//import com.template.schemas.ProductSchema2;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(ProductContract.class)
public class ProductState implements LinearState {

    // private variables
    private Integer ProductID;
    private String Name;
    private String Description;
    private Integer Quantity;
    private Party owner;
    private UniqueIdentifier linearId;

    /* Constructor of your Corda state */
    public ProductState(Integer ProductID, String Name, String Description, Integer Quantity,
            UniqueIdentifier linearId, Party owner) {
        this.ProductID = ProductID;
        this.Name = Name;
        this.Description = Description;
        this.Quantity = Quantity;
        this.linearId = linearId;
        this.owner = owner;

    }

    // getters
    public Integer getProductID() {
        return ProductID;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public Party getowner() {
        return owner;
    }

    /*
     * This method will indicate who are the participants and required signers when
     * this state is used in a transaction.
     */
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(owner);
    }

    @Override
    public UniqueIdentifier getLinearId() {
        return linearId;
    }

//     @Override
//     public PersistentState generateMappedObject(MappedSchema schema) {
//         if (schema instanceof ProductSchema2) {
//             return new ProductSchema2.PersistentProduct(
//                     this.ProductID,
//                     this.Name,
//                     this.Description,
//                     this.Quantity,
//                     this.owner.getName().toString(),
//                     this.linearId.getId());
//         } else {
//             throw new IllegalArgumentException("Unrecognised schema $schema");
//         }
//     }
//
//     @Override
//     public Iterable<MappedSchema> supportedSchemas() {
//         // TODO Auto-generated method stub
//         return Arrays.asList(new ProductSchema2());
//     }
}