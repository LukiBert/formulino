CREATE TABLE locations (
                           lat TEXT,
                           longitude TEXT,
                           locality TEXT NOT NULL,
                           country TEXT NOT NULL,
                           PRIMARY KEY (locality, country)
);

-- Create circuits table
CREATE TABLE circuits (
                          circuit_id TEXT PRIMARY KEY,
                          url TEXT,
                          circuit_name TEXT,
                          locality TEXT,
                          country TEXT,
                          FOREIGN KEY (locality, country) REFERENCES locations(locality, country)
);

-- Create races table
CREATE TABLE races (
                       season TEXT NOT NULL,
                       round TEXT NOT NULL,
                       url TEXT,
                       race_name TEXT,
                       date TEXT,
                       time TEXT,
                       circuit_id TEXT,
                       PRIMARY KEY (season, round),
                       FOREIGN KEY (circuit_id) REFERENCES circuits(circuit_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_races_season ON races(season);
CREATE INDEX idx_circuits_location ON circuits(locality, country);
CREATE INDEX idx_races_circuit ON races(circuit_id);