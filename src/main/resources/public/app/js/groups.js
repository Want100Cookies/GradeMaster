app.controller('GroupsCtrl', function($scope){

});

app.controller('TeacherGroupsCtrl', function($scope, $mdDialog, UserService){
    $scope.status = '  ';
    $scope.userArray = [];
    $scope.showAddGroup = function(ev) {
        UserService.getAllUsers().then(function(response){
            $scope.userArray = response.data;
        });
        $mdDialog.show({
          controller: DialogController,
          templateUrl: '/app/dialogs/addGroupDialog.tmpl.html',
          parent: angular.element(document.body),
          targetEvent: ev,
          clickOutsideToClose:true,
          fullscreen: $scope.customFullscreen
        })
      };

      function DialogController($scope, $mdDialog) {
        $scope.hide = function() {
          $mdDialog.hide();
        };
    
        $scope.cancel = function() {
          $mdDialog.cancel();
        };
    
        $scope.answer = function(answer) {
          $mdDialog.hide(answer);
        };
      }
});

