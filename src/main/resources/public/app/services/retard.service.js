app.factory('RetardService', function (API) {

    this.requestForgot = (data) => {
        return API.post({
            path: `auth/retard`,
            data,
            req: Object.assign({}, API.getUnAuthRequest())
        });
    };

    this.setNewPassword = (data) => {
        return API.patch({
            path: `auth/retard`,
            data,
            req: Object.assign({}, API.getUnAuthRequest())
        });
    };

    return this;
});