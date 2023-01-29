package com.template.schemas;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.UUID;
//4.6 changes
import org.hibernate.annotations.Type;

import javax.annotation.Nullable;

public class ProductSchema2 extends MappedSchema {
    public ProductSchema2() {
        super(ProductSchema.class, 1, Arrays.asList(PersistentProduct.class));
    }

    @Nullable
    @Override
    public String getMigrationResource() {
        return "product.changelog-master.xml";
    }

    @Entity
    @Table(name = "PRODUCT")
    public static class PersistentProduct extends PersistentState {
        @Column(name = "PRODUCT_ID")
        private final Integer productid;
        @Column(name = "NAME")
        private final String name;
        @Column(name = "DESCRIPTION")
        private final String description;
        @Column(name = "QUANTITY")
        private final int quantity;
        @Column(name = "OWNER")
        private final String owner;
        @Column(name = "LINEAR_ID")
        @Type(type = "uuid-char")
        private final UUID linearId;

        public PersistentProduct(Integer productid, String name, String description, int quantity, String owner,
                UUID linearId) {
            this.productid = productid;
            this.name = name;
            this.description = description;
            this.quantity = quantity;
            this.owner = owner;
            this.linearId = linearId;

        }

        // Default constructor required by hibernate.
        public PersistentProduct(Integer productID2, String name2, String description2, Integer quantity2, String string) {
            this.productid = null;
            this.name = null;
            this.description = null;
            this.quantity = 0;
            this.owner = null;
            this.linearId = null;
        }

        public Integer getproductid() {
            return productid;
        }

        public String getname() {
            return name;
        }

        public String getdescription() {
            return description;
        }

        public Integer getquantity() {
            return quantity;
        }

        public String getowneR() {
            return owner;
        }

        public UUID getId() {
            return linearId;
        }
    }
}
