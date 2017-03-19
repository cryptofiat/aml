SET SCHEMA 'aml';

CREATE TABLE sanction_entry (
  id SERIAL PRIMARY KEY,
  source_id TEXT NOT NULL,
  source TEXT NOT NULL,
  type TEXT NOT NULL
);

CREATE INDEX idx_sanction_entry_source_id ON sanction_entry(source_id);
CREATE INDEX idx_sanction_entry_source ON sanction_entry(source);
CREATE INDEX idx_sanction_entry_type ON sanction_entry(type);

CREATE TABLE sanction_entry_full_name (
  sanction_entry_id BIGINT NOT NULL,
  full_name TEXT NOT NULL
);

CREATE TABLE sanction_entry_year_of_birth(
  sanction_entry_id BIGINT NOT NULL,
  year_of_birth INT NOT NULL
);

CREATE TABLE sanction_entry_date_of_birth(
  sanction_entry_id BIGINT NOT NULL,
  date_of_birth DATE NOT NULL
);