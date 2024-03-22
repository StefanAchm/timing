-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Male competitors

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 1, 'John1', 'Doe', 'KIOT', 'Male', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 2, 'John2', 'Doe', 'KIOT', 'Male', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 3, 'John3', 'Doe', 'KIOT', 'Male', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 4, 'John4', 'Doe', 'KIOT', 'Male', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 5, 'John5', 'Doe', 'KIOT', 'Male', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 6, 'John6', 'Doe', 'KIOT', 'Male', 'Tristach', '1994-02-17', false);

-- Female competitors

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 7, 'Dane', 'Doe', 'KIOT', 'Female', 'Tristach', '1994-02-17', false);

INSERT INTO competitors (id, start_number, first_name, last_name, club, gender, city, date_of_birth, deleted)
VALUES (uuid_generate_v4(), 8, 'Dane1', 'Doe', 'KIOT', 'Female', 'Tristach', '1994-02-17', false);

-- Rounds

INSERT INTO rounds (id, gender, max_holds, round_number)
VALUES (uuid_generate_v4(), 'Male', 14, 1)