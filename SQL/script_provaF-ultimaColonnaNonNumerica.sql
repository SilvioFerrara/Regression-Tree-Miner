USE MapDB;

CREATE TABLE MapDB.provaF(X varchar(10),Y float(5,2),C varchar(10));

insert into MapDB.provaF values('A',2,'1');
insert into MapDB.provaF values('A',2,'2');
insert into MapDB.provaF values('A',1,'3');
commit;


SHOW TABLES FROM MapDb;
SELECT * FROM provaF;