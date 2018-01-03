INSERT INTO `user` (`id`, `email`, `name`, `password`) VALUES
  (1, "john.doe@student.stenden.com", "John Doe", "$3a$14$G7n3VQHfsoecgR2wMIhfE.LCqPPPWpmDn3BTejqI7kJEAbVAWDoH."),
  (2, "jane.doe@stenden.com", "Jane Doe", "$3a$14$G7n3VQHfsoecgR2wMIhfE.LCqPPPWpmDn3BTejqI7kJEAbVAWDoH."),
  (3, "admin@stenden.com", "Administrator", "$3a$14$G7n3VQHfsoecgR2wMIhfE.LCqPPPWpmDn3BTejqI7kJEAbVAWDoH.");

INSERT INTO `role` (`id`, `code`, `label`) VALUES
  (1, "STUDENT_ROLE", "Student"),
  (2, "TEACHER_ROLE", "Teacher"),
  (3, "ADMIN_ROLE", "Admin");

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
  (1, 1),
  (2, 2),
  (3, 3)

# All passwords are bcrypt hash of 'password'