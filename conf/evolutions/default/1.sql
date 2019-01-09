# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table task (
  id                            serial not null,
  name                          varchar(255),
  is_task_complete              boolean,
  constraint pk_task primary key (id)
);

create table person (
  id                            serial not null,
  name                          varchar(255),
  constraint pk_person primary key (id)
);


# --- !Downs

drop table if exists task cascade;

drop table if exists person cascade;

