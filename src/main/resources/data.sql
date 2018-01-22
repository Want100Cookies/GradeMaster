INSERT INTO `user` (`id`, `email`, `name`, `password`, `verified`, `reference_id`) VALUES
  (1, "john.doe@student.stenden.com", "John Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, '123456'),
  (2, "jane.doe@stenden.com", "Jane Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, '654321'),
  (3, "admin@stenden.com", "Administrator", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, '987555');

INSERT INTO `role` (`id`, `code`, `label`) VALUES
  (1, "STUDENT_ROLE", "Student"),
  (2, "TEACHER_ROLE", "Teacher"),
  (3, "ADMIN_ROLE", "Admin");

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
  (1, 1),
  (2, 2),
  (3, 3);

INSERT INTO `education` (`id`, `name`) VALUES
  (1, "INF"),
  (2, "ENG");

INSERT INTO `course` (`id`, `name`, `education_id`) VALUES
  (1, "AJP", 1),
  (2, "EthHack", 1),
  (3, "Minor", 2);

INSERT INTO `group_grade` (`id`, `grade`, `comment`, `teacher_id`, `deadline`) VALUES
  (1, 6, "comment", 2, NOW() + INTERVAL 1 DAY),
  (2, 7, "comment", 2, NOW() + INTERVAL 1 DAY),
  (3, 8, "comment", 2, NOW() + INTERVAL 1 DAY);

INSERT INTO `group` (`id`, `course_id`, `group_name`, `start_year`, `end_year`, `group_grade_id`) VALUES
  (1, 1, "Musketiers", 2017, 2018, 1),
  (2, 2, "Hackerman", 2017, 2018, 2),
  (3, 3, "Madam", 2016, 2017, 3);

INSERT INTO `group_periods` (`group_id`, `period`) VALUES
  (1, "Q1"),
  (1, "Q2"),
  (2, "Q3"),
  (2, "Q4"),
  (3, "Q1");

INSERT INTO `user_groups` (`user_id`, `group_id`) VALUES
  (1, 1),
  (2, 2),
  (1, 2),
  (2, 3),
  (3, 3);

INSERT INTO `grade` (`id`, `grade`, `motivation`, `from_user_id`, `group_id`, `to_user_id`, `valid`) VALUES
  (1, 8, "motivation", 1, 1, 1, TRUE),
  (2, 8, "final grade", 2, 1, 1, TRUE),
  (3, 8.1, "final grade", 2, 2, 1, TRUE),
  (4, 8.2, "final grade", 2, 3, 1, TRUE),
  (5, 2, "delete this", 1, 3, 1, TRUE);

INSERT INTO `notification`(`id`, `title`, `message`, `seen`, `user_id`) VALUES
 (1, "Test", "You have pending grades!", 0, 1),
 (2, "Ratings","Your grade ratings have been sent!", 0, 1),
 (3, "Final", "Your final grade has been determined.", 0, 1),
 (4, "Received", "All group ratings received", 0, 2),
 (5, "Test", "Group grade can be made final", 0, 2),
 (6, "Wey", "Do u new de", 0, 2),
 (7, "Mekker", "Skoapy skoapy hopsa", 0, 2),
 (8, "Relevant", "Datz wet ai am", 0, 2),
 (9, "Test", "Group grade can be made final", 0, 2),
 (10, "Mayonnaise", "Music instruments", 0, 2),
 (11, "*Laying horizontal*", "BUT CAN YOU DO DIS", 0, 2);



# All passwords are bcrypt hash of 'password'
