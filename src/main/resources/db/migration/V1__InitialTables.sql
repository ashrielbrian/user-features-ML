CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email varchar
);

CREATE TABLE features (
  id SERIAL PRIMARY KEY,
  name varchar
);

CREATE TABLE user_feature (
  user_id int,
  feature_id int,
  enabled boolean
);

CREATE INDEX ON users (email);

ALTER TABLE user_feature ADD FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_feature ADD FOREIGN KEY (feature_id) REFERENCES features (id);

ALTER TABLE user_feature ADD UNIQUE(user_id, feature_id);