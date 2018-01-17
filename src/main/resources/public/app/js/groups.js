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

app.controller('ChipCtrl', function(UserService){
    var self = this;
    self.userArray = [];
    self.SelectedStudents = [];
    UserService.getAllUsers().then(function (response) {
        self.userArray = response.data;
    });

    self.Search = function (query) {
        var results = query
            ? self.userArray.filter(createFilterFor(query))
            : [];

        return results;
    }

    function createFilterFor(query) {
        var lowercaseQuery = angular.lowercase(query);

        return function filterFn(user) {
            return (angular.lowercase(user.name)
                .indexOf(lowercaseQuery) != -1);
        };
    }
})
