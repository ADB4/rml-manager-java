CREATE TABLE geometries
(
    id             UUID                        NOT NULL,
    asset_id       VARCHAR(255)                NOT NULL,
    file_name      VARCHAR(255)                NOT NULL,
    file_type      VARCHAR(255)                NOT NULL,
    file_path      VARCHAR(255)                NOT NULL,
    file_size      BIGINT                      NOT NULL,
    content_type   VARCHAR(255),
    s3_key         VARCHAR(255)                NOT NULL,
    s3_bucket      VARCHAR(255),
    vertex_count   INTEGER,
    polygon_count  INTEGER,
    triangle_count INTEGER,
    has_textures   BOOLEAN,
    has_animation  BOOLEAN,
    has_skeleton   BOOLEAN,
    bounds_min_x   FLOAT,
    bounds_min_y   FLOAT,
    bounds_min_z   FLOAT,
    bounds_max_x   FLOAT,
    bounds_max_y   FLOAT,
    bounds_max_z   FLOAT,
    thumbnail_path VARCHAR(255),
    version        DOUBLE PRECISION,
    is_latest      BOOLEAN,
    checksum       VARCHAR(255),
    upload_source  VARCHAR(255),
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_geometries PRIMARY KEY (id)
);

ALTER TABLE assets
    ADD asset_id VARCHAR(255);

ALTER TABLE assets
    ADD asset_name VARCHAR(255);

ALTER TABLE assets
    ALTER COLUMN asset_name SET NOT NULL;

ALTER TABLE geometries
    ADD CONSTRAINT FK_GEOMETRIES_ON_ASSET FOREIGN KEY (asset_id) REFERENCES assets (asset_id);

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