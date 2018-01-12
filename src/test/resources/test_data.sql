SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE `user_roles`;
TRUNCATE TABLE `user_groups`;
TRUNCATE TABLE `user`;
TRUNCATE TABLE `course`;
TRUNCATE TABLE `group_periods`;
TRUNCATE TABLE `group`;
TRUNCATE TABLE `grade`;
TRUNCATE TABLE `group_grade`;

INSERT INTO `user` (`id`, `email`, `name`, `password`, `verified`) VALUES
  (1, "john.doe@student.stenden.com", "John Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (2, "jane.doe@stenden.com", "Jane Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (3, "admin@stenden.com", "Administrator", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1);

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
  (1, 1),
  (2, 2),
  (3, 3);

INSERT INTO `course` (`id`, `name`) VALUES
  (1, "AJP"),
  (2, "EthHack"),
  (3, "Minor");

INSERT INTO `group_grade` (`id`,`grade`,`comment`) VALUES
  (1, 6, "comment"),
  (2, 7, "comment"),
  (3, 8, "comment");

INSERT INTO `group` (`id`, `course_id`, `education`, `group_name`, `start_year`, `end_year`, `group_grade_id`) VALUES
  (1, 1, "INF", "Musketiers", 2017, 2018, 1),
  (2, 2, "INF", "Hackerman", 2017, 2018, 2),
  (3, 3, "ENG", "Madam", 2016, 2017, 3);

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

INSERT INTO `grade` (`id`, `grade`, `motivation`, `from_user_id`, `group_id`, `to_user_id`) VALUES
  (1, 8, "motivation", 1, 1, 1),
  (2, 7, "motivation", 1, 1, 2),
  (3, 5, "motivation", 1, 1, 3);

SET FOREIGN_KEY_CHECKS=1;
# All passwords are bcrypt hash of 'password'