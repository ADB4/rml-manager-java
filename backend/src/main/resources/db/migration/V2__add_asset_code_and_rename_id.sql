-- V2__add_asset_code_and_rename_id.sql
-- Add asset_code field and rename asset_id to id

-- Step 1: Add asset_code column
ALTER TABLE assets
    ADD COLUMN asset_code VARCHAR(255);

-- Step 2: Populate asset_code with existing asset_id values (or modify as needed)
UPDATE assets
SET asset_code = asset_id
WHERE asset_code IS NULL;

-- Step 3: Make asset_code NOT NULL
ALTER TABLE assets
    ALTER COLUMN asset_code SET NOT NULL;

-- Step 4: Add unique constraint to asset_code
ALTER TABLE assets
    ADD CONSTRAINT uk_assets_asset_code UNIQUE (asset_code);

-- Step 5: Drop foreign key constraint from geometries
ALTER TABLE geometries
    DROP CONSTRAINT IF EXISTS FK_GEOMETRIES_ON_ASSET;

-- Step 6: Drop primary key on assets
ALTER TABLE assets
    DROP CONSTRAINT IF EXISTS pk_assets;

-- Step 7: Rename asset_id to id
ALTER TABLE assets
    RENAME COLUMN asset_id TO id;

-- Step 8: Re-create primary key with new column name
ALTER TABLE assets
    ADD CONSTRAINT pk_assets PRIMARY KEY (id);

-- Step 9: Re-create foreign key constraint with new column reference
ALTER TABLE geometries
    ADD CONSTRAINT FK_GEOMETRIES_ON_ASSET FOREIGN KEY (asset_id) REFERENCES assets (id);