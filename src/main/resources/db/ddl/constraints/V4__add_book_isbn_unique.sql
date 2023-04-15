ALTER TABLE book
ADD CONSTRAINT unique_book_isbn
UNIQUE (isbn);