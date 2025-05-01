create table users(
    id serial primary key,
    name varchar(20) not null,
    email varchar(40) not null,
    password varchar(20) not null,
    house_id serial not null,
    foreign key (house_id) references houses(id) on delete cascade
);