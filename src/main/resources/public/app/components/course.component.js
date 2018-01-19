function courseController($stateParams, $mdDialog, $state, CourseService) {
    let ctrl = this;

    ctrl.course = {};
    ctrl.groups = [];

    ctrl.$onInit = () => {
        CourseService
            .getCourse($stateParams.courseId)
            .then(response => {
                ctrl.course = response.data;
            }, () => {
                $state.transitionTo('app.dashboard'); // Todo: make 404
            });

        CourseService
            .getGroupsByCourse($stateParams.courseId)
            .then(response => {
                ctrl.groups = response.data;
            }, () => {
                $state.transitionTo('app.dashboard'); // Todo: make 404
            });
    };

    ctrl.delete = () => {
        let confirm = $mdDialog
            .confirm()
            .title("Are you sure?")
            .textContent("Are your sure you want to delete this course? This action is destructive and will delete all related groups/grades!")
            .ok("Yes, I am sure I want to delete this course")
            .cancel("I don't know...");

        $mdDialog
            .show(confirm)
            .then(() => {
                CourseService
                    .deleteCourse(ctrl.course)
                    .then(() => {
                        $state.transitionTo('app.education', {
                            educationId: ctrl.course.education.id
                        });
                    });
            }, () => {
            });
    };
}

app.component('course', {
    templateUrl: '/app/components/course.component.html',
    controller: courseController,
});