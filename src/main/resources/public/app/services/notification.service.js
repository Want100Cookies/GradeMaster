app.factory('NotificationService', function (API) {
    return {
        getNotifications: () => {
            return API.get({
                path: `notifications`
            });
        },
        readNotification: (notificationId) => {
            return API.patch({
                path: notificationId ? `notifications/${notificationId}` : `notifications`
            });
        },
    }
});