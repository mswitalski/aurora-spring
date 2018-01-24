DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;

-- Sekwencje
CREATE SEQUENCE duty_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE SEQUENCE evaluation_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE SEQUENCE feedback_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE SEQUENCE mentor_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE SEQUENCE skill_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE SEQUENCE task_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE SEQUENCE training_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE SEQUENCE user_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


SELECT pg_catalog.setval('duty_id_sequence', 1, FALSE);
SELECT pg_catalog.setval('evaluation_id_sequence', 1, FALSE);
SELECT pg_catalog.setval('feedback_id_sequence', 1, FALSE);
SELECT pg_catalog.setval('mentor_id_sequence', 1, FALSE);
SELECT pg_catalog.setval('skill_id_sequence', 1, FALSE);
SELECT pg_catalog.setval('task_id_sequence', 1, FALSE);
SELECT pg_catalog.setval('training_id_sequence', 1, FALSE);
SELECT pg_catalog.setval('user_id_sequence', 1, FALSE);

-- Tabele
CREATE TABLE duty (
  id      BIGINT DEFAULT nextval('duty_id_sequence') NOT NULL,
  version BIGINT DEFAULT 0                           NOT NULL,
  name    CHARACTER VARYING(100)                     NOT NULL
);


CREATE TABLE evaluation (
  id                BIGINT DEFAULT nextval('evaluation_id_sequence') NOT NULL,
  version           BIGINT DEFAULT 0                                 NOT NULL,
  leaderevaluation  CHARACTER VARYING(20),
  leaderexplanation CHARACTER VARYING(200)                           NOT NULL,
  selfevaluation    CHARACTER VARYING(20),
  selfexplanation   CHARACTER VARYING(200)                           NOT NULL,
  skill_id          BIGINT                                           NOT NULL,
  user_id           BIGINT                                           NOT NULL
);


CREATE TABLE feedback (
  id              BIGINT DEFAULT nextval('feedback_id_sequence') NOT NULL,
  version         BIGINT DEFAULT 0                               NOT NULL,
  createdatetime  TIMESTAMP WITHOUT TIME ZONE,
  satisfied       BOOLEAN                                        NOT NULL,
  studentfeedback CHARACTER VARYING(200)                         NOT NULL,
  mentor_id       BIGINT                                         NOT NULL,
  user_id         BIGINT                                         NOT NULL
);


CREATE TABLE mentor (
  id            BIGINT DEFAULT nextval('mentor_id_sequence') NOT NULL,
  version       BIGINT DEFAULT 0                             NOT NULL,
  active        BOOLEAN                                      NOT NULL,
  approved      BOOLEAN                                      NOT NULL,
  evaluation_id BIGINT                                       NOT NULL
);


CREATE TABLE role (
  name CHARACTER VARYING(15) NOT NULL
);


CREATE TABLE skill (
  id      BIGINT DEFAULT nextval('skill_id_sequence') NOT NULL,
  version BIGINT DEFAULT 0                            NOT NULL,
  name    CHARACTER VARYING(50)                       NOT NULL
);


CREATE TABLE task (
  id           BIGINT DEFAULT nextval('task_id_sequence') NOT NULL,
  version      BIGINT DEFAULT 0                           NOT NULL,
  content      CHARACTER VARYING(100)                     NOT NULL,
  deadlinedate DATE,
  donedate     DATE,
  user_id      BIGINT                                     NOT NULL
);


CREATE TABLE training (
  id                BIGINT DEFAULT nextval('training_id_sequence') NOT NULL,
  version           BIGINT DEFAULT 0                               NOT NULL,
  description       CHARACTER VARYING(500)                         NOT NULL,
  enddatetime       TIMESTAMP WITHOUT TIME ZONE                    NOT NULL,
  internal          BOOLEAN                                        NOT NULL,
  participantslimit INTEGER                                        NOT NULL,
  location          CHARACTER VARYING(50)                          NOT NULL,
  name              CHARACTER VARYING(100)                         NOT NULL,
  startdatetime     TIMESTAMP WITHOUT TIME ZONE                    NOT NULL,
  type              CHARACTER VARYING(20)                          NOT NULL
);


CREATE TABLE "user" (
  id         BIGINT DEFAULT nextval('user_id_sequence') NOT NULL,
  version    BIGINT DEFAULT 0                           NOT NULL,
  email      CHARACTER VARYING(40)                      NOT NULL,
  enabled    BOOLEAN                                    NOT NULL,
  goals      CHARACTER VARYING(200)                     NOT NULL,
  name       CHARACTER VARYING(20)                      NOT NULL,
  password   CHARACTER VARYING(60)                      NOT NULL,
  "position" CHARACTER VARYING(40)                      NOT NULL,
  surname    CHARACTER VARYING(30)                      NOT NULL,
  username   CHARACTER VARYING(20)                      NOT NULL
);

CREATE TABLE user_duty (
  user_id BIGINT NOT NULL,
  duty_id BIGINT NOT NULL
);


CREATE TABLE user_role (
  user_id   BIGINT                NOT NULL,
  role_name CHARACTER VARYING(15) NOT NULL
);


CREATE TABLE user_training (
  training_id BIGINT NOT NULL,
  user_id     BIGINT NOT NULL
);


ALTER TABLE ONLY duty
  ADD CONSTRAINT duty_pkey PRIMARY KEY (id);
ALTER TABLE ONLY evaluation
  ADD CONSTRAINT evaluation_pkey PRIMARY KEY (id);
ALTER TABLE ONLY feedback
  ADD CONSTRAINT feedback_pkey PRIMARY KEY (id);
ALTER TABLE ONLY mentor
  ADD CONSTRAINT mentor_pkey PRIMARY KEY (id);
ALTER TABLE ONLY role
  ADD CONSTRAINT role_pkey PRIMARY KEY (name);
ALTER TABLE ONLY skill
  ADD CONSTRAINT skill_pkey PRIMARY KEY (id);
ALTER TABLE ONLY task
  ADD CONSTRAINT task_pkey PRIMARY KEY (id);
ALTER TABLE ONLY training
  ADD CONSTRAINT training_pkey PRIMARY KEY (id);
ALTER TABLE ONLY duty
  ADD CONSTRAINT unique_duty_name UNIQUE (name);
ALTER TABLE ONLY evaluation
  ADD CONSTRAINT unique_evaluation_pair UNIQUE (user_id, skill_id);
ALTER TABLE ONLY mentor
  ADD CONSTRAINT unique_mentor_evaluation UNIQUE (evaluation_id);
ALTER TABLE ONLY skill
  ADD CONSTRAINT unique_skill_name UNIQUE (name);
ALTER TABLE ONLY "user"
  ADD CONSTRAINT unique_user_email UNIQUE (email);
ALTER TABLE ONLY "user"
  ADD CONSTRAINT unique_user_username UNIQUE (username);
ALTER TABLE ONLY user_duty
  ADD CONSTRAINT user_duty_pkey PRIMARY KEY (user_id, duty_id);
ALTER TABLE ONLY "user"
  ADD CONSTRAINT user_pkey PRIMARY KEY (id);
ALTER TABLE ONLY user_role
  ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_name);
ALTER TABLE ONLY user_training
  ADD CONSTRAINT user_training_pkey PRIMARY KEY (training_id, user_id);

CREATE INDEX index_task_deadline_date
  ON task USING BTREE (deadlinedate);
CREATE INDEX index_task_done_date
  ON task USING BTREE (donedate);
CREATE INDEX index_training_location
  ON training USING BTREE (location);
CREATE INDEX index_training_name
  ON training USING BTREE (name);
CREATE INDEX index_training_type
  ON training USING BTREE (type);
CREATE INDEX index_user_enabled
  ON "user" USING BTREE (enabled);
CREATE INDEX index_user_name
  ON "user" USING BTREE (name);
CREATE INDEX index_user_surname
  ON "user" USING BTREE (surname);

ALTER TABLE ONLY user_duty
  ADD CONSTRAINT user_duty_duty_fkey FOREIGN KEY (duty_id) REFERENCES duty (id);
ALTER TABLE ONLY evaluation
  ADD CONSTRAINT evaluation_skill_fkey FOREIGN KEY (skill_id) REFERENCES skill (id);
ALTER TABLE ONLY feedback
  ADD CONSTRAINT feedback_mentor_fkey FOREIGN KEY (mentor_id) REFERENCES mentor (id);
ALTER TABLE ONLY feedback
  ADD CONSTRAINT feedback_user_fkey FOREIGN KEY (user_id) REFERENCES "user" (id);
ALTER TABLE ONLY evaluation
  ADD CONSTRAINT evaluation_user_fkey FOREIGN KEY (user_id) REFERENCES "user" (id);
ALTER TABLE ONLY user_training
  ADD CONSTRAINT user_training_training_fkey FOREIGN KEY (training_id) REFERENCES training (id);
ALTER TABLE ONLY user_role
  ADD CONSTRAINT user_role_user_fkey FOREIGN KEY (user_id) REFERENCES "user" (id);
ALTER TABLE ONLY mentor
  ADD CONSTRAINT mentor_evaluation_fkey FOREIGN KEY (evaluation_id) REFERENCES evaluation (id);
ALTER TABLE ONLY user_training
  ADD CONSTRAINT user_training_user_fkey FOREIGN KEY (user_id) REFERENCES "user" (id);
ALTER TABLE ONLY user_duty
  ADD CONSTRAINT user_duty_user_fkey FOREIGN KEY (user_id) REFERENCES "user" (id);
ALTER TABLE ONLY user_role
  ADD CONSTRAINT user_role_role_fkey FOREIGN KEY (role_name) REFERENCES role (name);
ALTER TABLE ONLY task
  ADD CONSTRAINT task_user_fkey FOREIGN KEY (user_id) REFERENCES "user" (id);

-- Define roles and privileges
DROP ROLE IF EXISTS aurmme;
DROP ROLE IF EXISTS aurmta;
DROP ROLE IF EXISTS aurmtr;
DROP ROLE IF EXISTS aurmsk;
DROP ROLE IF EXISTS aurmus;
CREATE ROLE aurmme WITH LOGIN PASSWORD 'jz43hpz@m&#q3N^?';
CREATE ROLE aurmta WITH LOGIN PASSWORD 'ezf*Er3#6nt-2P@Q';
CREATE ROLE aurmtr WITH LOGIN PASSWORD 'a5j*@_eQN5xYawr6';
CREATE ROLE aurmsk WITH LOGIN PASSWORD '7xfvuTV?%vnzz$S5';
CREATE ROLE aurmus WITH LOGIN PASSWORD 'BF!+r8+kMaY2cR3y';

GRANT SELECT ON duty TO aurmme;
GRANT SELECT ON evaluation TO aurmme;
GRANT SELECT, INSERT, DELETE ON feedback TO aurmme;
GRANT SELECT, INSERT, UPDATE, DELETE ON mentor TO aurmme;
GRANT SELECT ON "role" TO aurmme;
GRANT SELECT ON skill TO aurmme;
GRANT SELECT ON training TO aurmme;
GRANT SELECT ON "user" TO aurmme;
GRANT SELECT ON user_duty TO aurmme;
GRANT SELECT ON user_role TO aurmme;
GRANT SELECT ON user_training TO aurmme;

GRANT SELECT ON duty TO aurmta;
GRANT SELECT ON evaluation TO aurmta;
GRANT SELECT ON feedback TO aurmta;
GRANT SELECT ON mentor TO aurmta;
GRANT SELECT ON "role" TO aurmta;
GRANT SELECT ON skill TO aurmta;
GRANT SELECT, INSERT, UPDATE, DELETE ON task TO aurmta;
GRANT SELECT ON training TO aurmta;
GRANT SELECT ON "user" TO aurmta;
GRANT SELECT ON user_duty TO aurmta;
GRANT SELECT ON user_role TO aurmta;
GRANT SELECT ON user_training TO aurmta;

GRANT SELECT ON duty TO aurmtr;
GRANT SELECT ON evaluation TO aurmtr;
GRANT SELECT ON feedback TO aurmtr;
GRANT SELECT ON mentor TO aurmtr;
GRANT SELECT ON "role" TO aurmtr;
GRANT SELECT ON skill TO aurmtr;
GRANT SELECT, INSERT, UPDATE, DELETE ON training TO aurmtr;
GRANT SELECT ON "user" TO aurmtr;
GRANT SELECT ON user_duty TO aurmtr;
GRANT SELECT ON user_role TO aurmtr;
GRANT SELECT, INSERT, DELETE ON user_training TO aurmtr;

GRANT SELECT ON duty TO aurmsk;
GRANT SELECT, INSERT, UPDATE, DELETE ON evaluation TO aurmsk;
GRANT SELECT, DELETE ON feedback TO aurmsk;
GRANT SELECT, DELETE ON mentor TO aurmsk;
GRANT SELECT ON "role" TO aurmsk;
GRANT SELECT, INSERT, UPDATE, DELETE ON skill TO aurmsk;
GRANT SELECT ON training TO aurmsk;
GRANT SELECT ON "user" TO aurmsk;
GRANT SELECT ON user_duty TO aurmsk;
GRANT SELECT ON user_role TO aurmsk;
GRANT SELECT ON user_training TO aurmsk;

GRANT SELECT, INSERT, UPDATE, DELETE ON duty TO aurmus;
GRANT SELECT, DELETE ON evaluation TO aurmus;
GRANT SELECT, DELETE ON feedback TO aurmus;
GRANT SELECT, DELETE ON mentor TO aurmus;
GRANT SELECT ON "role" TO aurmus;
GRANT SELECT ON skill TO aurmus;
GRANT DELETE ON task TO aurmus;
GRANT SELECT, UPDATE ON training TO aurmus;
GRANT SELECT, INSERT, UPDATE, DELETE ON "user" TO aurmus;
GRANT SELECT, INSERT, DELETE ON user_duty TO aurmus;
GRANT SELECT, INSERT, DELETE ON user_role TO aurmus;
GRANT SELECT, DELETE ON user_training TO aurmus;

GRANT USAGE ON SEQUENCE duty_id_sequence TO aurmus;
GRANT USAGE ON SEQUENCE evaluation_id_sequence TO aurmsk;
GRANT USAGE ON SEQUENCE feedback_id_sequence TO aurmme;
GRANT USAGE ON SEQUENCE mentor_id_sequence TO aurmme;
GRANT USAGE ON SEQUENCE skill_id_sequence TO aurmsk;
GRANT USAGE ON SEQUENCE task_id_sequence TO aurmta;
GRANT USAGE ON SEQUENCE training_id_sequence TO aurmtr;
GRANT USAGE ON SEQUENCE user_id_sequence TO aurmus;

-- Define basic roles
INSERT INTO PUBLIC.role VALUES ('ADMIN');
INSERT INTO PUBLIC.role VALUES ('UNIT_LEADER');
INSERT INTO PUBLIC.role VALUES ('EMPLOYEE');

-- Add users
INSERT INTO PUBLIC."user" (username, password, name, surname, email, enabled, position, goals) VALUES
  ('admin', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Jan', 'Kowalski', 'admin@bar.mail.dummy',
   TRUE, 'Szef', 'Make dreams come true');
INSERT INTO PUBLIC."user" (username, password, name, surname, email, enabled, position, goals) VALUES
  ('unitleader', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Karolina', 'Nowak',
   'unitleader@bar.mail.dummy', TRUE, 'Unit leader', 'Deliver IoT Nova project before deadline');
INSERT INTO PUBLIC."user" (username, password, name, surname, email, enabled, position, goals) VALUES
  ('employee', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Adam', 'Zaborowski',
   'employee@bar.mail.dummy', TRUE, 'Junior Java Programmer', 'Opanować dobrze Jave i zostać wreszcie regularem');
INSERT INTO PUBLIC."user" (username, password, name, surname, email, enabled, position, goals) VALUES
  ('adminunitleader', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Joanna', 'Zaborowska',
   'adminunitleader@bar.mail.dummy', TRUE, 'Senior Android Programmer', 'Get to know the new Android 8.0');
INSERT INTO PUBLIC."user" (username, password, name, surname, email, enabled, position, goals) VALUES
  ('adminemployee', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Emil', 'Gustaffsonn',
   'adminemployee@bar.mail.dummy', TRUE, 'Senior Architect', 'Meet with Bill Gates');
INSERT INTO PUBLIC."user" (username, password, name, surname, email, enabled, position, goals) VALUES
  ('allroles', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Sigur', 'Ross',
   'allroles@bar.mail.dummy', TRUE, 'Senior Designer', 'Go to Paris for Nova conference');

-- Create relationships between users and roles
INSERT INTO PUBLIC.user_role VALUES (1, 'ADMIN');
INSERT INTO PUBLIC.user_role VALUES (2, 'UNIT_LEADER');
INSERT INTO PUBLIC.user_role VALUES (3, 'EMPLOYEE');
INSERT INTO PUBLIC.user_role VALUES (4, 'ADMIN');
INSERT INTO PUBLIC.user_role VALUES (4, 'UNIT_LEADER');
INSERT INTO PUBLIC.user_role VALUES (5, 'ADMIN');
INSERT INTO PUBLIC.user_role VALUES (5, 'EMPLOYEE');
INSERT INTO PUBLIC.user_role VALUES (6, 'ADMIN');
INSERT INTO PUBLIC.user_role VALUES (6, 'UNIT_LEADER');
INSERT INTO PUBLIC.user_role VALUES (6, 'EMPLOYEE');

-- Add duties
INSERT INTO PUBLIC.duty (name) VALUES ('Ferro Product Owner');
INSERT INTO PUBLIC.duty (name) VALUES ('Scrum Master');
INSERT INTO PUBLIC.duty (name) VALUES ('Hibernate Guardian');
INSERT INTO PUBLIC.duty (name) VALUES ('R200 Expert');
INSERT INTO PUBLIC.duty (name) VALUES ('Maven Trainer');

-- Create relationships between users and duties
INSERT INTO PUBLIC.user_duty VALUES (2, 1);
INSERT INTO PUBLIC.user_duty VALUES (2, 2);
INSERT INTO PUBLIC.user_duty VALUES (3, 2);
INSERT INTO PUBLIC.user_duty VALUES (4, 3);
INSERT INTO PUBLIC.user_duty VALUES (5, 4);
INSERT INTO PUBLIC.user_duty VALUES (5, 5);

-- Add skills
INSERT INTO PUBLIC.skill (name) VALUES ('Java 8');
INSERT INTO PUBLIC.skill (name) VALUES ('Design patterns');
INSERT INTO PUBLIC.skill (name) VALUES ('IPv4 protocol');
INSERT INTO PUBLIC.skill (name) VALUES ('Python 3');
INSERT INTO PUBLIC.skill (name) VALUES ('Git');
INSERT INTO PUBLIC.skill (name) VALUES ('English');
INSERT INTO PUBLIC.skill (name) VALUES ('German');
INSERT INTO PUBLIC.skill (name) VALUES ('HTML');
INSERT INTO PUBLIC.skill (name) VALUES ('Maven');
INSERT INTO PUBLIC.skill (name) VALUES ('C++');
INSERT INTO PUBLIC.skill (name) VALUES ('Unity 3D');
INSERT INTO PUBLIC.skill (name) VALUES ('Linux');
INSERT INTO PUBLIC.skill (name) VALUES ('SQL');
INSERT INTO PUBLIC.skill (name) VALUES ('PHP');
INSERT INTO PUBLIC.skill (name) VALUES ('C#');

-- Create relationships described as evaluations
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('NONE', 'no leader explanation', 'BEGINNER', 'no self explanation', 1, 2);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 2, 2);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 3, 2);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 4, 2);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('INTERMEDIATE', 'no leader explanation', 'BEGINNER', 'no self explanation', 5, 3);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('EXPERT', 'no leader explanation', 'BEGINNER', 'no self explanation', 6, 3);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 7, 3);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 8, 3);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 9, 3);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 10, 4);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 11, 4);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('BEGINNER', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 12, 4);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 13, 5);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 14, 5);
INSERT INTO PUBLIC.evaluation (leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id)
VALUES ('EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 15, 5);

-- Create relationships described as mentors
INSERT INTO PUBLIC.mentor (approved, active, evaluation_id) VALUES (TRUE, TRUE, 2);
INSERT INTO PUBLIC.mentor (approved, active, evaluation_id) VALUES (TRUE, FALSE, 3);
INSERT INTO PUBLIC.mentor (approved, active, evaluation_id) VALUES (TRUE, TRUE, 7);
INSERT INTO PUBLIC.mentor (approved, active, evaluation_id) VALUES (TRUE, TRUE, 10);
INSERT INTO PUBLIC.mentor (approved, active, evaluation_id) VALUES (TRUE, TRUE, 14);
INSERT INTO PUBLIC.mentor (approved, active, evaluation_id) VALUES (FALSE, TRUE, 15);

-- Create relationships described as feedback
INSERT INTO PUBLIC.feedback (satisfied, studentfeedback, createdatetime, mentor_id, user_id)
VALUES (TRUE, 'some feedback', current_timestamp - INTERVAL '1 day', 1, 1);
INSERT INTO PUBLIC.feedback (satisfied, studentfeedback, createdatetime, mentor_id, user_id)
VALUES (TRUE, 'some feedback', current_timestamp - INTERVAL '5 day', 2, 3);
INSERT INTO PUBLIC.feedback (satisfied, studentfeedback, createdatetime, mentor_id, user_id)
VALUES (FALSE, 'some feedback', current_timestamp - INTERVAL '10 day', 2, 4);
INSERT INTO PUBLIC.feedback (satisfied, studentfeedback, createdatetime, mentor_id, user_id)
VALUES (TRUE, 'some feedback', current_timestamp - INTERVAL '30 day', 3, 5);
INSERT INTO PUBLIC.feedback (satisfied, studentfeedback, createdatetime, mentor_id, user_id)
VALUES (TRUE, 'some feedback', current_timestamp - INTERVAL '11 day', 4, 2);
INSERT INTO PUBLIC.feedback (satisfied, studentfeedback, createdatetime, mentor_id, user_id)
VALUES (TRUE, 'some feedback', current_timestamp - INTERVAL '42 day', 4, 1);

-- Create trainings
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('Maven training', 'Workshop', 'Conference room 209', TRUE, current_date - INTERVAL '1 day' + INTERVAL '9 hour',
   current_date - INTERVAL '1 day' + INTERVAL '12 hour', 'some description', 10);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('JPA/Hibernate training', 'Workshop', 'Conference room 403', TRUE,
   current_date - INTERVAL '5 day' + INTERVAL '12 hour', current_date - INTERVAL '5 day' + INTERVAL '16 hour',
   'some description', 15);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('Introduction to collections in C#', 'Presentation', 'Conference room 521', TRUE,
   current_date - INTERVAL '10 day' + INTERVAL '8 hour', current_date - INTERVAL '10 day' + INTERVAL '16 hour',
   'some description', 10);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('Advanced features of GIT', 'Workshop', 'Conference room 217', TRUE,
   current_date + INTERVAL '1 day' + INTERVAL '8 hour', current_date + INTERVAL '1 day' + INTERVAL '14 hour',
   'some description', 5);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('Self improvement for everybody', 'Workshop', 'Kraków, Piaseczna 5', FALSE,
   current_date + INTERVAL '3 day' + INTERVAL '14 hour', current_date + INTERVAL '3 day' + INTERVAL '16 hour',
   'some description', 50);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('How to use new coffee machine', 'Presentation', 'Conference room 311', TRUE,
   current_date + INTERVAL '5 day' + INTERVAL '10 hour', current_date + INTERVAL '5 day' + INTERVAL '12 hour',
   'some description', 25);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('Migration from PHP5 to PHP7', 'Presentation', 'Conference room 203', TRUE,
   current_date + INTERVAL '5 day' + INTERVAL '9 hour', current_date + INTERVAL '5 day' + INTERVAL '12 hour',
   'some description', 5);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('Linux administration', 'Workshop', 'Conference room 403', TRUE,
   current_date + INTERVAL '6 day' + INTERVAL '11 hour', current_date + INTERVAL '6 day' + INTERVAL '15 hour',
   'some description', 8);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('Java9 features', 'Presentation', 'Conference room 209', TRUE, current_date - INTERVAL '8 day' + INTERVAL '8 hour',
   current_date - INTERVAL '8 day' + INTERVAL '10 hour', 'some description', 20);
INSERT INTO PUBLIC.training (name, type, location, internal, startdatetime, enddatetime, description, participantslimit) VALUES
  ('SQL Injections and how to prevent them', 'Workshop', 'Conference room 311', TRUE,
   current_date + INTERVAL '10 day' + INTERVAL '10 hour', current_date + INTERVAL '10 day' + INTERVAL '13 hour',
   'some description', 5);

-- Create relationships between trainings and users
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (1, 1);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (1, 3);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (1, 5);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (2, 2);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (2, 4);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (2, 6);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (3, 3);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (3, 5);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (3, 7);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (4, 4);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (4, 6);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (4, 8);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (5, 5);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (5, 7);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (5, 9);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (6, 6);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (6, 8);
INSERT INTO PUBLIC.user_training (user_id, training_id) VALUES (6, 10);

-- Create tasks
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content)
VALUES (6, NULL, NULL, 'Read book about Linux administration');
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content)
VALUES (6, NULL, current_timestamp - INTERVAL '5 day', 'Sign myself for the IIA Conference');
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content)
VALUES (6, current_timestamp - INTERVAL '2 day', NULL, 'Write new blog entry');
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content)
VALUES (6, current_timestamp + INTERVAL '8 hour', NULL, 'Learn basics of Angular 4');
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content)
VALUES (6, current_timestamp + INTERVAL '1 day', NULL, 'Inform my leader about planned holidays');
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content)
VALUES (6, current_timestamp + INTERVAL '2 day', NULL, 'Find some mentor for C#');
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content) VALUES
  (6, current_timestamp - INTERVAL '5 day', current_timestamp - INTERVAL '4 day', 'Finish implementing Juboo library');
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content)
VALUES (6, current_timestamp - INTERVAL '8 day', current_timestamp - INTERVAL '6 day', 'Test the new Unreal Engine 4');
INSERT INTO PUBLIC.task (user_id, deadlinedate, donedate, content) VALUES
  (6, current_timestamp - INTERVAL '10 day', current_timestamp - INTERVAL '2 day', 'Upload my tutorial to YouTube');