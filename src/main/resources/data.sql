INSERT INTO `user` (`id`, `email`, `name`, `password`, `verified`) VALUES
  (1, "john.doe@student.stenden.com", "John Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (2, "jane.doe@stenden.com", "Jane Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (3, "admin@stenden.com", "Administrator", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1),
  (4, "danny.hooijer@student.stenden.com", "Danny Hooijer", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6", 1);

INSERT INTO `role` (`id`, `code`, `label`) VALUES
  (1, "STUDENT_ROLE", "Student"),
  (2, "TEACHER_ROLE", "Teacher"),
  (3, "ADMIN_ROLE", "Admin");

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 1);

INSERT INTO `education` (`id`, `name`) VALUES
  (1, "INF"),
  (2, "ENG");

INSERT INTO `course` (`id`, `name`, `education_id`) VALUES
  (1, "AJP", 1),
  (2, "EthHack", 1),
  (3, "Minor", 2);

INSERT INTO `group_grade` (`id`,`grade`,`comment`) VALUES
  (1, 6, "comment"),
  (2, 7, "comment"),
  (3, 8, "comment");

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
  (1, 2),
  (2, 3),
  (3, 3),
  (4, 1);

INSERT INTO `grade` (`id`, `grade`, `motivation`, `from_user_id`, `group_id`, `to_user_id`) VALUES
  (1, 8, "motivation", 1, 1, 1),
  (2, 7, "motivation1", 2, 3, 2),
  (3, 5, "motivation2", 3, 3, 2),
  (4, 2, "motivation3", 3, 3, 3),
  (5, 2, "motivation4", 2, 3, 3);


INSERT INTO `notification`(`id`, `title`, `message`, `seen`, `user_id`) VALUES
 (1, "Test", "You have pending grades!", 0, 1),
 (2, "Ratings","Your grade ratings have been sent!", 0, 1),
 (3, "Final", "Your final grade has been determined.", 0, 1),
 (4, "Received", "All group ratings received", 0, 2),
 (5, "Test", "Group grade can be made final", 0, 2);



# All passwords are bcrypt hash of 'password'