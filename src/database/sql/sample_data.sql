SET SCHEMA MODEL;

INSERT INTO USERS (USERNAME) VALUES
  ('person_a'),
  ('person_b'),
  ('person_c'),
  ('person_d');
INSERT INTO PROJECTS (PROJECT_NAME) VALUES
  ('project_a'),
  ('project_b'),
  ('project_c'),
  ('project_d');
INSERT INTO PROJECT_TASKS (PROJECT_ID, TASK_NAME) VALUES
  (1, 'task_a'),
  (2, 'task_b'),
  (3, 'task_c'),
  (4, 'task_d');

INSERT INTO USER_PROJECTS (USER_ID, PROJECT_ID) VALUES 
  (1, 1),
  (1, 2);
