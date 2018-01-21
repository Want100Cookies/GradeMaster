function finalGroupOverviewController($stateParams, GradeService, GroupService) {
    let ctrl = this;
    ctrl.groupId = $stateParams.groupId;
    ctrl.groupMembers = [];
    ctrl.grades = [];
    GroupService.getGroupMembers(ctrl.groupId).then((response) => {
        ctrl.groupMembers = response.data.filter((user) => {
            return user.roles.filter((role) => {
                return role.code === "STUDENT_ROLE";
            }).length === 1
        });
        ctrl.getGrades();
    });

    ctrl.getGrades = () => {
        GradeService.getGradesByGroup(ctrl.groupId).then((response) => {
            ctrl.grades = response.data.grades;
        })
    };

    ctrl.getGradesForUser = (user) => {
        return ctrl.grades.filter((grade) => {
            return grade.fromUser.id === user.id;
        });
    }
}

app.component('finalGroupOverview', {
    templateUrl: '/app/components/finalGroupOverview.component.html',
    controller: finalGroupOverviewController,
});