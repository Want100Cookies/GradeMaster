app.component('userChipsInput', {
    templateUrl: '/app/components/userChipsInput.component.html',
    controller: userChipsInputCtrl,
    bindings: {
        selectedUsers: '<',
    }
});

function userChipsInputCtrl(UserService, $scope) {
    this.userArray = [];
    this.users = [];
    
    $scope.$watchCollection('$ctrl.selectedUsers', (users) => {
        if(users) this.users = users;
    });

    $scope.$watchCollection('$ctrl.users', (users) => {
        $scope.$parent.usersChange(users);
    });

    UserService.getAllUsers().then((response) => {
        this.userArray = response.data;
    });

    this.search = (query) => {
        return query
            ? this.userArray.filter(this.createFilterFor(query))
            : [];
    };

    this.createFilterFor = (query) => {
        let lowercaseQuery = angular.lowercase(query);

        return function filterFn(user) {
            return (angular.lowercase(user.name)
                .indexOf(lowercaseQuery) !== -1);
        };
    };
}
