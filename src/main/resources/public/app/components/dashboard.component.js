function DashboardCtrl($scope, StudentGroupsService, UserService){
    var ctrl = this;

    $scope.init = function(){
        ctrl.closed = 0;
        ctrl.open = 0;
        ctrl.pending = 0;

        UserService.getSelf().then(user => {
            ctrl.self = user;

            StudentGroupsService.getStudentGroups(ctrl.self.data.id).then(function(response){
                ctrl.groupsDetails = response.data;

                angular.forEach(ctrl.groupsDetails, function(value, key) {
                    StudentGroupsService.getGradingStatus(value.id).then(function(response){
                        if(response.data.status == "CLOSED"){
                            ctrl.closed += 1;
                        }else if(response.data.status == "OPEN"){
                            ctrl.open += 1;
                        }else if(response.data.status == "PENDING"){
                            ctrl.pending += 1;
                        }
                    })
                });
            });
        });
    }
}

app.component('dashboardCourseOverview', {
    templateUrl: '/app/components/dashboardCourseOverview.component.html',
    controller: GroupCardCtrl,
    bindings: {
        user: '<',
        group: '<',
    }
})
.component('dashboardProjectsOverview', {
    templateUrl: '/app/components/dashboardProjectsOverview.component.html',
    controller: DashboardCtrl
})
.component('dashboardGradeOverview', {
    templateUrl: '/app/components/dashboardGradeOverview.component.html',
    controller: GroupCardCtrl,
    bindings: {
        user: '<',
        group: '<',
    }
});