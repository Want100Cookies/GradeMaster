app.controller('LoginCtrl', function ($scope, $resource, $http, $httpParamSerializer, $cookies, $mdDialog, $state) {
  
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
            url: 'http://localhost:8080/oauth/token',
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
            $state.transitionTo('app.dashboard')
        }).catch(function (data){
            console.log("Error, you don't know de wey")
        });
    }
});