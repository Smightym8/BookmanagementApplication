BEGIN;
SELECT plan( 7 );

-- Test table and columns
SELECT has_table( 'genre' );

SELECT has_column( 'genre', 'genre_id' );
SELECT has_column( 'genre', 'name' );

-- Test keys
SELECT col_is_pk( 'genre', 'genre_id' );

-- Check if reference data is present
SELECT results_eq(
   'SELECT name FROM genre',
   ARRAY['Thriller', 'Novel', 'Science Fiction', 'Fantasy', 'Horror', 'Non-fiction book', 'Reference book', 'Biography'],
   'The names are correct'
);

-- Test constraints
SELECT col_is_unique( 'genre', 'name' );

PREPARE test_name_unique AS INSERT INTO genre(name)
VALUES ('Fantasy');
SELECT throws_ok(
   'test_name_unique',
   '23505',
   'duplicate key value violates unique constraint "unique_genre_name"'
);

SELECT * FROM finish();
ROLLBACK;