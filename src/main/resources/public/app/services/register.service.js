app.factory('RegisterService', function (API) {

    this.register = (data) => {
        return API.post({
            path: `users`,
            data,
            req: Object.assign({}, API.getUnAuthRequest())
        });
    };

    return this;
});