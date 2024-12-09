create table locals(
    id serial primary key,
    street varchar(255) not null,
    number int not null,
    postal char(8) not null,
    name varchar(255) not null,
    description varchar(255) not null,
    type varchar(255) not null,
    user_id serial not null,
    foreign key (user_id) references users(id) on delete cascade
);

create table houses(
    id serial primary key,
    local_id serial not null,
    description varchar(255) not null,
    foreign key (local_id) references locals(id) on delete cascade
);

create table users_houses(
    user_id serial not null,
    house_id serial not null,
    primary key (user_id,house_id),
    foreign key (user_id) references users(id) on delete cascade,
    foreign key (house_id) references houses(id) on delete cascade
);