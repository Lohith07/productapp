--liquibase formatted sql

--changeset your.name:1 labels:example-label context:example-context
--comment: example comment
create table person (
    id int primary key auto_increment not null,
    name varchar(50) not null,
    address1 varchar(50),
    address2 varchar(50),
    city varchar(30)
)
--rollback DROP TABLE person;

--changeset R3.Corda:2 labels:product-label context:product-context
--comment: product comment
--create table product (
--    product_id int,
--    name varchar(50),
--    description varchar(50),
--    quantity int,
--    owner varchar(50),
--    linear_id varchar(30)
--)
--rollback DROP TABLE product;

--changeset other.dev:3 labels:example-label context:example-context
--comment: example comment
alter table person add column country varchar(2)
--rollback ALTER TABLE person DROP COLUMN country;

