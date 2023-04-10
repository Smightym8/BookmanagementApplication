BEGIN;
SELECT plan( 11 );

-- Test table and columns
SELECT has_table( 'book' );

SELECT has_column( 'book', 'book_id' );
SELECT has_column( 'book', 'isbn' );
SELECT has_column( 'book', 'title' );
SELECT has_column( 'book', 'publication_date' );
SELECT has_column( 'book', 'price' );
SELECT has_column( 'book', 'genre' );

-- Test keys
SELECT col_is_pk( 'book', 'book_id' );

-- Test constraints
SELECT col_is_unique( 'book', 'isbn' );

PREPARE test_price_check AS INSERT INTO book (isbn, title, publication_date, price, genre)
VALUES ('1234567899873', 'title', '2017-03-14', -9.99, 'Novel');
SELECT throws_like(
   'test_price_check',
   '%"ck_positive_price_check"',
   'Expected error for price check constraint'
);

PREPARE test_isbn_unique AS INSERT INTO book (isbn, title, publication_date, price, genre)
VALUES ('1234567891234', 'title3', '2018-04-14', 9.99, 'Novel');
SELECT throws_ok(
    'test_isbn_unique',
    '23505',
    'duplicate key value violates unique constraint "unique_book_isbn"'
);

SELECT * FROM finish();
ROLLBACK;