app.controller('GroupsCtrl', function ($scope) {

});

app.controller('TeacherGroupsCtrl', function ($scope, $mdDialog, UserService) {
    $scope.status = '  ';

    $scope.showAddGroup = function (ev) {
        $mdDialog.show({
            controller: DialogController,
            templateUrl: '/app/dialogs/addGroupDialog.tmpl.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: true,
            fullscreen: $scope.customFullscreen
        })
    };

    function DialogController($scope, $mdDialog, GroupService) {
        $scope.vm = {
            formData: {
                groupName: '',
                startYear: '',
                endYear: '',
                users: [
    
                ]
            },
        };

        $scope.hide = function () {
            $mdDialog.hide();
        };

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.create = function() {
            if ($scope.vm.formData.groupName != null && $scope.vm.formData.startYear != null && $scope.vm.formData.startYear)  {
                GroupService.createGroup($scope.vm.formData);
                $mdDialog.cancel();
            }
        }

        $scope.usersChange = (val) => {
            $scope.vm.formData.users = val;
            // TODO Set users in formdata (use ng-model i guess)
        };
    };
});
