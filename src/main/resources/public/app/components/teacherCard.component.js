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
        $state.transitionTo("app.groupGrade", {groupId: ctrl.group.id});
    };

    ctrl.finalGroupView = () => {
        $state.transitionTo("app.finalGroupOverview", {groupId: ctrl.group.id});
    };

    ctrl.editGroup = (ev) => {
        $mdDialog.show({
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

function EditGroupDialogController($scope, $mdDialog) {
    $scope.hide = () => {
        $mdDialog.hide();
    };
    $scope.close = () => {
        $mdDialog.close();
    };
}

app.component('teacherCard', {
    templateUrl: '/app/components/teacherCard.component.html',
    controller: teacherCardCtrl,
    bindings: {
        group: '<',
    }
});