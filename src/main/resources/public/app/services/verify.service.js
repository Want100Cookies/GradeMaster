app.factory('VerifyService', function (API) {

    this.verify = (data) => {
        return API.patch({
            path: `auth/verify`,
            data
        });
    };

    return this;
});