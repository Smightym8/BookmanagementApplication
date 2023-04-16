-- Add new column for foreign key to genre table
ALTER TABLE book
ADD COLUMN genre_id_fk INTEGER;

-- Set genre_id_fk as foreign key
ALTER TABLE book
ADD CONSTRAINT book_genre_id_fk
FOREIGN KEY (genre_id_fk) REFERENCES genre(genre_id);

-- Set genre id based on old genre column
UPDATE book b SET genre_id_fk = (
    SELECT genre_id from genre g
    where g.name = b.genre
);

-- Set new genre column to not null
ALTER TABLE book
ALTER COLUMN genre_id_fk SET NOT NULL;

-- Drop old genre column
ALTER TABLE book DROP column genre;