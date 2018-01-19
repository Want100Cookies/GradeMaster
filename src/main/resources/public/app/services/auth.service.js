app.factory('AuthService', function ($cookies, $q, $location, $resource, $http, $httpParamSerializer, $state, UserService) {
    return {
        authenticate: function () {
            console.log(UserService.getSelf());
            return UserService
                .getSelf()
                .then(() => {
                    return true;
                })
                .catch(() => {
                    $state.transitionTo('login');
                    return false;
                });
        },
        hasRoles: (...roles) => {
            return UserService
                .getSelf()
                .then(user => {
                    for (const role of roles) {
                        for (const respRole of user.roles) {
                            if (respRole.code === role) return true;
                        }
                    }
                    return false;
                })
                .catch(() => {
                    return false;
                });
        }
    }
});