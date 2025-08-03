-- First, remove the data from the users table
DELETE FROM users;

-- For judges (password should be hashed with BCrypt)
INSERT INTO users (id, username, password, role) VALUES (gen_random_uuid(), 'simon', '$2a$10$RiyRzVRA2HAX4QJM7ETGqe4QuvXHC2JUSR3gC8l7Tftv4m9Jy0Ava', 'JUDGE');
INSERT INTO users (id, username, password, role) VALUES (gen_random_uuid(), 'markus', '$2a$10$RiyRzVRA2HAX4QJM7ETGqe4QuvXHC2JUSR3gC8l7Tftv4m9Jy0Ava', 'JUDGE');
INSERT INTO users (id, username, password, role) VALUES (gen_random_uuid(), 'stefan', '$2a$10$g3qOGdKzrCRV8I2bDanyGepc7txLSMbLhLhvkoXaAWejooW0ZztTq', 'ADMIN');