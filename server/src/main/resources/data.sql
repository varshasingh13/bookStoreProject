DELETE FROM book;
ALTER TABLE book AUTO_INCREMENT = 1001;

DELETE FROM category;
ALTER TABLE category AUTO_INCREMENT = 1001;

INSERT INTO `category` (`name`) VALUES ('Fiction'),('NonFiction'),('Kids'),('Health');

INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('Gifted', 'Kora Greenwood', '', 12.62, 0, TRUE, FALSE, 1001);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('Stillhouse Lake', 'Rachel Caine', '', 12.22, 0, FALSE, FALSE, 1001);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Island', 'Natasha Preston', '', 6.77, 0, TRUE, FALSE, 1001);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Silent Patient', 'Alex Michaelides', '', 9.99, 0, FALSE, FALSE, 1001);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Lost Bones', 'Kendra Elliot', '', 9.50, 0, FALSE, FALSE, 1001);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The New Girl', 'Jesse Q. Sutanto', '', 8.53, 0, FALSE, FALSE, 1001);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('Alone', 'Megan E. Freeman', '', 9.89, 0, FALSE, FALSE, 1001);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Inmate', 'Freida McFadden', '', 10.50, 0, FALSE, FALSE, 1001);

INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('Rich Dad Poor Dad', 'Robert T. Kiyosaki', '', 16.50, 0, TRUE, FALSE, 1002);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('Why Nations Fail', 'Daron Acemoglu', '', 12.30, 0, TRUE, FALSE, 1002);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('Elon Musk', 'Walter Isaacson', '', 15.43, 0, FALSE, FALSE, 1002);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('Bloodlands', ' Timothy Snyder', '', 10.00, 0, FALSE, FALSE, 1002);


INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Happy Pumpkin', 'MacKenzie Haley', '', 6.99, 0, FALSE, FALSE, 1003);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Three Little Pigs', 'Giuseppe Di Lernia', '', 13.49, 0, TRUE, FALSE, 1003);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Rainbow Fish', 'Marcus Pfister', '', 6.74, 0, TRUE, FALSE, 1003);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Gingerbread Man', 'Gail Yerrill', '', 9.99, 0, FALSE, FALSE, 1003);

INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Fantastic Body', 'Howard Bennett', '',19.99, 0, FALSE, FALSE, 1004);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('Food Can Fix It', 'Mehmet Oz', '', 16.75, 0, FALSE, FALSE, 1004);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Health Gap', 'Michael Marmot', '', 27.29, 0, TRUE, FALSE, 1004);
INSERT INTO `book` (title, author, description, price, rating, is_public, is_featured, category_id) VALUES ('The Mindful Body', 'Ellen J. Langer', '',24.33, 0, TRUE, FALSE, 1004);