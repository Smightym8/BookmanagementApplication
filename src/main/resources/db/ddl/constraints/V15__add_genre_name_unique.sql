ALTER TABLE genre
ADD CONSTRAINT unique_genre_name
UNIQUE (name);