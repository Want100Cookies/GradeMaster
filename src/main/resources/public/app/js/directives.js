app.directive('activeLink', ['$location', function (location) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs, controller) {
            var clazz = attrs.activeLink;
            var path = attrs.href;
            path = path.substring(2);
            scope.location = location;
            scope.$watch('location.path()', function (newPath) {
                if (path === newPath) {
                    element.addClass(clazz);
                } else {
                    element.removeClass(clazz);
                }
            });
        }
    };
}]);

app.directive('activeUser', function (AuthService) {
    return {
        link: function (scope, element, attrs, controller, rejection) {
            var clazz = attrs.activeUser;
            Auth = AuthService.authenticate().then(function(data){
                element.removeClass(clazz);
            }).catch(function() {
                element.addClass(clazz);
                console.log("catch");
            })
            // if(Auth) {

            // } else if(rejection === 'Not Authenticated'){
            //     console.log("test")
            // }
        }
    }
});