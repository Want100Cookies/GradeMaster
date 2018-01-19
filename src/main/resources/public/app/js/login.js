app.controller('LoginCtrl', function (API, $scope, $state) {
    $scope.valid = "";
    $scope.vm = {
        formData: {
            email: '',
            password: ''
        },
        submit: function () {
            $scope.login($scope.vm.formData.email, $scope.vm.formData.password);
        }
    }
    $scope.changeLogin = function() {
        $state.transitionTo('register')
    }
    $scope.login = function (username, password) {
        API.auth({user: {username, password}}).then((resp) => {
            $state.transitionTo('app.dashboard');
        }).catch((resp) => {
            $scope.valid = "Invalid credentials.";
        });
    }
});
