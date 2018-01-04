INSERT INTO `user` (`id`, `email`, `name`, `password`) VALUES
  (1, "john.doe@student.stenden.com", "John Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6"),
  (2, "jane.doe@stenden.com", "Jane Doe", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6"),
  (3, "admin@stenden.com", "Administrator", "$2a$04$FYZXxiv7A74rX33gfs2m/.AGqhQ/unlJCB2nHLRiuHCVlECcyLyb6");

INSERT INTO `role` (`id`, `code`, `label`) VALUES
  (1, "STUDENT_ROLE", "Student"),
  (2, "TEACHER_ROLE", "Teacher"),
  (3, "ADMIN_ROLE", "Admin");

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
  (1, 1),
  (2, 2),
  (3, 3)

# All passwords are bcrypt hash of 'password'