-- Auto update trigger
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = now();
    return NEW;
END;
$$ language 'plpgsql';

-- Users table
ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP DEFAULT now() NOT NULL;
ALTER TABLE users
    ADD COLUMN modified_at TIMESTAMP DEFAULT now() NOT NULL;

CREATE INDEX ON users (created_at);
CREATE INDEX on users (modified_at);
CREATE TRIGGER user_update_modified_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_column();

-- Media Items Table
ALTER TABLE media_items
    ADD COLUMN created_at TIMESTAMP DEFAULT now() NOT NULL;
ALTER TABLE media_items
    ADD COLUMN modified_at TIMESTAMP DEFAULT now() NOT NULL;

CREATE INDEX ON media_items (created_at);
CREATE INDEX on media_items (modified_at);
CREATE TRIGGER media_items_update_modified_at
    BEFORE UPDATE
    ON media_items
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_column();


-- Requests Table
ALTER TABLE requests
    ADD COLUMN created_at TIMESTAMP DEFAULT now() NOT NULL;
ALTER TABLE requests
    ADD COLUMN modified_at TIMESTAMP DEFAULT now() NOT NULL;

CREATE INDEX ON requests (created_at);
CREATE INDEX on requests (modified_at);
CREATE TRIGGER requests_update_modified_at
    BEFORE UPDATE
    ON requests
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_column();


