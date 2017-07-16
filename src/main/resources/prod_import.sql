insert into Category (NAME) VALUES ('Domácnost');
insert into Category (NAME, PARENT_ID) VALUES ('Potraviny', (SELECT(id) FROM Category WHERE name = 'Domácnost'));
insert into Category (NAME, PARENT_ID) VALUES ('Hygienické potřeby', (SELECT(id) FROM Category WHERE name = 'Domácnost'));
insert into Category (NAME, PARENT_ID) VALUES ('Kutilství', (SELECT(id) FROM Category WHERE name = 'Domácnost'));

/*TODO add localization*/
insert into Category (NAME) VALUES ('Party');
insert into Category (NAME) VALUES ('Snack');
insert into Category (NAME) VALUES ('Restaurant');
insert into Category (NAME) VALUES ('Lunch');
insert into Category (NAME) VALUES ('Movies');
insert into Category (NAME) VALUES ('Books');
insert into Category (NAME) VALUES ('Electronics');
