DO
$$
    BEGIN
        CREATE TYPE Request_Status_Enum as ENUM ('REQUESTED', 'FULFILLED', 'REJECTED', 'WAITING', 'IMPORTED', 'DOWNLOADING');
    EXCEPTION
        WHEN duplicate_object THEN NULL;
    END;
$$ language 'plpgsql';

DO
$$
    BEGIN
        CREATE TYPE Media_Type_Enum as ENUM ('MOVIE', 'TV');
    EXCEPTION
        WHEN duplicate_object THEN NULL;
    END;
$$ language 'plpgsql';

CREATE TABLE IF NOT EXISTS "requests"
(
    "id"               INT                 NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "tmdb_id"          INT                 NOT NULL,
    "media_type"       Media_Type_Enum     NOT NULL,
    "title"            TEXT                NOT NULL,
    "poster_path"      TEXT                NOT NULL,
    "release_date"     TIMESTAMP           NULL,
    "status"           Request_Status_Enum NOT NULL DEFAULT 'REQUESTED',
    "requester_id"     INT                 NOT NULL REFERENCES users (id),
    "rejection_reason" TEXT                NULL,
    "date_requested"   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "date_fulfilled"   TIMESTAMP           NULL,
    "date_rejected"    TIMESTAMP           NULL,
    "created_at"       TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "modified_at"      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX on "requests" ("created_at");
CREATE INDEX on "requests" ("modified_at");
CREATE INDEX on "requests" ("requester_id");
CREATE INDEX on "requests" ("status");
CREATE UNIQUE INDEX on "requests" ("media_type", "tmdb_id");

CREATE TRIGGER "requests_modified_at_update"
    BEFORE UPDATE
    ON "requests"
    FOR EACH ROW
EXECUTE PROCEDURE update_modified_at_column();