-- Insert user
INSERT INTO users (id, user_name, password, email, first_name, last_name, is_deleted) VALUES (3, 'John', '$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG', 'john@gmail.com', 'John', 'Lollipop', false);

-- Insert project
INSERT INTO projects (id, name, description, start_date, end_date, status, is_deleted, user_id) VALUES (3, 'Work flow', 'Some working notes', '2023-11-16', '2024-10-16', 'IN_PROGRESS', false, 3);

-- Insert task
INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id, is_deleted) VALUES (2, 'Task 2', 'Task 2 description', 'HIGH', 'IN_PROGRESS', '2024-02-26', 3, 3, false);

-- Insert 2 comments
INSERT INTO comments (id, task_id, user_id, text, time_stamp, is_deleted) VALUES (1, 2, 3, 'Some comment 1', '2024-01-16 23:30:00', false);
INSERT INTO comments (id, task_id, user_id, text, time_stamp, is_deleted) VALUES (2, 2, 3, 'Some comment 2', '2024-01-16 23:30:00', false);

-- Insert user_role relations
INSERT INTO users_roles (user_id, role_id) VALUES (3, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (3, 1);
