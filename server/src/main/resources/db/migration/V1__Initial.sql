CREATE TABLE events
(
    id             BIGSERIAL PRIMARY KEY,
    aggregate_id   VARCHAR(36) NOT NULL,
    aggregate_name VARCHAR     NOT NULL,
    label          VARCHAR     NOT NULL,
    instant        TIMESTAMP WITH TIME ZONE,
    payload        VARCHAR     NOT NULL,
    correlation_id VARCHAR(36) NOT NULL
);

CREATE TABLE sets
(
    id          VARCHAR(36) PRIMARY KEY,
    code        VARCHAR(6) NOT NULL,
    name        VARCHAR    NOT NULL,
    type        VARCHAR    NOT NULL,
    icon        VARCHAR    NOT NULL,
    released_at DATE       NOT NULL
);

CREATE TABLE cards
(
    id                VARCHAR(36) PRIMARY KEY,
    name              VARCHAR    NOT NULL,
    set_code          VARCHAR(6) NOT NULL,
    images            VARCHAR    NOT NULL,
    collection_number VARCHAR    NOT NULL,
    scryfall_prices   VARCHAR    NOT NULL DEFAULT '0|0|0|0'
);
