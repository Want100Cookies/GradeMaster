function educationController($stateParams, $mdDialog, $state, EducationService) {
    let ctrl = this;

    ctrl.education = {};
    ctrl.courses = [];

    ctrl.$onInit = () => {
        EducationService
            .getEducation($stateParams.educationId)
            .then(response => {
                ctrl.education = response.data;
                console.log(ctrl.education);
            }, () => {
            });

        EducationService
            .getCoursesByEducation($stateParams.educationId)
            .then(response => {
                ctrl.courses = response.data;
            }, () => {
            });
    };

    ctrl.viewCourse = course => {
        $state.transitionTo("app.course", {
            courseId: course.id,
        });
    };

    ctrl.delete = () => {
        let confirm = $mdDialog
            .confirm()
            .title("Are you sure?")
            .textContent("Are your sure you want to delete this education? This action is destructive and will delete all related courses/groups/grades!")
            .ok("Yes, I am sure I want to delete this education")
            .cancel("I don't know...");

        $mdDialog
            .show(confirm)
            .then(() => {
                EducationService
                    .deleteEducation(ctrl.education)
                    .then(() => {
                        $state.transitionTo('app.educations');
                    });
            }, () => {
            });
    };
}

app.component('education', {
    templateUrl: '/app/components/education.component.html',
    controller: educationController,
});