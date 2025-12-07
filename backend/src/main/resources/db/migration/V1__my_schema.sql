-- V1__my_schema.sql
-- Schema migration with proper data handling

-- Drop old foreign key constraint if it exists
ALTER TABLE geometries
    DROP CONSTRAINT IF EXISTS fkir4gn5kr6wjuyeyddrukbu3ns;

-- Add new columns to assets
ALTER TABLE assets
    ADD COLUMN IF NOT EXISTS asset_id VARCHAR(255);

ALTER TABLE assets
    ADD COLUMN IF NOT EXISTS asset_name VARCHAR(255);

-- Migrate data from old columns to new columns
UPDATE assets
SET asset_id = item_id
WHERE asset_id IS NULL;

UPDATE assets
SET asset_name = item_name
WHERE asset_name IS NULL;

-- Now safe to make NOT NULL
ALTER TABLE assets
    ALTER COLUMN asset_name SET NOT NULL;

ALTER TABLE assets
    ALTER COLUMN asset_id SET NOT NULL;

-- Drop old primary key constraint (on item_id)
ALTER TABLE assets
    DROP CONSTRAINT IF EXISTS assets_pkey;

-- Add primary key on new column
ALTER TABLE assets
    ADD CONSTRAINT pk_assets PRIMARY KEY (asset_id);

-- Update geometries foreign key to point to new column
UPDATE geometries g
SET asset_id = (SELECT a.asset_id FROM assets a WHERE a.item_id = g.asset_id)
WHERE EXISTS (SELECT 1 FROM assets a WHERE a.item_id = g.asset_id);

-- Add new foreign key constraint
ALTER TABLE geometries
    ADD CONSTRAINT FK_GEOMETRIES_ON_ASSET FOREIGN KEY (asset_id) REFERENCES assets (asset_id);

-- Drop old models_3d table if exists
DROP TABLE IF EXISTS models_3d CASCADE;

-- Drop old columns (now safe because data is migrated)
ALTER TABLE assets
    DROP COLUMN IF EXISTS item_id;

ALTER TABLE assets
    DROP COLUMN IF EXISTS item_name;

ALTER TABLE assets
    DROP COLUMN IF EXISTS lods;

-- Add new lods column with array type
ALTER TABLE assets
    ADD COLUMN lods INTEGER[] NOT NULL DEFAULT '{}';

-- Make created_at NOT NULL
ALTER TABLE assets
    ALTER COLUMN created_at SET NOT NULL;