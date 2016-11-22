CREATE TABLE users (
  user_id    INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  username   VARCHAR(50) NOT NULL,
  password   VARCHAR(50) NOT NULL,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  UNIQUE (username)
);

CREATE TABLE projects (
  project_id       INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  project_due_date DATE        NOT NULL,
  project_name     VARCHAR(50) NOT NULL
);

CREATE TABLE project_tasks (
  task_id           INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  project_id        INTEGER     NOT NULL,
  task_name         VARCHAR(50) NOT NULL,
  task_due_date     DATE        NOT NULL,
  completed         BOOLEAN              DEFAULT FALSE,
  user_completed_id INTEGER,
  date_completed    DATE,
  FOREIGN KEY (project_id) REFERENCES projects (project_id),
  FOREIGN KEY (user_completed_id) REFERENCES users (user_id)
);

CREATE TABLE user_projects (
  user_id    INTEGER NOT NULL,
  project_id INTEGER NOT NULL,
  UNIQUE (user_id, project_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id),
  FOREIGN KEY (project_id) REFERENCES projects (project_id)
);

CREATE TABLE user_assigned_project_tasks (
  user_id         INTEGER NOT NULL,
  project_task_id INTEGER NOT NULL,
  UNIQUE (user_id, project_task_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id),
  FOREIGN KEY (project_task_id) REFERENCES project_tasks (task_id)
);

CREATE TABLE groups (
  group_id   INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  group_name VARCHAR(50) NOT NULL
);

CREATE TABLE group_tasks (
  task_id         INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  group_id        INTEGER     NOT NULL,
  task_name       VARCHAR(50) NOT NULL,
  task_due_date   DATE        NOT NULL,
  users_completed INTEGER     NOT NULL DEFAULT 0,
  FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE user_groups (
  user_id  INTEGER NOT NULL,
  group_id INTEGER NOT NULL,
  UNIQUE (user_id, group_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id),
  FOREIGN KEY (group_id) REFERENCES groups (group_id)
);


CREATE TABLE individual_tasks (
  task_id       INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  user_id       INTEGER     NOT NULL,
  task_name     VARCHAR(50) NOT NULL,
  task_due_date DATE        NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (user_id)
)