CREATE TABLE IF NOT EXISTS `genre` (
    `id` INTEGER PRIMARY KEY NOT NULL,
    `name` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `media_type_genre` (
    `type` TEXT NOT NULL,
    `genre_id` INTEGER NOT NULL,
    PRIMARY KEY(`type`, `genre_id`)
);

CREATE INDEX IF NOT EXISTS `index_media_type_genre_type` ON `media_type_genre` (`type`);
CREATE INDEX IF NOT EXISTS `index_media_type_genre_genre_id` ON `media_type_genre` (`genre_id`);

CREATE TABLE IF NOT EXISTS `pending_favorite` (
    `id` INTEGER NOT NULL,
    `type` TEXT NOT NULL,
    `title` TEXT NOT NULL,
    `poster_path` TEXT NOT NULL,
    `last_update` INTEGER NOT NULL,
    PRIMARY KEY(`type`, `id`)
);

INSERT OR REPLACE INTO `pending_favorite` (`id`, `type`, `title`, `poster_path`, `last_update`)
SELECT `id`, `type`, CASE WHEN `name` <> '' THEN `name` ELSE `title` END, `poster_path`, `last_update`
FROM `media`
WHERE `is_liked` = 1;

DROP TABLE IF EXISTS media_type_cross_ref;
DROP TABLE IF EXISTS media_types;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS media;

ALTER TABLE `streaming` RENAME TO `catalog`;
