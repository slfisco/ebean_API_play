# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  username                      varchar(255) not null,
  password                      varchar(255),
  constraint pk_account primary key (username)
);

create table task (
  id                            serial not null,
  name                          varchar(255),
  is_task_complete              boolean,
  account_name                  varchar(255),
  constraint pk_task primary key (id)
);


# --- !Downs

drop table if exists account cascade;

drop table if exists task cascade;

