app.controller('GroupsCtrl', function ($scope) {

});

app.controller('StudentGroupsCtrl', function ($scope, UserService, StudentGroupsService) {

    UserService.getSelf().then(function (response) {
        $scope.userDetails = response.data;
        StudentGroupsService.getStudentGroups($scope.userDetails.id).then(function (response) {
            $scope.groupsDetails = response.data;
        });
    });
});

app.controller('TeacherGroupsCtrl', function ($scope, $mdDialog, UserService, GroupService) {
    $scope.status = '  ';
    $scope.teacherGroupList = [];
    $scope.onInit = () => {
        $scope.getGroups();
    }
    $scope.getGroups = () => {
        UserService.getSelf().then((response) => {
                return GroupService.getGroupsByUserId(response.data.id).then((response) => {
                    $scope.teacherGroupList = response.data;
                });
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
                EducationService.getCoursesByEducation($scope.chosenEducation).then((response) => {
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
                period: []
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
            if (Object.keys($scope.vm.formData.period).length !== 0 && $scope.vm.formData.users.length !== 0 &&
                $scope.vm.formData.groupName != null && $scope.vm.formData.startYear != null && $scope.vm.formData.endYear != null &&
                $scope.vm.formData.course != null) {
                GroupService.createGroup($scope.vm.formData);
                $scope.showSimpleToast();
                $scope.cancel();
            }
        }
        $scope.usersChange = (val) => {
            $scope.vm.formData.users = val;
        };
    };
});