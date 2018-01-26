app.factory('VerifyService', function (API) {

    this.verify = (data) => {
        return API.patch({
            path: `auth/verify`,
            data,
            req: Object.assign({}, API.getUnAuthRequest())
        });
    };

    return this;
});