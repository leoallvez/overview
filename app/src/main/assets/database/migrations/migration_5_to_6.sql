-- Recreates `media` with primary key (`id`, `type`): TMDB ids are only unique
-- per media type, and SQLite cannot alter a primary key in place.
CREATE TABLE IF NOT EXISTS `media_new` (
    `id` INTEGER NOT NULL,
    `name` TEXT NOT NULL,
    `title` TEXT NOT NULL,
    `poster_path` TEXT NOT NULL,
    `type` TEXT NOT NULL,
    `is_liked` INTEGER NOT NULL,
    `last_update` INTEGER NOT NULL,
    PRIMARY KEY(`id`, `type`)
);

INSERT INTO `media_new` (`id`, `name`, `title`, `poster_path`, `type`, `is_liked`, `last_update`)
SELECT `id`, `name`, `title`, `poster_path`, `type`, `is_liked`, `last_update` FROM `media`;

DROP TABLE `media`;

ALTER TABLE `media_new` RENAME TO `media`;
