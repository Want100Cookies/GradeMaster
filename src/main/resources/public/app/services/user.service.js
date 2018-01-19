app.factory('UserService', function (API) {
    return {
        getAllUsers: function () {
            return API.get({
                path: `users`
            });
        },
        getUser: function () {
            return API.get({
                path: `users/self`
            })
        }
    }
});