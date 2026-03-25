ALTER TABLE direct_inquiry
    MODIFY COLUMN `admin_id` BIGINT(20) NULL,
    ADD COLUMN category VARCHAR(20) NOT NULL AFTER admin_id;