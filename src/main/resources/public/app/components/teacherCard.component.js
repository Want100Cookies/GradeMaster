teacherCardCtrl = ($scope, GroupService) => {
    var ctrl = this;

    $scope.init = () => {
        $scope.groupGrade = "TBD";
        $scope.groupStatus = "TBD";
        $scope.groupName = "Couldn't resolve name";
        $scope.groupMembers = [];
        $scope.groupId = $scope.$ctrl.group.id;
        if ($scope.$ctrl.group.groupGrade !== null) {
            $scope.groupStatus = "GRADED"
            $scope.groupGrade = $scope.$ctrl.group.groupGrade.grade;
        } else {
            $scope.groupStatus = "PENDING"
        }
        
        if ($scope.$ctrl.group !== null) {
            $scope.groupName = $scope.$ctrl.group.groupName;
        }

        // Service calls
        GroupService.getGroupMembers($scope.$ctrl.group.id).then((response) => {
           $scope.groupMembers = response.data;
        });
    }
}
app.component('teacherCard', {
    templateUrl: '/app/components/teacherCard.component.html',
    controller: teacherCardCtrl,
    bindings: {
        group: '<',
    }
});