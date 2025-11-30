-- Create assets table
CREATE TABLE IF NOT EXISTS assets (
                                      item_id VARCHAR(255) PRIMARY KEY,
                                      item_name VARCHAR(255) NOT NULL,
                                      category VARCHAR(255) NOT NULL,
                                      subcategory VARCHAR(255) NOT NULL,
                                      description TEXT NOT NULL,
                                      notes TEXT,
                                      shader VARCHAR(255),
                                      material VARCHAR(255) NOT NULL,
                                      animation BOOLEAN NOT NULL,
                                      lods VARCHAR(255) NOT NULL,
                                      created_at TIMESTAMP,
                                      updated_at TIMESTAMP
);