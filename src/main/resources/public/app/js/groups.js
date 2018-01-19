app.controller('GroupsCtrl', function ($scope) {

});

app.controller('TeacherGroupsCtrl', function ($scope, $mdDialog, UserService, GroupService) {
    $scope.status = '  ';
    $scope.onInit = () => {
        $scope.getGroups();
    }
    $scope.getGroups = () => {
        return GroupService.getGroups().then((response) => {
            console.log(response);
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
        var last = {
            bottom: false,
            top: true,
            left: false,
            right: true
          };
      
        $scope.toastPosition = angular.extend({},last);
      
        $scope.getToastPosition = function() {
          sanitizePosition();
      
          return Object.keys($scope.toastPosition)
            .filter(function(pos) { return $scope.toastPosition[pos]; })
            .join(' ');
        };
      
        function sanitizePosition() {
          var current = $scope.toastPosition;
      
          if ( current.bottom && last.top ) current.top = false;
          if ( current.top && last.bottom ) current.bottom = false;
          if ( current.right && last.left ) current.left = false;
          if ( current.left && last.right ) current.right = false;
      
          last = angular.extend({},current);
        }

        $scope.showSimpleToast = () => {
            var pinTo = $scope.getToastPosition();
            $mdToast.show(
                $mdToast.simple()
                    .textContent('Simple Toast!')
                    .position(pinTo)
                    .hideDelay(3000)
            );
        };

        $scope.hide = () => {
            $mdDialog.hide();
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
                $mdDialog.cancel();
            }
        }
        $scope.usersChange = (val) => {
            $scope.vm.formData.users = val;
        };
    }
});
