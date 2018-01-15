app.controller('VerifyCtrl', function ($scope, $state, $stateParams, $resource, $http) {
    $scope.email = $stateParams.email;
    $scope.token = $stateParams.token;
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
            console.log("You now know da wae");
        }).catch(function (data) {
            $scope.error = "verification failed."
        });
    }
    $scope.verify($scope.email, $scope.token);
});