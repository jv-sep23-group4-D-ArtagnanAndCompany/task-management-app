-- Insert user
INSERT INTO users (id, user_name, password, email, first_name, last_name, is_deleted) VALUES (3, 'John', '$2a$10$fRCKtHfKmoKkzXByokmM6.FVmatskXfInb.IYBUI1ukvDBjN4EqGG', 'john@gmail.com', 'John', 'Lollipop', false);

-- Insert role
-- INSERT INTO roles (id, role_name, is_deleted) VALUES (3, 'USER', false);

-- Insert user_role relation
INSERT INTO users_roles (user_id, role_id) VALUES (3, 2);