drop table if exists Users;
create table Users (
    username varchar(20) primary key,
    passwords varchar(20) not null
);

insert into Users values
    ('root', 'rootuser'),
    ('zyx', 'zyxzyx'),
    ('cx', 'cxcxcx'),
    ('lhc', 'lhclhc'),
    ('gxx', 'gxxgxx');


drop table if exists Messages;
create table Messages(
    username varchar(20),
    messageNo int,
    contexts varchar(1023),
    primary key (username, messageNo)
);

