teacherCardCtrl = ($scope, GroupService) => {
    var ctrl = this;

    $scope.init = () => {
        $scope.groupGrade = "TBD";
        if ($scope.$ctrl.groupGrade !== null) {
            $scope.groupGrade = $scope.$ctrl.group.groupGrade.grade;
            console.log($scope.$ctrl.group.groupGrade.grade);
        }
    }
}
app.component('teacherCard', {
    templateUrl: '/app/components/teacherCard.component.html',
    controller: teacherCardCtrl,
    bindings: {
        group: '<',
    }
});