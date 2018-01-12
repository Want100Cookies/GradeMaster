app.controller('LoginCtrl', function($scope, $resource, $http, $httpParamSerializer, $cookies){
    $scope.data = {
        grant_type: 'password',
        username: 'admin@stenden.com',
        password: 'password',
        scope: 'read write'
    }
    
    $scope.login = function() {
        var req = {
            method: 'POST',
            url: 'http://192.168.1.128:8080/oauth/token',
            headers: {
                "Authorization": "Basic " + "Z3JhZGVtYXN0ZXItY2xpZW50OmdyYWRlbWFzdGVyLXNlY3JldA==",
                "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
            },
            data: $httpParamSerializer($scope.data)
        }
        $http(req).then(function(data){
            $http.defaults.headers.common.Authorization =
            'Bearer ' + data.data.access_token;
            $cookies.put("access_token", data.data.access_token);
            window.location.href="#!/";
        })
    }
});