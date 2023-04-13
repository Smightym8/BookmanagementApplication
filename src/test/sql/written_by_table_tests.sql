BEGIN;
SELECT plan( 8 );

-- Test table and columns
SELECT has_table( 'written_by' );

SELECT has_column( 'written_by', 'book_id' );
SELECT has_column( 'written_by', 'author_id' );

-- Test keys
SELECT col_is_pk( 'written_by', ARRAY['book_id', 'author_id'] );
SELECT col_is_fk( 'written_by', 'book_id' );
SELECT col_is_fk( 'written_by', 'author_id' );

SELECT fk_ok( 'written_by', 'book_id', 'book', 'book_id' );
SELECT fk_ok( 'written_by', 'author_id', 'author', 'author_id' );

-- Test constraints


SELECT * FROM finish();
ROLLBACK;