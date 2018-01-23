INSERT INTO `user` (`id`, `email`, `name`, `password`, `verified`, `reference_id`) VALUES
<<<<<<< HEAD
  (1, "john.doe@student.stenden.com", "John Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, '123456'),
  (2, "jane.doe@stenden.com", "Jane Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, '654321'),
  (3, "admin@stenden.com", "Administrator", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, '987555'),
  (4, "test.doe@student.stenden.com", "John Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, '123456');
=======
(1, "pascal.drewes@student.stenden.com", "Pascal Drewes", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, "123456"),
(2, "admin@stenden.com", "Administrator", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, '987555'),
(3, "cas.van.dinter@student.stenden.com", "Cas van Dinter", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, "123456"),
(4, "danny.hooijer@student.stenden.com", "Danny Hooijer", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, "123456"),
(5, "bas.van.t.holt@stenden.com", "Bas van't Holt", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1, "123456");
>>>>>>> 105352b401274b6ff2367ac5b4f1c8c52968ed58

INSERT INTO `role` (`id`, `code`, `label`) VALUES
  (1, "STUDENT_ROLE", "Student"),
  (2, "TEACHER_ROLE", "Teacher"),
  (3, "ADMIN_ROLE", "Admin");

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
  (1, 1),
<<<<<<< HEAD
  (2, 2),
  (3, 3),
  (4, 1);
=======
  (2, 3),
  (3, 1),
  (4, 1),
  (5, 2);
>>>>>>> 105352b401274b6ff2367ac5b4f1c8c52968ed58

INSERT INTO `education` (`id`, `name`) VALUES
  (1, "INF"),
  (2, "ENG");

INSERT INTO `course` (`id`, `name`, `education_id`) VALUES
  (1, "AJP", 1),
  (2, "EthHack", 1),
  (3, "Minor", 2);


INSERT INTO `group_grade` (`id`, `grade`, `comment`, `teacher_id`, `deadline`) VALUES
  (1, 6, "comment", 2, NOW() + INTERVAL 1 DAY);

INSERT INTO `group` (`id`, `course_id`, `group_name`, `start_year`, `end_year`, `group_grade_id`) VALUES
  (1, 1, "Datbois", 2017, 2018, 1);

INSERT INTO `group_periods` (`group_id`, `period`) VALUES
  (1, "Q2");

INSERT INTO `user_groups` (`user_id`, `group_id`) VALUES
  (1, 1),
  (3, 1),
  (4, 1),
  (5, 1);

INSERT INTO `notification`(`id`, `title`, `message`, `seen`, `user_id`) VALUES
  (1, "Datbois has been graded", "Your group with group name \"Datbois\" has been graded by Bas van't Holt. You can now view your group grade and grade your team members.", 0, 1),
  (2, "Datbois has been graded", "Your group with group name \"Datbois\" has been graded by Bas van't Holt. You can now view your group grade and grade your team members.", 0, 3),
  (3, "Datbois has been graded", "Your group with group name \"Datbois\" has been graded by Bas van't Holt. You can now view your group grade and grade your team members.", 0, 4);


# All passwords are bcrypt hash of 'password'
