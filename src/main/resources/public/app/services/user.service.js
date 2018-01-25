app.factory('UserService', function (API) {

    this.getSelf = () => {
        return API.get({
            path: `users/self`
        });
    };

    this.getAllUsers = () => {
        return API.get({
            path: `users`
        });
    };

    return this;
});