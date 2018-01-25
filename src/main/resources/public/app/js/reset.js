app.controller('ResetCtrl', function (RetardService, $state, $stateParams, $timeout) {

    this.submit = () => {
        this.loading = true;
        this.error = false;
        this.success = false;
        RetardService.setNewPassword({
            email: this.email,
            password: this.password,
            token: $stateParams.token
        }).then((resp) => {
            this.loading = false;
            this.success = true;
            $timeout(() => {
                $state.transitionTo('login');
            }, 3000);
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