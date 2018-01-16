app.controller('VerifyCtrl', function ($scope, $state, $stateParams, $resource, $http) {
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
            url: 'http://192.168.1.128:8080/api/v1/auth/verify',
            headers: {
                "Content-type": "application/json"
            },
            data
        }
        $http(req).then(function (data) {
            $scope.isLoading = false;
            $scope.isFinished = true;
        }).catch(function (data) {
            $scope.error = "verification failed."
            $scope.isLoading = false;
            $scope.isError = true;
        })
    }
    $scope.verify($scope.email, $scope.token);
});