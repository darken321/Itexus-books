-- DROP DATABASE IF EXISTS postgres;

CREATE TABLE authors (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE genres (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE books (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       genre_id INT,
                       author_id INT,
                       FOREIGN KEY (genre_id) REFERENCES genres(id),
                       FOREIGN KEY (author_id) REFERENCES authors(id)
);

INSERT INTO authors (name) VALUES
                               ('F. Scott Fitzgerald'),
                               ('George Orwell');

INSERT INTO genres (name) VALUES
                              ('Classic'),
                              ('Science Fiction'),
                              ('Dystopian');

INSERT INTO books (title, description, genre_id, author_id) VALUES
                                                                ('The Great Gatsby', 'A novel set in the Roaring Twenties.', 1, 1),
                                                                ('1984', 'A dystopian social science fiction novel and cautionary tale.', 2, 2);