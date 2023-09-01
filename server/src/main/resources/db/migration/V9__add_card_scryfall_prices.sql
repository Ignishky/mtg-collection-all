ALTER TABLE cards
    ADD COLUMN scryfall_prices varchar not null default '0|0|0|0';

ALTER TABLE events
    ALTER COLUMN payload TYPE varchar;
