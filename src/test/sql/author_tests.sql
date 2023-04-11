BEGIN;
SELECT plan( 5 );

-- Test table and columns
SELECT has_table( 'author' );

SELECT has_column( 'author', 'author_id' );
SELECT has_column( 'author', 'first_name' );
SELECT has_column( 'author', 'last_name' );

-- Test keys
SELECT col_is_pk( 'author', 'author_id' );

SELECT * FROM finish();
ROLLBACK;