ALTER TABLE written_by
ADD CONSTRAINT written_by_book_id_fk
FOREIGN KEY (book_id) REFERENCES book(book_id)
ON DELETE CASCADE;

ALTER TABLE written_by
ADD CONSTRAINT written_by_author_id_fk
FOREIGN KEY (author_id) REFERENCES author(author_id)
ON DELETE CASCADE;