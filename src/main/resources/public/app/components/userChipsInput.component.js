
app.component('userChipsInput', {
    templateUrl: '/app/components/userChipsInput.component.html',
    controller: userChipsInputCtrl,
    transclude: true
});

function userChipsInputCtrl(UserService) {
    
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
}
