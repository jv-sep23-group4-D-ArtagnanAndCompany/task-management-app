INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (5, 'Task 5', 'Description 5', 'MEDIUM', 'NOT_STARTED', DATE_ADD(CURDATE(), INTERVAL 8 DAY), 2, 1);
