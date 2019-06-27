CREATE TABLE posts (
    `id` BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(150) NOT NULL,
    `body` TEXT,
    `domain` varchar(10),
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    `created_by` BIGINT,
    `updated_by` BIGINT,
    UNIQUE INDEX `i_posts_title` (`domain`, `title`),
    INDEX `i_posts_created_at` (`domain`,`created_at`),
    INDEX `i_posts_updated_at` (`domain`,`updated_at`),
    INDEX `i_posts_created_by` (`domain`,`created_by`),
    INDEX `i_posts_updated_by` (`domain`,`updated_by`)
)
