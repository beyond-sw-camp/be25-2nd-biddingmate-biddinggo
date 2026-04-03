ALTER TABLE point_history
    MODIFY type VARCHAR(20)
    CHECK (`type` IN ('CHARGE','BID','EXCHANGE','REFUND'));