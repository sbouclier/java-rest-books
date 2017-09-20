-- books
insert into book(isbn,title,publisher) values ('978-0321356680','Effective Java','Addison Wesley');
insert into book(isbn,title,publisher) values ('978-1617292545','Spring Boot in Action','Manning Publications');
insert into book(isbn,title,publisher) values ('978-1491900864','Java 8 Pocket Guide','O''Reilly');
insert into book(isbn,title,publisher) values ('978-0321349606','Java Concurrency in Practice','Addison Wesley');

-- authors
insert into book_authors(book_id,first_name,last_name) values (1,'Joshua', 'Blosh');
insert into book_authors(book_id,first_name,last_name) values (2,'Craig', 'Walls');
insert into book_authors(book_id,first_name,last_name) values (3,'Robert', 'Liguori');
insert into book_authors(book_id,first_name,last_name) values (3,'Patricia', 'Liguori');
insert into book_authors(book_id,first_name,last_name) values (4,'Brian', 'Goetz');
insert into book_authors(book_id,first_name,last_name) values (4,'Joshua', 'Blosh');
insert into book_authors(book_id,first_name,last_name) values (4,'Joseph', 'Bowbeer');
insert into book_authors(book_id,first_name,last_name) values (4,'Tim', 'Peierls');