app.controller('RegisterCtrl', function ($scope, $resource, $http, $httpParamSerializer, $cookies, $state) {
    $scope.vm = {
        formData: {
            email: '',
            password: '',
            confirmPassword: ''
        },
        register: function () {
            $scope.register($scope.vm.formData.email, $scope.vm.formData.password);
        }
    }
    $scope.error = ""
    $scope.register = function(email, password) {
        var data = {
            email: email,
            password: password
        }
        var req = {
            method: 'POST',
            url: 'http://192.168.1.128:8080/api/v1/users',
            headers: {
                "Content-type": "application/json"
            },
            data
        }
        $http(req).then(function (data) {
            $state.transitionTo('registered')
        }).catch(function (data){
            $scope.error = "Register failed."
        });
    }
});