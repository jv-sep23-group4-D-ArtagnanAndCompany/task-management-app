-- Insert users
INSERT INTO users (id, user_name, password, email, first_name, last_name, is_deleted) VALUES (3, 'John', '$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG', 'john1@gmail.com', 'John', 'Lollipop', false);
INSERT INTO users (id, user_name, password, email, first_name, last_name, is_deleted) VALUES (4, 'Kris', '$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG', 'kris1@gmail.com', 'Kris', 'Fisher', false);

-- Insert user_role relations
INSERT INTO users_roles (user_id, role_id) VALUES (3, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (3, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (4, 2);