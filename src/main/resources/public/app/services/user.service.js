app.factory('UserService', function ($cookies, $q, $resource, $http, $state) {
    return {
        getRole: function () {
            const accessToken = $cookies.get("access_token");
            const req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/users/self',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            };
            return $http(req).then(function (data) {
                for (let key in data.data.roles) {
                    let role = data.data.roles[key].label;
                }
                return role;
            }).catch(function (data) {
                console.log(data);
            });
        },
        getAllUsers: function () {
            const accessToken = $cookies.get("access_token");
            const req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/users',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            };
            return $http(req).then(function (data) {
                return data;
            }).catch(function (data) {
                console.log(data);
            });
        }
    }
});