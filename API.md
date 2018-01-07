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

## /api/{v1}/users
### (GET)
Get all users
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

## /api/{v1}/groups/{id}/grades
### (GET)
Get the group grade and all the grades for the group members
### (POST)
Set the grade for this project

## /api/{v1}/groups/{id}/users
### (GET)
Get all users in this group
### (POST)
Add users to this group

## /api/{v1}/groups/{id}/users/{id}
### (DELETE)
Delete this user from the group

## /api/{v1}/groups/{id}/users/{id}/grades
### (GET)
Get the group/past grade(s)
### (POST)
Set the grades for all the group members
### (PATCH)
Adjust a grade for all the group members


## /api/{v1}/notifications
### (GET)
View all notifications
### (PATCH)
Mark all notifications read

## /api/{v1}/notifications/{id}
### (PATCH)
Mark notification read
