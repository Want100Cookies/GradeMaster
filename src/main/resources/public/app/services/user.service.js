app.factory('UserService', function (API) {
    return {
        getSelf: () => {
            return API.get({
                path: `users/self`
            });
        },
        getAllUsers: () => {
            return API.get({
                path: `users`
            });
        },
    }
});