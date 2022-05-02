DO
$$
    BEGIN
        CREATE TYPE Media_Type_Enum as ENUM ('MOVIE', 'TV');
    EXCEPTION
        WHEN duplicate_object THEN null;
    END
$$;

DO
$$
    BEGIN
        CREATE TYPE Request_Status_Enum as ENUM (
            'REQUESTED', 'FULFILLED', 'REJECTED', 'CANCELLED', 'WAITING', 'IMPORTED', 'DOWNLOADING'
            );
    EXCEPTION
        WHEN duplicate_object THEN null;
    END
$$;

CREATE TABLE IF NOT EXISTS "users"
(
    "id"          INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "external_id" TEXT NOT NULL UNIQUE,
    "email"       TEXT NOT NULL UNIQUE,
    "name"        TEXT DEFAULT 'Unavailable'
);

CREATE TABLE IF NOT EXISTS "media_items"
(
    "id"             INT             NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "media_type"     Media_Type_Enum NOT NULL,
    "overview"       TEXT DEFAULT '',
    "release_date"   DATE            NULL,
    "title"          TEXT,
    "poster_url"     TEXT            NULL,
    "background_url" TEXT            NULL,
    "tmdb_url"       TEXT            NOT NULL,
    "popularity"     FLOAT           NOT NULL,
    "tmdb_id"        INT             NOT NULL
);

CREATE UNIQUE INDEX ON "media_items" ("media_type", "tmdb_id");

CREATE TABLE IF NOT EXISTS "requests"
(
    "date_cancelled"   TIMESTAMP                       NULL,
    "date_fulfilled"   TIMESTAMP                       NULL,
    "date_rejected"    TIMESTAMP                       NULL,
    "date_requested"   TIMESTAMP                       NOT NULL DEFAULT now(),
    "rejection_reason" TEXT                            NULL,
    "status"           Request_Status_Enum             NOT NULL,
    "user_id"          INT REFERENCES users (id)       NOT NULL,
    "media_id"         INT REFERENCES media_items (id) NOT NULL
);

CREATE INDEX ON "requests" ("user_id");
CREATE INDEX ON "requests" ("media_id");

