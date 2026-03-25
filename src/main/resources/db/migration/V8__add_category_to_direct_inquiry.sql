ALTER TABLE direct_inquiry
    ADD COLUMN category VARCHAR(20) NOT NULL AFTER admin_id,
    MODIFY COLUMN `admin_id` BIGINT(20) NULL;