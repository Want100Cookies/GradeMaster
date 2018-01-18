'use strict';
var app = angular.module('gmApp', [
    'ngMaterial',
    'ngMessages',
    'ngAnimate',
    'ngResource',
    'ngCookies',
    'ui.router'
]);

app.controller('LayoutController', function ($scope, $mdSidenav, $window, $cookies) {
    $scope.openSideNav = function () {
        $mdSidenav('left').open();
    };
    $scope.closeSideNav = function () {
        $mdSidenav('left').close();
    };
    $scope.logout = function () {
        angular.forEach($cookies.getAll(), function (v, k) {
            $cookies.remove(k);
        });
        $window.location.reload();
    }
});

app.config(function ($stateProvider) {
    $stateProvider
        .state('root', {
            url: '',
            templateUrl: '/app/pages/app.html',
            resolve: {
                'auth': function (AuthService) {
                    return AuthService.authenticate();
                },
            }
        })
        .state('login', {
            url: '/login',
            templateUrl: '/app/pages/login.html',
            controller: 'LoginCtrl'
        })
        .state('register', {
            url: '/register',
            templateUrl: '/app/pages/register.html',
            controller: 'RegisterCtrl'
        })
        .state('registered', {
            url: '/registered',
            templateUrl: '/app/pages/registered.html',
        })
        .state('verify', {
            url: '/verify?email&token',
            templateUrl: '/app/pages/verify.html',
            controller: 'VerifyCtrl'
        })
        .state('app', {
            url: '/home',
            templateUrl: '/app/pages/app.html',
            resolve: {
                'auth': function (AuthService) {
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.dashboard', {
            url: '/dashboard',
            templateUrl: '/app/pages/dashboard.html',
            resolve: {
                'auth': function (AuthService) {
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.groups', {
            url: '/groups',
            templateUrl: '/app/pages/groups.html',
            resolve: {
                'auth': function (AuthService) {
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.grading', {
            url: '/groups/:groupId/grading',
            component: 'grading',
            resolve: {
                'auth': function (AuthService) {
                    return AuthService.authenticate();
                },
            }
        });
});