INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (3, 'Task 3', 'Description 3', 'HIGH', 'COMPLETED', DATE_ADD(CURDATE(), INTERVAL 9 DAY), 1, 2);
