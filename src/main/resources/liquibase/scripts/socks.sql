--liquibase formatted sql

-- changeset aleksandr:1
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'socks'
CREATE TABLE socks
(
    id       BIGSERIAL PRIMARY KEY,
    color    VARCHAR(255),
    cotton_part  INTEGER,
    quantity INTEGER
)

-- changeset aleksandr:2
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'registration'
CREATE TABLE registration
(
    id       BIGSERIAL PRIMARY KEY,
    date     TIMESTAMP,
    quantity INTEGER,
    status   VARCHAR(7),
    socks_id BIGINT REFERENCES socks (id)
)