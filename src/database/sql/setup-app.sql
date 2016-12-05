CREATE SCHEMA app;
-- Table to store 'stay logged in' records
CREATE TABLE app.logins (
  token CHAR(36) NOT NULL PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES MODEL.USERS (user_id)
)