CREATE OR REPLACE FUNCTION update_modified_at_column()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = now();
    return NEW;
END;
$$ language 'plpgsql';

DO
$$
    BEGIN
        CREATE TYPE User_Type_Enum as ENUM ('ADMIN', 'DEFAULT');
    EXCEPTION
        WHEN duplicate_object THEN null;
    END
$$;

CREATE TABLE IF NOT EXISTS "users"
(
    "id"                INT            NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "plex_username"     TEXT           NOT NULL UNIQUE,
    "plex_id"           INT            NOT NULL UNIQUE,
    "plex_token"        TEXT           NOT NULL,
    "email"             TEXT           NOT NULL UNIQUE,
    "user_type"         User_Type_Enum NOT NULL,
    "request_count"     INT            NOT NULL DEFAULT 0,
    "movie_quota_limit" INT            NOT NULL DEFAULT 5,
    "movie_quota_days"  INT            NOT NULL DEFAULT 1,
    "tv_quota_limit"    INT            NOT NULL DEFAULT 5,
    "tv_quota_days"     INT            NOT NULL DEFAULT 1,
    "created_at"        TIMESTAMP               DEFAULT now() NOT NULL,
    "modified_at"       TIMESTAMP               DEFAULT now() NOT NULL
);

CREATE INDEX ON users (created_at);
CREATE INDEX ON users (modified_at);

CREATE TRIGGER user_update_modified_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE update_modified_at_column();
