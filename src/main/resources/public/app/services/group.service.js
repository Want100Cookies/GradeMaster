app.factory('GroupService', function ($cookies, $q, $location, $resource, $http, $httpParamSerializer, $state) {
    return {
        getGroup: id => {
            const accessToken = $cookies.get("access_token");

            return $http({
                method: 'GET',
                url: 'http://localhost:8080/api/v1/groups/' + id,
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }).then(function (response) {
                console.log("Succesful API Call", response);
                return response.data;
            }).catch(function (data) {
                $state.transitionTo('app.dashboard')
            });
        },
        createGrades: (students, user, group) => {
            const accessToken = $cookies.get("access_token");

            let data = [];
            for (let i = 0; i < students.length; i++) {
                data.push({
                    fromUser: {id: user.id},
                    toUser: {id: students[i].id},
                    group: {id: group.id},
                    grade: students[i].grade.grade,
                    motivation: students[i].grade.motivation
                })
            }

            return $http({
                method: 'POST',
                url: 'http://localhost:8080/api/v1/grades/users/' + user.id,
                data: data,
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            }).then(response => {
                console.log("Succesful API Call", response);
                return response.data;
            });
        },
        createGroup: function (groupData) {
            const data = {
                "groupName": groupData.groupName,
                "startYear": groupData.startYear,
                "endYear": groupData.endYear,
                "users": groupData.users,
                "course": {
                    "id": 1
                },
                "period": [
                    "Q1",
                    "Q2"
                ],
            };
            const accessToken = $cookies.get("access_token");
            const req = {
                method: 'POST',
                url: 'http://localhost:8080/api/v1/groups',
                headers: {
                    "Authorization": "Bearer " + accessToken,
                    "Content-type": "application/json"
                },
                data
            };
            $http(req).then(function (data) {
                console.log(data);
            }).catch(function (data) {
                console.log(data);
            });
        }
    }
});