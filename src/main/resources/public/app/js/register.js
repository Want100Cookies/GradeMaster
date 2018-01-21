app.controller('RegisterCtrl', function ($scope, $http, $state) {
    $scope.form = {};
    $scope.register = function () {
        const data = {
            email: $scope.form.email,
            referenceId: $scope.form.referenceId,
            password: $scope.form.password,
        };
        const req = {
            method: 'POST',
            url: 'http://192.168.1.128:8080/api/v1/users',
            headers: {
                "Content-type": "application/json"
            },
            data
        };
        $http(req).then(function (data) {
            $state.transitionTo('registered')
        }).catch(function (data){
            $scope.error = "Register failed."
        });
    }

    $scope.toLogin = () => {
        $state.transitionTo('login');
    }
});