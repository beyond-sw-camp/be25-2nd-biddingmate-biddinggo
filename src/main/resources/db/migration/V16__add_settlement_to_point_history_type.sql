ALTER TABLE point_history
    MODIFY type VARCHAR(20),
    ADD CONSTRAINT chk_point_type CHECK (`type` IN ('CHARGE', 'BID', 'EXCHANGE', 'REFUND', 'SETTLEMENT'));