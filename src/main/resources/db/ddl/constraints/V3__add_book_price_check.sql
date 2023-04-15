ALTER TABLE book
ADD CONSTRAINT ck_positive_price_check
CHECK ( price > 0 );