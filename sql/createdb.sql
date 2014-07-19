-- Destroy DB first

DROP TABLE brewery;
DROP TABLE tasting;

-- Breweries
CREATE TABLE brewery (id serial PRIMARY KEY,
                      name varchar(255) UNIQUE);

-- Very minimal table for testing end-to-end functionalities
CREATE TABLE tasting (id serial PRIMARY KEY, 
                      name varchar(255) not null, 
                      brewery_id integer REFERENCES brewery(id));
