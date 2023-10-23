ALTER TABLE cards
    ADD COLUMN isOwned boolean NOT NULL DEFAULT false,
    ADD COLUMN isOwnedFoil boolean NOT NULL DEFAULT false
