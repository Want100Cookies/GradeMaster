# API documentation

Login is done using oauth2

## /api/{v1}/auth/retard
### (POST)
Oh... You forgot your password. What a shame...
No grades for you.
```
{
  email
}
```

### (PATCH)
Set your new password
```
{
  token,
  email,
  password,
}
```

## /api/{v1}/auth/verify
### (PATCH)
Verify your email address
```
{
  token,
  email
}
```

## /api/{v1}/users
### (GET)
Get all users

Add `?role={role}` to your url to filter on role.
### (POST)
Make new user
```
{
  name,
  email,
  reference_id (nullable),
  password,
}
```

## /api/{v1}/users/{id}
### (GET)
Get single user
### (PATCH)
Update specific fields of the user
This includes adding a role (for example super user) and adding a profile picture
### (DELETE)
Delete a single user

## /api/{v1}/courses
### (GET)
Get all courses

### (POST)
Make a new course (only admins)
```
{
  name
}
```

## /api/{v1}/courses/{id}
### (GET)
Get a single course
### (PATCH)
Update specific field of the course
### (DELETE)
Delete a single course

## /api/{v1}/groups
### (GET)
Get all groups
### (POST)
Make a new student group
```
{
  education,
  startYear,
  endYear,
  period (array, enums),
  course,
  groupName
}
```
### (PATCH)
Update a specific field from the group

## /api/{v1}/groups/{id}/users
### (GET)
Get all users in this group
### (POST)
Add users to this group

## /api/{v1}/groups/{id}/users/{id}
### (DELETE)
Delete this user from the group

## api/{v1}/grades/groups/{id}
### (POST)
Set grades for group members
### (GET)
Get the group grade and all the grades for the group members
### (PATCH)
Adjust group grade
### (DELETE)
Delete graden given out by students for specific group

## api/{v1}/grades/status/groups/{id}
### (GET)
Get grading status of a particular group

## api/{v1}/grades/groups/{id}/users/{id}
### (GET)
Get final grade of student in a particular group

## api/{v1}/grades/groups/{id}/export.csv
### (GET)
Get grades exported to csv

## api/{v1}/grades/groups/{id}/export.pdf
### (GET)
Get grades exported to pdf

## /api/{v1}/notifications
### (GET)
View all notifications
### (PATCH)
Mark all notifications read

## /api/{v1}/notifications/{id}
### (PATCH)
Mark notification read
