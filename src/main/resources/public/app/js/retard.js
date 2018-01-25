app.controller('RetardCtrl', function (RetardService, $state) {

    this.changeLogin = () => {
        $state.transitionTo('login');
    }

    this.submit = () => {
        this.loading = true;
        this.error = false;
        this.success = false;
        RetardService.requestForgot({
            email: this.email
        }).then((resp) => {
            this.loading = false;
            this.success = true;
        }).catch((error) => {
            this.loading = false;
            this.error = true;
        });
    }
    
    this.showLoading = () => {
        return this.loading;
    }

    this.showForm = () => {
        return !this.loading && !this.success;
    }

    this.showSuccess = () => {
        return this.success;
    }

    this.showError = () => {
        return this.error;
    }

});