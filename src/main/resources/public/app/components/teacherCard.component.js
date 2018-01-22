teacherCardCtrl = ($scope, GroupService, $state, $mdDialog) => {
    var ctrl = this;

    $scope.init = () => {
        $scope.groupGrade = "TBD";
        $scope.groupStatus = "TBD";
        $scope.groupName;
        $scope.groupMembers = [];
        $scope.groupId = $scope.$ctrl.group.id;

        GroupService.getGradingStatus($scope.$ctrl.group.id).then((response) => {
            $scope.groupStatus = response.data.status;
        });

        // Check the group grade
        if ($scope.$ctrl.group.groupGrade !== null) {
            $scope.groupGrade = $scope.$ctrl.group.groupGrade.grade;
        }
        // Check the group name
        if ($scope.$ctrl.group !== null) {
            $scope.groupName = $scope.$ctrl.group.groupName;
        } else {
            $scope.groupName = "Couldn't resolve group name";
        }
        // Check the group members
        if ($scope.$ctrl.group.users !== null) {
            $scope.groupMembers = $scope.$ctrl.group.users;
        }
    }

    $scope.gradeGroup = () => {
        $state.transitionTo("app.groupGrade", {groupId: $scope.groupId});
    }
    $scope.finalGroupView = () => {
        $state.transitionTo("app.finalGroupOverview", {groupId: $scope.groupId});
    }
     $scope.editGroup = (ev) => {
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
    }

    $scope.deleteGroup = () => {
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

function EditGroupDialogController($scope, $mdDialog, GroupService) {
    $scope.hide = () => {
        $mdDialog.hide();
    }
    $scope.close = () => {
        $mdDialog.close();
    }
}
app.component('teacherCard', {
    templateUrl: '/app/components/teacherCard.component.html',
    controller: teacherCardCtrl,
    bindings: {
        group: '<',
    }
});