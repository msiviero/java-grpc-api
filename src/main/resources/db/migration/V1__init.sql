CREATE TABLE users (
    id int NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    token varchar(255),
    PRIMARY KEY (id)
);

INSERT INTO users (username, password)
VALUES ('m.siviero83@gmail.com', 'my-password');
