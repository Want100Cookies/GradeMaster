function teacherCardCtrl($scope, GroupService, $state, $mdDialog) {
    var ctrl = this;

    ctrl.$onInit = () => {
        ctrl.groupGrade = "TBD";
        ctrl.groupStatus = "TBD";

        GroupService.getGradingStatus(ctrl.group.id).then((response) => {
            ctrl.groupStatus = response.data.status;
        });
    };

    ctrl.gradeGroup = () => {
        $state.transitionTo("app.groupGrade", {groupId: $scope.groupId});
    };

    ctrl.finalGroupView = () => {
        $state.transitionTo("app.finalGroupOverview", {groupId: $scope.groupId});
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
                GroupService.deleteGroup($scope.groupId).then(() => {
                    //Refresh groups
                    $scope.$parent.$parent.getGroups();
                }, () => {
                    $mdDialog.show($mdDialog.alert()
                        .title("Error")
                        .textContent("There was an error processing your request.")
                        .ok("Okay"));
                });
            });
    }
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