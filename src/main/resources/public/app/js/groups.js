app.controller('GroupsCtrl', function ($scope) {

});

app.controller('StudentGroupsCtrl', function($scope, UserService, StudentGroupsService){

    UserService.getSelf().then(function(response){
        $scope.userDetails = response.data;
        StudentGroupsService.getStudentGroups($scope.userDetails.id).then(function(response){
            $scope.groupsDetails = response.data;
        });
    });
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

    function DialogController($scope, $mdDialog, GroupService, EducationService) {
        $scope.chosenEducation = null;
        $scope.courseOptions = [];
        $scope.educationOptions = [];
        EducationService.getEducations().then(function (response) {
            $scope.educationOptions = response.data;
        })
        $scope.$watch('chosenEducation', function () {
            if ($scope.chosenEducation !== null) {
                EducationService.getCoursesByEducation($scope.chosenEducation).then(function (response) {
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

        $scope.hide = function () {
            $mdDialog.hide();
        };

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.create = function () {
            if (Object.keys($scope.vm.formData.periods).length !== 0 && $scope.vm.formData.users.length !== 0
            && $scope.vm.formData.groupName != null && $scope.vm.formData.startYear != null && $scope.vm.formData.endYear != null
            && $scope.vm.formData.course != null) {
                GroupService.createGroup($scope.vm.formData);
                $mdDialog.cancel();
            }
        }
        $scope.usersChange = (val) => {
            $scope.vm.formData.users = val;
        };
    };
});

