app.controller('VerifyCtrl', function (VerifyService, $scope, $state, $stateParams, $timeout) {
    $scope.email = $stateParams.email;
    $scope.token = $stateParams.token;
    $scope.isLoading = true;
    $scope.isFinished = false;
    $scope.isError = false;
    $scope.verify = (email, token) => {
        const data = {
            email,
            token
        };

        VerifyService.verify(data).then((resp) => {
            $scope.isLoading = false;
            $scope.isFinished = true;
            $timeout(() => {
                $state.transitionTo('login');
            }, 3000);
        }).catch((error) => {
            $scope.error = "verification failed."
            $scope.isLoading = false;
            $scope.isError = true;
        });
    }
    $scope.verify($scope.email, $scope.token);
});