CREATE SCHEMA model;

CREATE TABLE model.users (
  user_id    INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  username   VARCHAR(50) NOT NULL,
  password   VARCHAR(50) NOT NULL DEFAULT 'password filler',
  first_name VARCHAR(30) NOT NULL DEFAULT 'first name filler',
  last_name  VARCHAR(30) NOT NULL DEFAULT 'last name filler',
  UNIQUE (username)
);

CREATE TABLE model.projects (
  project_id        INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  project_due_date  DATE        NOT NULL DEFAULT CURRENT_DATE,
  completed         BOOLEAN     NOT NULL DEFAULT FALSE,
  user_completed_id INTEGER,
  date_completed    DATE,
  project_name      VARCHAR(50) NOT NULL,
  CHECK (NOT (completed = TRUE AND (user_completed_id IS NULL OR date_completed IS NULL)))
);

CREATE TABLE model.project_tasks (
  task_id           INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  project_id        INTEGER     NOT NULL,
  task_name         VARCHAR(50) NOT NULL DEFAULT 'task_name_filler',
  task_due_date     DATE        NOT NULL DEFAULT CURRENT_DATE,
  completed         BOOLEAN              DEFAULT FALSE,
  user_completed_id INTEGER,
  date_completed    DATE,
  FOREIGN KEY (project_id) REFERENCES model.projects (project_id),
  FOREIGN KEY (user_completed_id) REFERENCES model.users (user_id),
  CHECK (NOT (completed = TRUE AND (user_completed_id IS NULL OR date_completed IS NULL)))
);

CREATE TABLE model.user_projects (
  user_id    INTEGER NOT NULL,
  project_id INTEGER NOT NULL,
  UNIQUE (user_id, project_id),
  FOREIGN KEY (user_id) REFERENCES model.users (user_id),
  FOREIGN KEY (project_id) REFERENCES model.projects (project_id)
);

CREATE TABLE model.user_assigned_project_tasks (
  user_id         INTEGER NOT NULL,
  project_task_id INTEGER NOT NULL,
  UNIQUE (user_id, project_task_id),
  FOREIGN KEY (user_id) REFERENCES model.users (user_id),
  FOREIGN KEY (project_task_id) REFERENCES model.project_tasks (task_id)
);

CREATE TABLE model.groups (
  group_id   INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  group_name VARCHAR(50) NOT NULL
);

CREATE TABLE model.group_tasks (
  task_id         INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  group_id        INTEGER     NOT NULL,
  task_name       VARCHAR(50) NOT NULL,
  task_due_date   DATE        NOT NULL,
  users_completed INTEGER     NOT NULL DEFAULT 0,
  FOREIGN KEY (group_id) REFERENCES model.groups (group_id)
);

CREATE TABLE model.user_groups (
  user_id  INTEGER NOT NULL,
  group_id INTEGER NOT NULL,
  UNIQUE (user_id, group_id),
  FOREIGN KEY (user_id) REFERENCES model.users (user_id),
  FOREIGN KEY (group_id) REFERENCES model.groups (group_id)
);

CREATE TABLE model.user_completed_group_tasks (
  user_id        INTEGER NOT NULL,
  task_id        INTEGER NOT NULL,
  date_completed DATE    NOT NULL,
  UNIQUE (user_id, task_id),
  FOREIGN KEY (user_id) REFERENCES model.users (user_id),
  FOREIGN KEY (task_id) REFERENCES model.group_tasks (task_id)
);

CREATE TABLE model.individual_tasks (
  task_id           INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  user_id           INTEGER     NOT NULL,
  task_name         VARCHAR(50) NOT NULL,
  task_due_date     DATE        NOT NULL,
  completed         BOOLEAN              DEFAULT FALSE,
  user_completed_id INTEGER,
  date_completed    DATE,
  FOREIGN KEY (user_id) REFERENCES model.users (user_id),
  CHECK (NOT (completed = TRUE AND
              ((user_completed_id IS NULL OR date_completed IS NULL) OR user_completed_id != user_id)))
);