create table sys_user
(
   username varchar(255) not null,
   password varchar(255) not null,
   name varchar(255),
   email varchar(255),
   primary key(username)
);