app.factory('NotificationService', function (API) {

    this.getNotifications = () => {
        return API.get({
            path: `notifications`
        });
    };

    this.readNotification = (notificationId) => {
        return API.patch({
            path: notificationId ? `notifications/${notificationId}` : `notifications`
        });
    };

    return this;
});