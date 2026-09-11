CREATE TABLE IF NOT EXISTS `genre` (
    `id` INTEGER PRIMARY KEY NOT NULL,
    `name` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `media_type` (
    `type` TEXT NOT NULL,
    PRIMARY KEY(`type`)
);

CREATE TABLE IF NOT EXISTS `media_type_genre` (
    `type` TEXT NOT NULL,
    `genre_id` INTEGER NOT NULL,
    PRIMARY KEY(`type`, `genre_id`)
);

CREATE INDEX IF NOT EXISTS `index_media_type_genre_type` ON `media_type_genre` (`type`);
CREATE INDEX IF NOT EXISTS `index_media_type_genre_genre_id` ON `media_type_genre` (`genre_id`);

DROP TABLE IF EXISTS media_type_cross_ref;
DROP TABLE IF EXISTS media_types;
DROP TABLE IF EXISTS genres;

ALTER TABLE `streaming` RENAME TO `catalog`;
