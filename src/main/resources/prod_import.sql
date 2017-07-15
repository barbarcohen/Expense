insert into Category (NAME) VALUES ('Home');
insert into Category (NAME, PARENT_ID) VALUES ('Gloceries', (SELECT(id) FROM Category WHERE name = 'Home'));

insert into Category (NAME) VALUES ('Party');
insert into Category (NAME) VALUES ('Snack');
insert into Category (NAME) VALUES ('Restaurant');
insert into Category (NAME) VALUES ('Lunch');
insert into Category (NAME) VALUES ('Movies');
insert into Category (NAME) VALUES ('Books');
insert into Category (NAME) VALUES ('Electronics');
