# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  username                      varchar(255) not null,
  password                      varchar(255),
  constraint pk_account primary key (username)
);

create table task (
  id                            integer auto_increment not null,
  name                          varchar(255),
  is_task_complete              boolean,
  constraint pk_task primary key (id)
);

create table person (
  id                            integer auto_increment not null,
  name                          varchar(255),
  constraint pk_person primary key (id)
);


# --- !Downs

drop table if exists account;

drop table if exists task;

drop table if exists person;

