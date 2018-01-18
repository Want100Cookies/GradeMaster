function GroupCardCtrl($scope, StudentGroupsService){
    var ctrl = this;

    $scope.init = function(){
        $scope.name =  $scope.$ctrl.group.groupName;
        $scope.groupStatus = "TEST";
        $scope.groupGrade = $scope.$ctrl.group.groupGrade.grade;
        $scope.studentGrade = "MOET NOG";

        StudentGroupsService.getGroupMembers($scope.$ctrl.group.id).then(function(response){
            $scope.groupMembers = response.data;
        });

        console.log($scope.$ctrl.group);
    }
}

app.component('groupCard', {
    templateUrl: '/app/components/groupCard.component.html',
    controller: GroupCardCtrl,
    bindings: {
        group: '<',
    }
});