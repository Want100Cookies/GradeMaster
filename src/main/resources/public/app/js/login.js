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
    $scope.changeLogin = () => {
        $state.transitionTo('register');
    }
    $scope.changeForgotPassword = () => {
        $state.transitionTo('retard');
    }
    $scope.login = (username, password) => {
        API.auth({user: {username, password}}).then((resp) => {
            $state.transitionTo('app.dashboard');
        }).catch((resp) => {
            $scope.valid = "Invalid credentials.";
        });
    }
});