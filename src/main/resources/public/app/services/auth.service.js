app.factory('AuthService', function (API, $state) {

    this.authenticate = () => {
        return API.get({
            path: `users/self`
        }).then((resp) => {
            return true;
        }).catch((error) => {
            $state.transitionTo('login');
        });
    };

    this.hasRoles = (...roles) => {
        return API.get({
            path: `users/self`
        }).then((resp) => {
            for(const role of roles) {
                for(const respRole of resp.data.roles) {
                    if(respRole.code === role) return true;
                }
            }
            return false;
        }).catch((error) => {
            return false;
        });
    }

    return this;
});