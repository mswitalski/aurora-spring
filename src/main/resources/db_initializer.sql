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