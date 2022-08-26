CREATE SCHEMA logging;
CREATE TABLE logging.audit_log
(
    id             INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    time           TIMESTAMP DEFAULT now(),
    schema_name    text,
    table_name     text,
    operation      text,
    who            text      DEFAULT session_user,
    previous_value jsonb,
    new_value      jsonb
);

CREATE FUNCTION audit_trigger() RETURNS trigger AS
$$
BEGIN
    IF tg_op = 'INSERT'
    THEN
        INSERT INTO logging.audit_log(schema_name, table_name, operation, new_value)
        VALUES (tg_table_schema, tg_table_name, tg_op, to_jsonb(NEW));
        RETURN NEW;
    ELSIF tg_op = 'UPDATE'
    THEN
        INSERT INTO logging.audit_log(schema_name, table_name, operation, previous_value, new_value)
        VALUES (tg_table_schema, tg_table_name, tg_op, to_jsonb(OLD), to_jsonb(NEW));
        RETURN NEW;
    ElSIF tg_op = 'DELETE'
    THEN
        INSERT INTO logging.audit_log(schema_name, table_name, operation, previous_value)
        VALUES (tg_table_schema, tg_table_name, tg_op, to_jsonb(OLD));
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE 'plpgsql' SECURITY DEFINER;

CREATE TRIGGER user_audit_log
    BEFORE INSERT OR UPDATE OR DELETE
    on users
    FOR EACH ROW
EXECUTE PROCEDURE audit_trigger();

CREATE TRIGGER request_audit_log
    BEFORE INSERT OR UPDATE OR DELETE
    on requests
    FOR EACH ROW
EXECUTE PROCEDURE audit_trigger();
