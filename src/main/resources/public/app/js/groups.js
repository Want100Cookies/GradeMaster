app.controller('GroupsCtrl', function ($scope) {

});

app.controller('TeacherGroupsCtrl', function ($scope, $mdDialog, UserService, GroupService) {
    $scope.status = '  ';
    $scope.onInit = () => {
        $scope.getGroups();
    }
    $scope.getGroups = () => {
        return GroupService.getGroups().then((response) => {
            
        });
    }
    $scope.showAddGroup = (ev) => {
        $mdDialog.show({
            controller: DialogController,
            templateUrl: '/app/dialogs/addGroupDialog.tmpl.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: true,
            fullscreen: $scope.customFullscreen
        })
    };

    function DialogController($scope, $mdDialog, GroupService, EducationService, $mdToast) {
        $scope.chosenEducation = null;
        $scope.courseOptions = [];
        $scope.educationOptions = [];
        EducationService.getEducations().then((response) => {
            $scope.educationOptions = response.data;
        })
        $scope.$watch('chosenEducation', () => {
            $scope.courseOptions = null;
            if ($scope.chosenEducation !== null) {
                EducationService.getCoursesByEducation($scope.chosenEducation).then( (response) => {
                    $scope.courseOptions = response.data;
                })
            }
        });
        $scope.vm = {
            formData: {
                groupName: '',
                startYear: '',
                endYear: '',
                users: [

                ],
                course: {},
                periods: []
            },
        };
        $scope.showSimpleToast = () => {
            $mdToast.show(
                $mdToast.simple()
                    .textContent('Created Group!')
                    .hideDelay(3000)
            );
        };
        $scope.cancel = () => {
            $mdDialog.cancel();
        };
        $scope.create = () => {
            if (Object.keys($scope.vm.formData.periods).length !== 0 && $scope.vm.formData.users.length !== 0
                && $scope.vm.formData.groupName != null && $scope.vm.formData.startYear != null && $scope.vm.formData.endYear != null
                && $scope.vm.formData.course != null) {
                GroupService.createGroup($scope.vm.formData);
                $scope.showSimpleToast();
                $scope.cancel();
            }
        }
        $scope.usersChange = (val) => {
            $scope.vm.formData.users = val;
        };
    }
});
