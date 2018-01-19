app.factory('UserService', function (API) {
    return {
        getAllUsers: function () {
            return API.get({
                path: `users`
            });
        }
    }
});