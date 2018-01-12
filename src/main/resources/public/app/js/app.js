'use strict';
var app = angular.module('gmApp', [
    'ngMaterial',
    'ngRoute',
    'ngResource',
    'ngCookies'
]);

app.controller('LayoutController', function ($scope, $mdSidenav) {
    $scope.openSideNav = function () {
        $mdSidenav('left').open();
    };
    $scope.closeSideNav = function () {
        $mdSidenav('left').close();
    };
});

app.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: '/app/pages/dashboard.html',
            resolve: {
                'auth': function(AuthService){
                    return AuthService.authenticate();
                },
            }
        })
        .when('/login', {
            templateUrl: '/app/pages/login.html',
            controller: 'LoginCtrl',
        })
        .when('/groups', {
            templateUrl: '/app/pages/groups.html',
            resolve: {
                'auth': function(AuthService){
                    return AuthService.authenticate();
                }
            }
        })
        .when('/grades', {
            templateUrl: '/app/pages/grades.html',
            resolve: {
                'auth': function(AuthService){
                    return AuthService.authenticate();
                }
            }
        });
});
app.run(function($rootScope, $location){
    //If the route change failed due to authentication error, redirect them out
    $rootScope.$on('$routeChangeError', function(event, current, previous, rejection){
        if(rejection === 'Not Authenticated'){
            $location.path('/login');
        }
    })
});
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