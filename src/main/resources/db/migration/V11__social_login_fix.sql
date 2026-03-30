-- username의 길이를 20에서 100으로 변경
ALTER TABLE `member` MODIFY COLUMN `username` VARCHAR(100) UNIQUE;