function GroupCardCtrl($scope, StudentGroupsService){
    var ctrl = this;

    $scope.init = function(){
        ctrl.name =  $scope.$ctrl.group.groupName;
        ctrl.groupStatus = "TEST";
        ctrl.groupGrade = $scope.$ctrl.group.groupGrade.grade;

        $scope.status = "pending";

        StudentGroupsService.getGroupMembers($scope.$ctrl.group.id).then(function(response){
            $scope.groupMembers = response.data;
        });

        StudentGroupsService.getFinalGroupGrade($scope.$ctrl.group.id, $scope.$ctrl.user.id).then(function(response){
            $scope.finalGroupGrades = response.data;
        });
    }


}

app.component('groupCard', {
    templateUrl: '/app/components/groupCard.component.html',
    controller: GroupCardCtrl,
    bindings: {
        user: '<',
        group: '<',
    }
});