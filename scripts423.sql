--Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах
-- (достаточно получить только имя и возраст студента)
-- школы Хогвартс вместе с названиями факультетов.
--Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.

SELECT s.name,s.age
FROM students s
LEFT JOIN faculties f on f.id = s.faculty_id;

SELECT s.name,s.name,a.id
FROM students s
         left join avatar a on s.id = a.student_id