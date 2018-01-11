app.controller('LoginCtrl', function($scope, $resource, $http, $httpParamSerializer, $cookies){
    $scope.data = {
        grant_type: 'password',
        username: 'admin@stenden.com',
        password: 'password',
        scope: 'read write'
    }

    $scope.encoded = btoa('clientIdPassword:secret');
    $scope.login = function() {
        var req = {
            method: 'POST',
            url: 'http://192.168.1.128:8080/oauth/token',
            headers: {
                "Authorization": "Basic" + $scope.encoded,
                "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
            },
            data: $httpParamSerializer($scope.data)
        }
        $http(req).then(function(data){
            $http.defaults.headers.common.Authorization =
            'Bearer ' + data.data.access_token;
            $cookies.put("access_token", data.data.access_token);
            window.location.href="#!/";
            // console.log($cookies.get("access_token"));
        })
    }
});