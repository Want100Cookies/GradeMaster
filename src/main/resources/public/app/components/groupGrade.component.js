function groupGradeController($stateParams, $mdDialog, $state, GroupService, GradeService) {
    let ctrl = this;
    ctrl.group = {};

    ctrl.$onInit = () => {
        GroupService
            .getGroup($stateParams.groupId)
            .then((response) => {
                ctrl.group = response.data;
                if (ctrl.group.groupGrade !== null) {
                    $state.transitionTo('app.dashboard');
                } else {
                    ctrl.group.groupGrade = {
                        grade: 5.5,
                        comment: '',
                    };
                }
            }, () => {
                $state.transitionTo('app.dashboard');
            });
    };

    ctrl.save = () => {
        GradeService
            .createGroupGrade(ctrl.group)
            .then((groupGrade) => {
                $state.transitionTo("app.groups");
            });
    };
}

app.component('groupGrade', {
    templateUrl: '/app/components/groupGrade.component.html',
    controller: groupGradeController,
});