-- Destroy DB first

DROP TABLE tasting;
DROP TABLE beer;
DROP TABLE brewery;


-- Breweries
CREATE TABLE brewery (id serial PRIMARY KEY,
                      name varchar(255) UNIQUE);

CREATE TABLE beer (id serial PRIMARY KEY,
                   name varchar(255) UNIQUE,
                   brewery_id integer REFERENCES brewery(id));

-- Very minimal table for testing end-to-end functionalities
CREATE TABLE tasting (id serial PRIMARY KEY, 
                      beer_id integer REFERENCES beer(id), 
                      user_rating integer,
                      user_id varchar(255));
