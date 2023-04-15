ALTER TABLE written_by
ADD CONSTRAINT pk_written_by_bookid_authorid
PRIMARY KEY (book_id, author_id);