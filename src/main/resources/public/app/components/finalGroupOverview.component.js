function finalGroupOverviewController($stateParams, GradeService, GroupService) {
    let ctrl = this;
    ctrl.groupId = $stateParams.groupId;
    ctrl.groupMembers = [];
    ctrl.grades = [];
    GroupService.getGroupMembers(ctrl.groupId).then((response) => {
        ctrl.groupMembers = response.data;
        ctrl.getGrades();
    });

    ctrl.getGrades = () => {
        GradeService.getGradesByGroup(ctrl.groupId).then((response) => {
            ctrl.grades = response.data.grades;
            console.log(ctrl.grades);
            ctrl.userGrades = [];
            var newGrades = ctrl.grades.filter(ctrl.filterGrade);
            // console.log(ctrl.userGrades);
        })
    }
    ctrl.filterGrade = (grade) => {
        var gradesFromUser = [];
        ctrl.groupMembers.forEach((user) => {
            if(grade.fromUser.id = user.id){
                // console.log(grade);
                gradesFromUser.push(user, grade);
            }
        })
        ctrl.userGrades.push(gradesFromUser);
    }
}

app.component('finalGroupOverview', {
    templateUrl: '/app/components/finalGroupOverview.component.html',
    controller: finalGroupOverviewController,
});