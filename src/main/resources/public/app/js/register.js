app.controller('RegisterCtrl', function (RegisterService, $scope, $state) {
    $scope.form = {};
    $scope.register = () => {
        const data = {
            email: $scope.form.email,
            referenceId: $scope.form.referenceId,
            password: $scope.form.password,
        };
        RegisterService.register(data).then((resp) => {
            $state.transitionTo('registered');
        }).catch((error) => {
            $scope.error = "Register failed."
        });
    };

    $scope.toLogin = () => {
        $state.transitionTo('login');
    };
});