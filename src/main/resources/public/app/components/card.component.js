function CardCtrl($scope, StudentGroupsService){
    var ctrl = this;

    $scope.init = function(){

        StudentGroupsService.getGroupMembers($scope.$ctrl.group).then(function(response){
            $scope.groupMembers = response.data;
            console.log(response.data);
        });

        console.log($scope.$ctrl.group);
    }
}

app.component('card', {
    templateUrl: '/app/components/card.component.html',
    controller: CardCtrl,
    bindings: {
        group: '<',
    }
});