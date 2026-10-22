CREATE TABLE IF NOT EXISTS `streaming` (
    `id` INTEGER PRIMARY KEY NOT NULL,
    `name` TEXT NOT NULL,
    `priority` INTEGER NOT NULL,
    `logo_path` TEXT NOT NULL,
    `display` INTEGER NOT NULL,
    `last_update` INTEGER NOT NULL
);

DROP TABLE streamings;
