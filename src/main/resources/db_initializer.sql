-- Define basic roles
INSERT INTO PUBLIC.role VALUES ('ADMIN');
INSERT INTO PUBLIC.role VALUES ('UNIT_LEADER');
INSERT INTO PUBLIC.role VALUES ('EMPLOYEE');

-- Add users
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals) VALUES (nextval('user_id_sequence'), 'admin', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Jan', 'Kowalski', 'admin@bar.mail.dummy', true, 'Szef', 'Make dreams come true');
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals) VALUES (nextval('user_id_sequence'), 'unitleader', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Karolina', 'Nowak', 'unitleader@bar.mail.dummy', true, 'Unit leader', 'Deliver IoT Nova project before deadline');
INSERT INTO PUBLIC."user" (id, username, password, name, surname, email, enabled, position, goals) VALUES (nextval('user_id_sequence'), 'employee', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'Adam', 'Zaborowski', 'employee@bar.mail.dummy', true, 'Junior Java Programmer', 'Opanować dobrze Jave i zostać wreszcie regularem');

-- Create relationships between users and roles
INSERT INTO PUBLIC.user_role VALUES (1, 'ADMIN');
INSERT INTO PUBLIC.user_role VALUES (2, 'UNIT_LEADER');
INSERT INTO PUBLIC.user_role VALUES (3, 'EMPLOYEE');