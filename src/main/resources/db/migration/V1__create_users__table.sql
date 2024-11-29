create table users(
    id serial primary key,
    name varchar(20) not null,
    email varchar(40) not null,
    password varchar(20) not null
);