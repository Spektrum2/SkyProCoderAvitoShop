--liquibase formatted sql

-- changeset aleksandr:1
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'image'
CREATE TABLE image
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  TEXT,
    file_size  BIGINT,
    media_type TEXT,
    data       BYTEA
);

-- changeset aleksandr:2
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'user'
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    first_name TEXT,
    last_name  TEXT,
    email      TEXT,
    phone      TEXT,
    reg_date   TIMESTAMP,
    role       VARCHAR(5),
    image_id   BIGINT REFERENCES image (id)
);

-- changeset aleksandr:3
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'ads'
CREATE TABLE ads
(
    id                BIGSERIAL PRIMARY KEY,
    author_first_name TEXT,
    author_last_name  TEXT,
    description       TEXT,
    email             TEXT,
    phone             TEXT,
    price             NUMERIC(7, 2),
    title             TEXT,
    image_id          BIGINT REFERENCES image (id),
    author_id         BIGINT REFERENCES users (id)
);

-- changeset aleksandr:4
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'comment'
CREATE TABLE comment
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    text       TEXT,
    author_id  BIGINT REFERENCES users (id)
);



