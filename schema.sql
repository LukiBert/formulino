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

CREATE TABLE drivers (
                        driver_id TEXT PRIMARY KEY,
                        url TEXT,
                        given_name TEXT,
                        family_name TEXT,
                        date_of_birth TEXT,
                        nationality TEXT,
                        permanent_number TEXT,
                        code TEXT
);

CREATE TABLE  laps (
                        race_id TEXT,
                        lap_number INTEGER,
                        driver_id TEXT,
                        position INTEGER,
                        lap_time TEXT,
                        lap_time_ms INTEGER,
                        PRIMARY KEY (race_id, lap_number, driver_id),
                        FOREIGN KEY (driver_id) REFERENCES drivers(driver_id)
);

CREATE TABLE pitstops (
                       race_id TEXT,
                       driver_id TEXT,
                       lap_number INTEGER,
                       stop_number INTEGER,
                       duration TEXT,
                       stop_time TEXT,
                       PRIMARY KEY (race_id, driver_id, lap_number, stop_number),
                       FOREIGN KEY (driver_id) REFERENCES drivers(driver_id)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_laps_race_lap ON laps(race_id, lap_number);
CREATE INDEX IF NOT EXISTS idx_pitstops_race_lap ON pitstops(race_id, lap_number);
CREATE INDEX idx_laps_race_lap ON laps(race_id, lap_number);

CREATE INDEX idx_races_season ON races(season);
CREATE INDEX idx_circuits_location ON circuits(locality, country);
CREATE INDEX idx_races_circuit ON races(circuit_id);