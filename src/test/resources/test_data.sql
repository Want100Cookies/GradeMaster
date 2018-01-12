SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE `user_roles`;
TRUNCATE TABLE `user_groups`;
TRUNCATE TABLE `user`;
TRUNCATE TABLE `course`;
TRUNCATE TABLE `group_periods`;
TRUNCATE TABLE `group`;
TRUNCATE TABLE `education`;

INSERT INTO `user` (`id`, `email`, `name`, `password`, `verified`) VALUES
  (1, "john.doe@student.stenden.com", "John Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (2, "jane.doe@stenden.com", "Jane Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (3, "admin@stenden.com", "Administrator", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1);

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

INSERT INTO `group` (`id`, `course_id`, `group_name`, `start_year`, `end_year`) VALUES
  (1, 1, "Musketiers", 2017, 2018),
  (2, 2, "Hackerman", 2017, 2018),
  (3, 3, "Madam", 2016, 2017);

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

SET FOREIGN_KEY_CHECKS=1;
# All passwords are bcrypt hash of 'password'