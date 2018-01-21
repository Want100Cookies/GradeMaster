function gradingController($stateParams, $mdDialog, $state, UserService, GroupService, GradeService) {
    let ctrl = this;

    ctrl.groupGrade = 0;
    ctrl.group = {};
    ctrl.students = [];
    ctrl.self = {};
    ctrl.loading = true;

    UserService.getSelf().then(user => {
        ctrl.self = user.data;
        
    });

    GroupService.getGroup($stateParams.groupId).then(response => {
        const group = response.data;
        ctrl.group = group;
        ctrl.groupGrade = group.groupGrade.grade;
        ctrl.students = group.users.filter(user => {
            return user.roles.filter(role => {
                return role.code === "STUDENT_ROLE"
            }).length === 1;
        });

        for (let i = 0; i < ctrl.students.length; i++) {
            ctrl.students[i].grade = {
                grade: ctrl.groupGrade,
                motivation: "",
            };
        }
        ctrl.loading = false;
    }).catch(function (data) {
        $state.transitionTo('app.dashboard')
    });;

    ctrl.save = () => {
        let confirm = $mdDialog.confirm()
            .title("Are you sure?")
            .textContent("Are your sure you want to save these grades and remarks? You cannot edit these after saving!")
            .ok("Yes, I am sure the data I entered is correct!")
            .cancel("I don't know...");

        $mdDialog.show(confirm)
            .then(() => {
                ctrl.loading = true;

                GradeService.createGrades(ctrl.students, ctrl.self, ctrl.group).then(() => {
                    $state.transitionTo("app.groups");
                }, () => {
                    ctrl.loading = false;

                    $mdDialog.show($mdDialog.alert()
                        .title("Error")
                        .textContent("There was an error processing your request.")
                        .ok("Okay"));
                });
            });
    };
}

app.component('grading', {
    templateUrl: '/app/components/grading.component.html',
    controller: gradingController,
});