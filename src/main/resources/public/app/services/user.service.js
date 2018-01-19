app.factory('UserService', function (API) {
    return {
        getSelf: function () {
            return API.get({
                path: `users/self`
            })
        },
        getAllUsers: function () {
            return API.get({
                path: `users`
            });
        },
    }
});