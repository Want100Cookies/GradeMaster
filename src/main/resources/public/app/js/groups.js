app.controller('GroupsCtrl', function ($scope) {

});

app.controller('TeacherGroupsCtrl', function ($scope, $mdDialog, UserService) {
    $scope.status = '  ';
    $scope.vm = {
        formData: {
            groupName: '',
            period: [],
            startYear: '',
            endYear: '',
            course: {
                id: ''
            },
            SelectedStudents: [

            ]

        
        },
    }
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
        $scope.hide = function () {
            $mdDialog.hide();
        };

        $scope.cancel = function () {
            $mdDialog.cancel();
        };
    }
});
