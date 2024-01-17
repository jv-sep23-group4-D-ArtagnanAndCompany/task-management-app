-- Delete users_roles relations
DELETE FROM users_roles WHERE user_id = 3;
DELETE FROM users_roles WHERE user_id = 4;

-- Delete 2 comments
DELETE FROM comments WHERE task_id = 2;

-- Delete task
DELETE FROM tasks WHERE id = 2;

-- Delete project
DELETE FROM projects WHERE id = 3;

-- Delete users
DELETE FROM users WHERE id = 3;
DELETE FROM users WHERE id = 4;
