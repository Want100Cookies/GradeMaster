function GroupCardCtrl($scope, StudentGroupsService){
    var ctrl = this;

    $scope.init = function(){
        ctrl.name =  $scope.$ctrl.group.groupName;
        ctrl.groupStatus = "TEST";
        ctrl.groupGrade = $scope.$ctrl.group.groupGrade.grade;

        $scope.status = "pending";
        $scope.groupGradeStatus = CheckGrade(ctrl.groupGrade);

        StudentGroupsService.getGroupMembers($scope.$ctrl.group.id).then(function(response){
            $scope.groupMembers = response.data;
        });

        StudentGroupsService.getFinalGroupGrade($scope.$ctrl.group.id, $scope.$ctrl.user.id).then(function(response){
            $scope.finalGroupGrades = response.data;
            $scope.finalGradeStatus = CheckGrade($scope.finalGroupGrades.grade);

        });
    }

    function CheckGrade(grade){
        if(grade <= 5.4){
            return "failMark";
        }
        else if(grade == 5.5){
            return "closeMark";
        }
        else if(grade > 5.5){
            return "passMark";
        }
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