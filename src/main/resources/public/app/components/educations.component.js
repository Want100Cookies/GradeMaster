function educationsController($stateParams, $mdDialog, $state, EducationService) {
    let ctrl = this;

    ctrl.educations = [];

    ctrl.$onInit = () => {
        EducationService
            .getEducations()
            .then(educations => {
                ctrl.educations = educations;
            }, () => {
                $state.transitionTo('app.dashboard');
            });
    };

    ctrl.edit = education => {
        let dialog = $mdDialog
            .prompt()
            .title("Edit education")
            .textContent("Change the name of this education:")
            .initialValue(education.name)
            .ok("Save")
            .cancel("Cancel");

        $mdDialog
            .show(dialog)
            .then(name => {
                education.name = name;

                EducationService
                    .updateEducation(education)
                    .then(updated => {
                        angular.copy(updated, education);
                    }, error => {
                        console.log(error);
                        $mdDialog.show($mdDialog.alert()
                            .title("Error")
                            .textContent("Could not update this education...")
                            .ok("Okay"));
                    });
            });
    };

    ctrl.view = education => {
        $state.transitionTo("app.education", {
            educationId: education.id,
        });
    };

    ctrl.showAdd = () => {
        let dialog = $mdDialog
            .prompt()
            .title("Create education")
            .textContent("Education name:")
            .ok("Create")
            .cancel("Cancel");

        $mdDialog
            .show(dialog)
            .then(name => {
                EducationService
                    .createEducation(name)
                    .then(education => {
                        ctrl.educations.push(education);
                    }, () => {
                    });
            });
    };
}

app.component('educations', {
    templateUrl: '/app/components/educations.component.html',
    controller: educationsController,
});