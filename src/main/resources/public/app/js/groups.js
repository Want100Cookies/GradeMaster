app.controller('GroupsCtrl', function ($scope) {

});

app.controller('StudentGroupsCtrl', function($scope, UserService, StudentGroupsService){
    $scope.groupName;
    $scope.groupStatus = "Active";
    $scope.groupGrade;
    $scope.groupStudentGrade;
    $scope.groupStudentList;

    UserService.getUser().then(function(response){
        $scope.userDetails = response.data;
        StudentGroupsService.getStudentGroups($scope.userDetails.id).then(function(response){
            $scope.studentGroupsDetails = response.data;
            angular.forEach($scope.studentGroupsDetails, function(value){
                StudentGroupsService.getAllGroupMembers(value.id).then(function(response){
                    $scope.groupMembers = response.data;
                    console.log("MEMBERS", $scope.groupMembers);
                });
            });
        });
    });

});