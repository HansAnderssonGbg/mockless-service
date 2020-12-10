create table mockless.customer
(
    id             SERIAL NOT NULL
        constraint customer_pkey
            primary key,
    first_name     varchar,
    last_name      varchar
);