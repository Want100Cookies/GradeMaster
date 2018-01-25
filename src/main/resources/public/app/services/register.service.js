app.factory('RegisterService', function (API) {

    this.register = (data) => {
        return API.post({
            path: `users`,
            data
        });
    };

    return this;
});