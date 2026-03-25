ALTER TABLE auction_inquiry
    ADD COLUMN title VARCHAR(50) AFTER writer_id;

UPDATE auction_inquiry
SET title = '기존 문의글입니다'
WHERE title IS NULL;

ALTER TABLE auction_inquiry
    MODIFY COLUMN title VARCHAR(50) NOT NULL;