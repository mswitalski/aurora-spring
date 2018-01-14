DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

CREATE TABLE duty (
  id bigint NOT NULL,
  version bigint NOT NULL,
  name character varying(100) NOT NULL
);

ALTER TABLE duty OWNER TO dbadmin;

CREATE SEQUENCE duty_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE duty_id_sequence OWNER TO dbadmin;

CREATE TABLE evaluation (
  id bigint NOT NULL,
  version bigint NOT NULL,
  leaderevaluation character varying(255),
  leaderexplanation character varying(200) NOT NULL,
  selfevaluation character varying(255),
  selfexplanation character varying(200) NOT NULL,
  skill_id bigint NOT NULL,
  user_id bigint NOT NULL
);

ALTER TABLE evaluation OWNER TO dbadmin;

CREATE SEQUENCE evaluation_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE evaluation_id_sequence OWNER TO dbadmin;

CREATE TABLE feedback (
  id bigint NOT NULL,
  version bigint NOT NULL,
  createdatetime timestamp without time zone,
  satisfied boolean NOT NULL,
  studentfeedback character varying(200) NOT NULL,
  mentor_id bigint NOT NULL,
  user_id bigint NOT NULL
);

ALTER TABLE feedback OWNER TO dbadmin;

CREATE SEQUENCE feedback_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE feedback_id_sequence OWNER TO dbadmin;

CREATE TABLE mentor (
  id bigint NOT NULL,
  version bigint NOT NULL,
  active boolean NOT NULL,
  approved boolean NOT NULL,
  evaluation_id bigint NOT NULL
);

ALTER TABLE mentor OWNER TO dbadmin;

CREATE SEQUENCE mentor_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE mentor_id_sequence OWNER TO dbadmin;

CREATE TABLE role (
  name character varying(15) NOT NULL
);

ALTER TABLE role OWNER TO dbadmin;

CREATE TABLE skill (
  id bigint NOT NULL,
  version bigint NOT NULL,
  name character varying(50) NOT NULL
);

ALTER TABLE skill OWNER TO dbadmin;

CREATE SEQUENCE skill_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE skill_id_sequence OWNER TO dbadmin;

CREATE TABLE task (
  id bigint NOT NULL,
  version bigint NOT NULL,
  content character varying(100) NOT NULL,
  deadlinedate date,
  donedate date,
  user_id bigint NOT NULL
);

ALTER TABLE task OWNER TO dbadmin;

CREATE SEQUENCE task_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE task_id_sequence OWNER TO dbadmin;

CREATE TABLE training (
  id bigint NOT NULL,
  version bigint NOT NULL,
  description character varying(500) NOT NULL,
  enddatetime timestamp without time zone NOT NULL,
  internal boolean NOT NULL,
  location character varying(50) NOT NULL,
  name character varying(100) NOT NULL,
  startdatetime timestamp without time zone NOT NULL,
  type character varying(20) NOT NULL
);

ALTER TABLE training OWNER TO dbadmin;

CREATE SEQUENCE training_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE training_id_sequence OWNER TO dbadmin;

CREATE TABLE "user" (
  id bigint NOT NULL,
  version bigint NOT NULL,
  email character varying(40) NOT NULL,
  enabled boolean NOT NULL,
  goals character varying(200) NOT NULL,
  name character varying(20) NOT NULL,
  password character varying(60) NOT NULL,
  "position" character varying(40) NOT NULL,
  surname character varying(30) NOT NULL,
  username character varying(20) NOT NULL
);

ALTER TABLE "user" OWNER TO dbadmin;

CREATE TABLE user_duty (
  user_id bigint NOT NULL,
  duty_id bigint NOT NULL
);

ALTER TABLE user_duty OWNER TO dbadmin;

CREATE SEQUENCE user_id_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE user_id_sequence OWNER TO dbadmin;

CREATE TABLE user_role (
  user_id bigint NOT NULL,
  role_name character varying(15) NOT NULL
);

ALTER TABLE user_role OWNER TO dbadmin;

CREATE TABLE user_training (
  training_id bigint NOT NULL,
  user_id bigint NOT NULL
);

ALTER TABLE user_training OWNER TO dbadmin;

SELECT pg_catalog.setval('duty_id_sequence', 1, false);
SELECT pg_catalog.setval('evaluation_id_sequence', 1, false);
SELECT pg_catalog.setval('feedback_id_sequence', 1, false);
SELECT pg_catalog.setval('mentor_id_sequence', 1, false);
SELECT pg_catalog.setval('skill_id_sequence', 1, false);
SELECT pg_catalog.setval('task_id_sequence', 1, false);
SELECT pg_catalog.setval('training_id_sequence', 1, false);
SELECT pg_catalog.setval('user_id_sequence', 1, false);

ALTER TABLE ONLY duty ADD CONSTRAINT duty_pkey PRIMARY KEY (id);
ALTER TABLE ONLY evaluation ADD CONSTRAINT evaluation_pkey PRIMARY KEY (id);
ALTER TABLE ONLY feedback ADD CONSTRAINT feedback_pkey PRIMARY KEY (id);
ALTER TABLE ONLY mentor ADD CONSTRAINT mentor_pkey PRIMARY KEY (id);
ALTER TABLE ONLY role ADD CONSTRAINT role_pkey PRIMARY KEY (name);
ALTER TABLE ONLY skill ADD CONSTRAINT skill_pkey PRIMARY KEY (id);
ALTER TABLE ONLY task ADD CONSTRAINT task_pkey PRIMARY KEY (id);
ALTER TABLE ONLY training ADD CONSTRAINT training_pkey PRIMARY KEY (id);
ALTER TABLE ONLY duty ADD CONSTRAINT unique_duty_name UNIQUE (name);
ALTER TABLE ONLY evaluation ADD CONSTRAINT unique_evaluation_pair UNIQUE (user_id, skill_id);
ALTER TABLE ONLY mentor ADD CONSTRAINT unique_mentor_evaluation UNIQUE (evaluation_id);
ALTER TABLE ONLY skill ADD CONSTRAINT unique_skill_name UNIQUE (name);
ALTER TABLE ONLY "user" ADD CONSTRAINT unique_user_email UNIQUE (email);
ALTER TABLE ONLY "user" ADD CONSTRAINT unique_user_username UNIQUE (username);
ALTER TABLE ONLY user_duty ADD CONSTRAINT user_duty_pkey PRIMARY KEY (user_id, duty_id);
ALTER TABLE ONLY "user" ADD CONSTRAINT user_pkey PRIMARY KEY (id);
ALTER TABLE ONLY user_role ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_name);
ALTER TABLE ONLY user_training ADD CONSTRAINT user_training_pkey PRIMARY KEY (training_id, user_id);

CREATE INDEX index_task_deadline_date ON task USING btree (deadlinedate);
CREATE INDEX index_task_done_date ON task USING btree (donedate);
CREATE INDEX index_training_location ON training USING btree (location);
CREATE INDEX index_training_name ON training USING btree (name);
CREATE INDEX index_training_type ON training USING btree (type);
CREATE INDEX index_user_enabled ON "user" USING btree (enabled);
CREATE INDEX index_user_name ON "user" USING btree (name);
CREATE INDEX index_user_surname ON "user" USING btree (surname);

ALTER TABLE ONLY user_duty ADD CONSTRAINT user_duty_duty_fkey FOREIGN KEY (duty_id) REFERENCES duty(id);
ALTER TABLE ONLY evaluation ADD CONSTRAINT evaluation_skill_fkey FOREIGN KEY (skill_id) REFERENCES skill(id);
ALTER TABLE ONLY feedback ADD CONSTRAINT feedback_mentor_fkey FOREIGN KEY (mentor_id) REFERENCES mentor(id);
ALTER TABLE ONLY feedback ADD CONSTRAINT feedback_user_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);
ALTER TABLE ONLY evaluation ADD CONSTRAINT evaluation_user_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);
ALTER TABLE ONLY user_training ADD CONSTRAINT user_training_training_fkey FOREIGN KEY (training_id) REFERENCES training(id);
ALTER TABLE ONLY user_role ADD CONSTRAINT user_role_user_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);
ALTER TABLE ONLY mentor ADD CONSTRAINT mentor_evaluation_fkey FOREIGN KEY (evaluation_id) REFERENCES evaluation(id);
ALTER TABLE ONLY user_training ADD CONSTRAINT user_training_user_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);
ALTER TABLE ONLY user_duty ADD CONSTRAINT user_duty_user_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);
ALTER TABLE ONLY user_role ADD CONSTRAINT user_role_role_fkey FOREIGN KEY (role_name) REFERENCES role(name);
ALTER TABLE ONLY task ADD CONSTRAINT task_user_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);

-- Define roles and privileges
-- REVOKE ALL ON ALL TABLES IN SCHEMA public FROM aurmme;
-- REVOKE ALL ON ALL TABLES IN SCHEMA public FROM aurmta;
-- REVOKE ALL ON ALL TABLES IN SCHEMA public FROM aurmtr;
-- REVOKE ALL ON ALL TABLES IN SCHEMA public FROM aurmsk;
-- REVOKE ALL ON ALL TABLES IN SCHEMA public FROM aurmus;
--
-- DROP ROLE aurmme;
-- DROP ROLE aurmta;
-- DROP ROLE aurmtr;
-- DROP ROLE aurmsk;
-- DROP ROLE aurmus;

CREATE ROLE aurmme WITH LOGIN PASSWORD 'password';
CREATE ROLE aurmta WITH LOGIN PASSWORD 'password';
CREATE ROLE aurmtr WITH LOGIN PASSWORD 'password';
CREATE ROLE aurmsk WITH LOGIN PASSWORD 'password';
CREATE ROLE aurmus WITH LOGIN PASSWORD 'password';

GRANT ALL ON ALL TABLES IN SCHEMA public TO dbadmin;

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
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals, version) VALUES (nextval('user_id_sequence'), 'admin', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Jan', 'Kowalski', 'admin@bar.mail.dummy', true, 'Szef', 'Make dreams come true', 0);
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals, version) VALUES (nextval('user_id_sequence'), 'unitleader', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Karolina', 'Nowak', 'unitleader@bar.mail.dummy', true, 'Unit leader', 'Deliver IoT Nova project before deadline', 0);
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals, version) VALUES (nextval('user_id_sequence'), 'employee', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Adam', 'Zaborowski', 'employee@bar.mail.dummy', true, 'Junior Java Programmer', 'Opanować dobrze Jave i zostać wreszcie regularem', 0);
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals, version) VALUES (nextval('user_id_sequence'), 'adminunitleader', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Joanna', 'Zaborowska', 'adminunitleader@bar.mail.dummy', true, 'Senior Android Programmer', 'Get to know the new Android 8.0', 0);
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals, version) VALUES (nextval('user_id_sequence'), 'adminemployee', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Emil', 'Gustaffsonn', 'adminemployee@bar.mail.dummy', true, 'Senior Architect', 'Meet with Bill Gates', 0);
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals, version) VALUES (nextval('user_id_sequence'), 'allroles', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Sigur', 'Ross', 'allroles@bar.mail.dummy', true, 'Senior Designer', 'Go to Paris for Nova conference', 0);

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
INSERT INTO PUBLIC.duty (id, name, version) VALUES (nextval('duty_id_sequence'), 'Ferro Product Owner', 0);
INSERT INTO PUBLIC.duty (id, name, version) VALUES (nextval('duty_id_sequence'), 'Scrum Master', 0);
INSERT INTO PUBLIC.duty (id, name, version) VALUES (nextval('duty_id_sequence'), 'Hibernate Guardian', 0);
INSERT INTO PUBLIC.duty (id, name, version) VALUES (nextval('duty_id_sequence'), 'R200 Expert', 0);
INSERT INTO PUBLIC.duty (id, name, version) VALUES (nextval('duty_id_sequence'), 'Maven Trainer', 0);

-- Create relationships between users and duties
INSERT INTO PUBLIC.user_duty VALUES (2, 1);
INSERT INTO PUBLIC.user_duty VALUES (2, 2);
INSERT INTO PUBLIC.user_duty VALUES (3, 2);
INSERT INTO PUBLIC.user_duty VALUES (4, 3);
INSERT INTO PUBLIC.user_duty VALUES (5, 4);
INSERT INTO PUBLIC.user_duty VALUES (5, 5);

-- Add skills
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'Java 8', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'Design patterns', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'IPv4 protocol', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'Python 3', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'Git', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'English', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'German', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'HTML', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'Maven', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'C++', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'Unity 3D', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'Linux', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'SQL', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'PHP', 0);
INSERT INTO PUBLIC.skill (id, name, version) VALUES (nextval('skill_id_sequence'), 'C#', 0);

-- Create relationships described as evaluations
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'NONE', 'no leader explanation', 'BEGINNER', 'no self explanation', 1, 2, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 2, 2, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 3, 2, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 4, 2, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'INTERMEDIATE', 'no leader explanation', 'BEGINNER', 'no self explanation', 5, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'BEGINNER', 'no self explanation', 6, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 7, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 8, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 9, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 10, 4, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 11, 4, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 12, 4, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 13, 5, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 14, 5, 0);
INSERT INTO PUBLIC.evaluation (id, leaderevaluation, leaderexplanation, selfevaluation, selfexplanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 15, 5, 0);

-- Create relationships described as mentors
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, true, 0, 2);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, false, 0, 3);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, true, 0, 7);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, true, 0, 10);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, true, 0, 14);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), false, true, 0, 15);

-- Create relationships described as feedback
INSERT INTO PUBLIC.feedback (id, version, satisfied, studentfeedback, createdatetime, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '1 day', 1, 1);
INSERT INTO PUBLIC.feedback (id, version, satisfied, studentfeedback, createdatetime, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '5 day', 2, 3);
INSERT INTO PUBLIC.feedback (id, version, satisfied, studentfeedback, createdatetime, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, false, 'some feedback', current_timestamp - interval '10 day', 2, 4);
INSERT INTO PUBLIC.feedback (id, version, satisfied, studentfeedback, createdatetime, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '30 day', 3, 5);
INSERT INTO PUBLIC.feedback (id, version, satisfied, studentfeedback, createdatetime, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '11 day', 4, 2);
INSERT INTO PUBLIC.feedback (id, version, satisfied, studentfeedback, createdatetime, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '42 day', 4, 1);

-- Create trainings
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'Maven training', 'Workshop', 'Conference room 209', true, current_date - interval '1 day' + interval '9 hour', current_date - interval '1 day' + interval '12 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'JPA/Hibernate training', 'Workshop', 'Conference room 403', true, current_date - interval '5 day' + interval '12 hour', current_date - interval '5 day' + interval '16 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'Introduction to collections in C#', 'Presentation', 'Conference room 521', true, current_date - interval '10 day' + interval '8 hour', current_date - interval '10 day' + interval '16 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'Advanced features of GIT', 'Workshop', 'Conference room 217', true, current_date + interval '1 day' + interval '8 hour', current_date + interval '1 day' + interval '14 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'Self improvement for everybody', 'Workshop', 'Kraków, Piaseczna 5', false, current_date + interval '3 day' + interval '14 hour', current_date + interval '3 day' + interval '16 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'How to use new coffee machine', 'Presentation', 'Conference room 311', true, current_date + interval '5 day' + interval '10 hour', current_date + interval '5 day' + interval '12 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'Migration from PHP5 to PHP7', 'Presentation', 'Conference room 203', true, current_date + interval '5 day' + interval '9 hour', current_date + interval '5 day' + interval '12 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'Linux administration', 'Workshop', 'Conference room 403', true, current_date + interval '6 day' + interval '11 hour', current_date + interval '6 day' + interval '15 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'Java9 features', 'Presentation', 'Conference room 209', true, current_date - interval '8 day' + interval '8 hour', current_date - interval '8 day' + interval '10 hour', 'some description');
INSERT INTO PUBLIC.training (id, version, name, type, location, internal, startdatetime, enddatetime, description) VALUES (nextval('training_id_sequence'), 0, 'SQL Injections and how to prevent them', 'Workshop', 'Conference room 311', true, current_date + interval '10 day' + interval '10 hour', current_date + interval '10 day' + interval '13 hour', 'some description');

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
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, null, null, 'Read book about Linux administration');
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, null, current_timestamp - interval '5 day', 'Sign myself for the IIA Conference');
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, current_timestamp - interval '2 day', null, 'Write new blog entry');
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, current_timestamp + interval '8 hour', null, 'Learn basics of Angular 4');
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, current_timestamp + interval '1 day', null, 'Inform my leader about planned holidays');
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, current_timestamp + interval '2 day', null, 'Find some mentor for C#');
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, current_timestamp - interval '5 day', current_timestamp - interval '4 day', 'Finish implementing Juboo library');
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, current_timestamp - interval '8 day', current_timestamp - interval '6 day', 'Test the new Unreal Engine 4');
INSERT INTO PUBLIC.task (id, version, user_id, deadlinedate, donedate, content) VALUES (nextval('task_id_sequence'), 0, 6, current_timestamp - interval '10 day', current_timestamp - interval '2 day', 'Upload my tutorial to YouTube');