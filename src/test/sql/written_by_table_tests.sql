BEGIN;
SELECT plan( 10 );

-- Test table and columns
SELECT has_table( 'written_by' );

SELECT has_column( 'written_by', 'book_id' );
SELECT has_column( 'written_by', 'author_id' );

-- Test keys
SELECT col_is_pk( 'written_by', ARRAY['book_id', 'author_id'] );
SELECT col_is_fk( 'written_by', 'book_id' );
SELECT col_is_fk( 'written_by', 'author_id' );

-- Test relationships
SELECT fk_ok( 'written_by', 'book_id', 'book', 'book_id' );
SELECT fk_ok( 'written_by', 'author_id', 'author', 'author_id' );

-- Test referential integrity
PREPARE test_book_id_fk_valid AS INSERT INTO written_by (book_id, author_id)
VALUES (200, 100);
SELECT throws_like(
   'test_book_id_fk_valid',
   '%"written_by_book_id_fk"',
   'Expected error for referential integrity of book_id_fk'
);

PREPARE test_author_id_fk_valid AS INSERT INTO written_by (book_id, author_id)
 VALUES (100, 200);
SELECT throws_like(
   'test_author_id_fk_valid',
   '%"written_by_author_id_fk"',
   'Expected error for referential integrity of author_id_fk'
);


SELECT * FROM finish();
ROLLBACK;