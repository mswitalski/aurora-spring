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
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'NONE', 'no leader explanation', 'BEGINNER', 'no self explanation', 1, 2, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 2, 2, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 3, 2, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 4, 2, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'INTERMEDIATE', 'no leader explanation', 'BEGINNER', 'no self explanation', 5, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'BEGINNER', 'no self explanation', 6, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 7, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 8, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 9, 3, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 10, 4, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'BEGINNER', 'no self explanation', 11, 4, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'BEGINNER', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 12, 4, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'INTERMEDIATE', 'no leader explanation', 'INTERMEDIATE', 'no self explanation', 13, 5, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 14, 5, 0);
INSERT INTO PUBLIC.evaluation (id, leader_evaluation, leader_explanation, self_evaluation, self_explanation, skill_id, user_id, version) VALUES (nextval('evaluation_id_sequence'), 'EXPERT', 'no leader explanation', 'EXPERT', 'no self explanation', 15, 5, 0);

-- Create relationships described as mentors
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, true, 0, 2);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, false, 0, 3);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, true, 0, 7);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, true, 0, 10);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), true, true, 0, 14);
INSERT INTO PUBLIC.mentor (id, approved, active, version, evaluation_id) VALUES (nextval('mentor_id_sequence'), false, true, 0, 15);

-- Create relationships described as feedbacks
INSERT INTO PUBLIC.feedback (id, version, satisfied, student_feedback, create_date_time, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '1 day', 1, 1);
INSERT INTO PUBLIC.feedback (id, version, satisfied, student_feedback, create_date_time, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '5 day', 2, 3);
INSERT INTO PUBLIC.feedback (id, version, satisfied, student_feedback, create_date_time, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, false, 'some feedback', current_timestamp - interval '10 day', 2, 4);
INSERT INTO PUBLIC.feedback (id, version, satisfied, student_feedback, create_date_time, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '30 day', 3, 5);
INSERT INTO PUBLIC.feedback (id, version, satisfied, student_feedback, create_date_time, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '11 day', 4, 2);
INSERT INTO PUBLIC.feedback (id, version, satisfied, student_feedback, create_date_time, mentor_id, user_id) VALUES (nextval('feedback_id_sequence'), 0, true, 'some feedback', current_timestamp - interval '42 day', 4, 1);