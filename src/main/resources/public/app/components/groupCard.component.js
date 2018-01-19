function GroupCardCtrl($scope, StudentGroupsService){
    var ctrl = this;

    $scope.init = function(){
        ctrl.name =  $scope.$ctrl.group.groupName; 
        $scope.groupGrades = [];
        $scope.finalGrades = [];

        StudentGroupsService.getGroupMembers($scope.$ctrl.group.id).then(function(response){
            $scope.groupMembers = response.data;
        });

        StudentGroupsService.getFinalGroupGrade($scope.$ctrl.group.id, $scope.$ctrl.user.id).then(function(response){
                $scope.finalGroupGrades = response.data;
                if($scope.finalGroupGrades.grade)
                {                  
                    $scope.finalGrades.push($scope.finalGroupGrades.grade);
                    $scope.finalGradeStatus = CheckGrade($scope.finalGrades);                  
                }
                else
                {
                    $scope.finalGrades.push("T.B.D");
                }

        });

        StudentGroupsService.getGradingStatus($scope.$ctrl.group.id).then(function(response){
            $scope.groupsStatuses = response.data;
            $scope.groupStatus = CheckStatus($scope.groupsStatuses.status);
        });

        if(!$scope.$ctrl.group.groupGrade){
            $scope.groupGrades.push("T.B.D");
        }
        else if($scope.$ctrl.group.groupGrade.grade){
            $scope.groupGrades.push($scope.$ctrl.group.groupGrade.grade);
            $scope.groupGradeStatus = CheckGrade($scope.$ctrl.group.groupGrade.grade);
        }

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

    function CheckStatus(status){
        if(status == "OPEN"){
            return "open";
        }
        else if(status == "PENDING"){
            return "pending";
        }
        else if(status == "CLOSED"){
            return "closed";
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