app.controller('GroupsCtrl', function ($scope) {

});

app.controller('StudentGroupsCtrl', function($scope, UserService, StudentGroupsService){
    $scope.groupStatus = "Test Status";

    UserService.getUser().then(function(response){
        $scope.userDetails = response.data;
        StudentGroupsService.getStudentGroups($scope.userDetails.id).then(function(response){
            $scope.groupsDetails = response.data;
            console.log($scope.groupsDetails);
        });
    });
});