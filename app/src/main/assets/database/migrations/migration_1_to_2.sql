CREATE TABLE IF NOT EXISTS `media` (
    `id` INTEGER PRIMARY KEY NOT NULL,
    `name` TEXT NOT NULL,
    `title` TEXT NOT NULL,
    `poster_path` TEXT NOT NULL,
    `type` TEXT NOT NULL,
    `is_liked` INTEGER NOT NULL,
    `last_update` INTEGER NOT NULL
);

INSERT INTO media (
   id,
   name,
   title,
   poster_path,
   type,
   is_liked,
   last_update
)
SELECT
   m.api_id,
   m.letter,
   m.letter,
   m.poster_path,
   m.type,
   m.is_liked,
   m.last_update
FROM medias m
WHERE NOT EXISTS (
  SELECT
    1
  FROM
    media
  WHERE
    media.id = m.api_id
);

DROP TABLE medias;
