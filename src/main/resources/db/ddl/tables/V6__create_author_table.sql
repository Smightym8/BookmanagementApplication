CREATE TABLE Author(
    author_id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 100 INCREMENT BY 1),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL
);