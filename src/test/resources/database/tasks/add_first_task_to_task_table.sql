INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (1, 'Task 1', 'Description for Task 1', 'HIGH', 'IN_PROGRESS', DATE_ADD(CURDATE(), INTERVAL 10 DAY), 1, 1);