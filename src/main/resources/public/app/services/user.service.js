app.factory('UserService', function (API) {
    return {
        getSelf: function () {
            const accessToken = $cookies.get("access_token");
            const req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/users/self',
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                cache: true,
            };

            return $http(req).then(function (response) {
                return response.data;
            }).catch(function (response) {
                return response;
            });
        },
        getAllUsers: function () {
            return API.get({
                path: `users`
            });
        },
    }
});