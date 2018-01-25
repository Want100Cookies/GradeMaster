function teacherCardCtrl($scope, GroupService, $state, $mdDialog, API) {
    var ctrl = this;

    ctrl.$onInit = () => {
        ctrl.groupGrade = "TBD";
        ctrl.groupStatus = "TBD";

        GroupService.getGradingStatus(ctrl.group.id).then((response) => {
            ctrl.groupStatus = response.data.status;
        });
    };

    ctrl.gradeGroup = () => {
        $state.transitionTo("app.groupGrade", {
            groupId: ctrl.group.id
        });
    };

    ctrl.finalGroupView = () => {
        $state.transitionTo("app.finalGroupOverview", {
            groupId: ctrl.group.id
        });
    };

    ctrl.editGroup = (ev) => {
        $mdDialog.show({
            locals: {
                group: ctrl.group
            },
            bindToController: true,
            controller: EditGroupDialogController,
            templateUrl: '/app/dialogs/editGroupDialog.tmpl.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: true,
            fullscreen: $scope.customFullscreen
        }).then(function () {
            $scope.$parent.$parent.getGroups();
        }, function () {
            console.log("close dialog");
        });
    };

    ctrl.deleteGroup = () => {
        let confirm = $mdDialog.confirm()
            .title("Are you sure?")
            .textContent("Are your sure you want to delete this group? You cannot recover this group after deleting it!")
            .ok("Yes, I am sure I want to delete this group!")
            .cancel("No!");

        $mdDialog.show(confirm)
            .then(() => {
                GroupService.deleteGroup(ctrl.group.id).then(() => {
                    //Refresh groups
                    $scope.$parent.$parent.getGroups();
                }, () => {
                    $mdDialog.show($mdDialog.alert()
                        .title("Error")
                        .textContent("There was an error processing your request.")
                        .ok("Okay"));
                });
            });
    };

    ctrl.export = (format, contentType) => {
        API.get({
            path: `grades/groups/${ctrl.group.id}/export.${format}`,
            req: Object.assign({
                headers: {
                    'Content-type': contentType,
                },
                responseType: 'arraybuffer'
            }, API.getRequest())
        }).then((response) => {
            const file = new Blob([response.data], {
                type: contentType
            });

            const fileURL = URL.createObjectURL(file);
            const a = document.createElement('a');
            a.href = fileURL;
            a.target = '_blank';
            a.download = `${ctrl.group.groupName}.${format}`;
            document.body.appendChild(a);
            a.click();
        }).catch((error) => {
            console.error("Error downloading export");
        });
    };

    ctrl.downloadPDF = () => {
        ctrl.export("pdf", "application/pdf");
    };

    ctrl.downloadCSV = () => {
        ctrl.export("csv", "text/csv");
    };
}

function EditGroupDialogController($scope, $mdDialog, EducationService, group, GroupService, $mdToast) {
    $scope.group = group;
    $scope.chosenEducation = group.course.education.id;
    $scope.courseOptions = [];
    $scope.educationOptions = [];

    EducationService.getEducations().then((response) => {
        $scope.educationOptions = response.data;
    })
    $scope.$watch('chosenEducation', () => {
        $scope.courseOptions = null;
        if ($scope.chosenEducation !== null) {
            EducationService.getCoursesByEducation($scope.chosenEducation).then((response) => {
                $scope.courseOptions = response.data;
            })
        }
    });

    $scope.vm = {
        formData: {
            groupName: group.groupName,
            startYear: group.startYear,
            endYear: group.endYear,
            users: group.users,
            course: {
                id: group.course.id,
                education: {
                    id: group.course.education.id
                }
            },
            period: group.period,
        },
    };
    $scope.usersChange = (val) => {
        $scope.vm.formData.users = val;
    };

    $scope.hide = () => {
        $mdDialog.hide();
    };
    $scope.cancel = () => {
        $mdDialog.cancel();
    };
    $scope.showSimpleToast = () => {
        $mdToast.show(
            $mdToast.simple()
            .textContent('Edited Group!')
            .hideDelay(3000)
        );
    };
    $scope.edit = () => {
        if (Object.keys($scope.vm.formData.period).length !== 0 && $scope.vm.formData.users.length !== 0 &&
            $scope.vm.formData.groupName != null && $scope.vm.formData.startYear != null && $scope.vm.formData.endYear != null &&
            $scope.vm.formData.course != null) {
            GroupService.editGroup($scope.vm.formData, $scope.group.id);
            GroupService.editGroupUsers($scope.vm.formData.users, $scope.group.id);
            $scope.showSimpleToast();
            $scope.hide();
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