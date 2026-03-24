ALTER TABLE auction_inquiry
    MODIFY status VARCHAR(20)
    CHECK (status IN ('PENDING','ANSWERED'));

UPDATE auction_inquiry
SET status = 'PENDING'
WHERE status = 'ACTIVE';