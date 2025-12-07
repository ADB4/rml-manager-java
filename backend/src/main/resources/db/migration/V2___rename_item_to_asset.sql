ALTER TABLE assets
    ADD asset_id BIGINT;

ALTER TABLE assets
    ADD asset_name VARCHAR(255);

ALTER TABLE assets
    ALTER COLUMN asset_name SET NOT NULL;

DROP TABLE models_3d CASCADE;

ALTER TABLE assets
    DROP COLUMN item_id;

ALTER TABLE assets
    DROP COLUMN created_at;

ALTER TABLE assets
    DROP COLUMN item_name;

ALTER TABLE assets
    DROP COLUMN updated_at;

ALTER TABLE assets
    DROP COLUMN lods;

ALTER TABLE assets
    ADD lods INTEGER[] NOT NULL;

ALTER TABLE assets
    ADD CONSTRAINT pk_assets PRIMARY KEY (asset_id);