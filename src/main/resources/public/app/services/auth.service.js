app.factory('AuthService', function ($cookies, $q, $location, $resource, $http, $httpParamSerializer, $state) {
    return {
        authenticate: function () {
            const accessToken = $cookies.get("access_token");
            const req = {
                method: 'GET',
                url: 'http://localhost:8080/api/v1/users/self',
                headers: {
                    "Authorization": "Bearer " + accessToken
                }
            };
            return $http(req).then(function (data) {
                return true;
            }).catch(function (data) {
                $state.transitionTo('login')
            });
        }
    }
});