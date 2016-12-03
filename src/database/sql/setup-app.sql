CREATE SCHEMA app;
-- Table to store 'stay logged in' records
CREATE TABLE app.logins (
  token INTEGER NOT NULL PRIMARY KEY, -- UUID Column
  user_id INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES MODEL.USERS (user_id)
)