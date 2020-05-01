CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    passhash VARCHAR(255) NOT NULL
);

CREATE TABLE profiles(
profile_name varchar(255),
id int not null,
profile_picture BLOB,
bio varchar(255),
public_email varchar(255),
location varchar(255),
FOREIGN KEY profile_id(id)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE events(
event_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
street varchar(255),
city varchar(255),
state varchar(255),
country varchar(255),
event_date DATE,
doors_open_at varchar(255),
doors_close_at varchar(255),
description varchar(255),
sponsors varchar(255),
event_name varchar(255),
banner BLOB,
overview BLOB,
creator_id int not null,
FOREIGN KEY profile_id(creator_id)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE sectors(
sector_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
sector_name varchar(255),
quantity_of_tickets int,
quantity_of_rows int,
quantity_of_seats_per_row int,
ticket_adult_price double,
ticket_senior_student_price double,
ticket_children_price double,
event_id int not null,
FOREIGN KEY sector_event_id(event_id)
REFERENCES events(event_id)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE tickets(
ticket_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
ticket_type varchar(255),
row int,
price double,
seat_number int,
sector_name varchar(255),
event_id int not null,
FOREIGN KEY sector_event_id(event_id)
REFERENCES events(event_id)
ON UPDATE CASCADE
ON DELETE RESTRICT,
sector_id int not null,
FOREIGN KEY sector_id(sector_id)
REFERENCES sectors(sector_id)
ON UPDATE CASCADE
ON DELETE RESTRICT,
owner_id int,
FOREIGN KEY profile_id(owner_id)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT
);
