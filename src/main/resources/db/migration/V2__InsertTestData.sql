INSERT INTO users(email) VALUES
('brian@tang.com'), ('test_user@gmail.com'), ('test@user.com');

INSERT INTO features(name) VALUES
('tipping'), ('cryptoTransfer'), ('trade');

INSERT INTO user_feature(user_id, feature_id, enabled) VALUES
(1, 1, true),
(1, 2, true),
(1, 3, true),
(2, 1, false),
(2, 2, true),
(2, 3, false),
(3, 1, true),
(3, 2, false),
(3, 3, true);