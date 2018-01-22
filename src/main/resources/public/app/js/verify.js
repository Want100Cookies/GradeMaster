app.controller('VerifyCtrl', function ($scope, $state, $stateParams, $resource, $http, $timeout) {
    $scope.email = $stateParams.email;
    $scope.token = $stateParams.token;
    $scope.isLoading = true;
    $scope.isFinished = false;
    $scope.isError = false;
    $scope.verify = function (email, token) {
        var data = {
            email: email,
            token: token
        }
        var req = {
            method: 'PATCH',
            url: 'http://localhost:8080/api/v1/auth/verify',
            headers: {
                "Content-type": "application/json"
            },
            data
        }
        $http(req).then(function (data) {
            $scope.isLoading = false;
            $scope.isFinished = true;
            $timeout(function() {
                $state.transitionTo('login')
                }, 3000);
        }).catch(function (data) {
            $scope.error = "verification failed."
            $scope.isLoading = false;
            $scope.isError = true;
        })
    }
    $scope.verify($scope.email, $scope.token);
});