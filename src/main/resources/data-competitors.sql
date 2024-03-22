-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Herren competitors

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 1, 'John1', 'Doe', 'KIOT', 'HERREN', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 2, 'John2', 'Doe', 'KIOT', 'HERREN', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 3, 'John3', 'Doe', 'KIOT', 'HERREN', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 4, 'John4', 'Doe', 'KIOT', 'HERREN', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 5, 'John5', 'Doe', 'KIOT', 'HERREN', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 6, 'John6', 'Doe', 'KIOT', 'HERREN', 'Tristach', '1994-02-17', false);

-- DAMEN competitors

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 7, 'Dane', 'Doe', 'KIOT', 'DAMEN', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 8, 'Dane1', 'Doe', 'KIOT', 'DAMEN', 'Tristach', '1994-02-17', false);