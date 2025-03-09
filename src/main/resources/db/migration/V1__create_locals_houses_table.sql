create table locals(
    id serial primary key,
    street varchar(255) not null,
    number int not null,
    postal char(8) not null,
    name varchar(255) not null,
    description varchar(255) not null,
    type varchar(255) not null
);

create table houses(
    id serial primary key,
    local_id serial not null,
    description varchar(255) not null,
    foreign key (local_id) references locals(id) on delete cascade
);