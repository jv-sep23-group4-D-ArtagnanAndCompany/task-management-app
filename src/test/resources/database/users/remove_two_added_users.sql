-- Delete users_roles relations
DELETE FROM users_roles WHERE user_id = 3;
DELETE FROM users_roles WHERE user_id = 4;

-- Delete users
DELETE FROM users WHERE id = 3;
DELETE FROM users WHERE id = 4;
