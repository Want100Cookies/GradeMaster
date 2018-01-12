app.controller('LoginCtrl', function ($scope, $resource, $http, $httpParamSerializer, $cookies) {
    $scope.vm = {
        formData: {
            email: '',
            password: ''
        },
        submit: function () {
            $scope.login($scope.vm.formData.email, $scope.vm.formData.password);
        }
    }
    $scope.login = function (username, password) {
        var data = {
            grant_type: 'password',
            username: username,
            password: password,
            scope: 'read write'
        }
        var req = {
            method: 'POST',
            url: '/oauth/token',
            headers: {
                "Authorization": "Basic " + "Z3JhZGVtYXN0ZXItY2xpZW50OmdyYWRlbWFzdGVyLXNlY3JldA==",
                "Content-type": "application/x-www-form-urlencoded; charset=utf-8"
            },
            data: $httpParamSerializer(data)
        }
        $http(req).then(function (data) {
            $http.defaults.headers.common.Authorization =
                'Bearer ' + data.data.access_token;
            $cookies.put("access_token", data.data.access_token);
            window.location.href = "#!/";
        }).catch(function (data){
            
        });
    }
});