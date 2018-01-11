INSERT INTO `user` (`id`, `email`, `name`, `password`, `verified`) VALUES
  (1, "john.doe@student.stenden.com", "John Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (2, "jane.doe@stenden.com", "Jane Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (3, "admin@stenden.com", "Administrator", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1);

INSERT INTO `role` (`id`, `code`, `label`) VALUES
  (1, "STUDENT_ROLE", "Student"),
  (2, "TEACHER_ROLE", "Teacher"),
  (3, "ADMIN_ROLE", "Admin");

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
  (1, 1),
  (2, 2),
  (3, 3);

INSERT INTO `course` (`id`, `name`) VALUES
  (1, "AJP"),
  (2, "EthHack"),
  (3, "Minor");

INSERT INTO `group` (`id`, `course_id`, `education`, `group_name`, `start_year`, `end_year`) VALUES
  (1, 1, "INF", "Musketiers", 2017, 2018),
  (2, 2, "INF", "Hackerman", 2017, 2018),
  (3, 3, "ENG", "Madam", 2016, 2017);

INSERT INTO `group_periods` (`group_id`, `period`) VALUES
  (1, "Q1"),
  (1, "Q2"),
  (2, "Q3"),
  (2, "Q4"),
  (3, "Q1");

INSERT INTO `user_groups` (`user_id`, `group_id`) VALUES
  (1, 1),
  (1, 2),
  (2, 3),
  (3, 3);

# All passwords are bcrypt hash of 'password'