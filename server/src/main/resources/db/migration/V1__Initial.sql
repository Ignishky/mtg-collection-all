CREATE TABLE events
(
    id             bigserial PRIMARY KEY,
    aggregate_id   VARCHAR(36)  NOT NULL,
    aggregate_name VARCHAR(255) NOT NULL,
    label          VARCHAR(255) NOT NULL,
    instant        TIMESTAMP WITH TIME ZONE,
    payload        VARCHAR(255) NOT NULL,
    correlation_id VARCHAR(36)  NOT NULL
);
