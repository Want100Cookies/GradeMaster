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

    function DialogController($scope, $mdDialog) {
        $scope.vm = {
            formData: {
                groupName: '',
                period: [],
                startYear: '',
                endYear: '',
                course: {
                    id: ''
                },
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
            console.log($scope.vm.formData);
        }

        $scope.usersChange = (val) => {
            $scope.vm.formData.users = val;
            // TODO Set users in formdata (use ng-model i guess)
        };
    };
});
