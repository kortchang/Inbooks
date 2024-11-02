CREATE SCHEMA IF NOT EXISTS public;
CREATE TABLE users
(
    id           uuid PRIMARY KEY,
    display_name TEXT                                NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified_at  TIMESTAMP                           NULL
);
CREATE TABLE authentications
(
    id                            uuid PRIMARY KEY,
    user_id                       uuid                                NOT NULL,
    email                         TEXT                                NOT NULL,
    email_verified                BOOLEAN   DEFAULT FALSE             NOT NULL,
    email_verification_code       TEXT                                NULL,
    email_verification_expired_at TIMESTAMP                           NULL,
    password_hash                 TEXT                                NULL,
    created_at                    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified_at                   TIMESTAMP                           NULL,
    CONSTRAINT fk_authentications_user_id__id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE OR REPLACE FUNCTION update_modified_at_column()
    RETURNS TRIGGER AS
$$
BEGIN
    -- 如果表中有 modifiedAt 欄位且其他欄位有變更，才更新 modifiedAt
    IF (NEW.* IS DISTINCT FROM OLD.* AND NEW.modified_at IS NOT DISTINCT FROM OLD.modified_at) THEN
        NEW.modified_at := NOW();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER update_modified_at_users
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION update_modified_at_column();

CREATE OR REPLACE TRIGGER update_modified_at_authentications
    BEFORE UPDATE
    ON authentications
    FOR EACH ROW
EXECUTE FUNCTION update_modified_at_column();