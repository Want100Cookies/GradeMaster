'use strict';
var app = angular.module('gmApp', [
    'ngMaterial',
    'ngMessages',
    'ngAnimate',
    'ngRoute',
    'ngResource',
    'ngCookies',
    'ui.router'
]);

app.controller('LayoutController', function ($scope, $mdSidenav) {
    $scope.openSideNav = function () {
        $mdSidenav('left').open();
    };
    $scope.closeSideNav = function () {
        $mdSidenav('left').close();
    };
});

app.config(function ($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('root', {
            url: '',
            templateUrl: '/app/pages/app.html',
            resolve: {
                'auth': function(AuthService){
                    return AuthService.authenticate();
                },
            }
        })
        .state('login', {
            url: '/login',
            templateUrl: '/app/pages/login.html',
            controller: 'LoginCtrl'
        })
        .state('app', {
            url: '/home',
            templateUrl: '/app/pages/app.html',
            resolve: {
                'auth': function(AuthService){
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.dashboard', {
            url: '/dashboard',
            templateUrl: '/app/pages/dashboard.html',
            resolve: {
                'auth': function(AuthService){
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.groups', {
            url: '/groups',
            templateUrl: '/app/pages/groups.html',
            resolve: {
                'auth': function(AuthService){
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.grades', {
            url: '/grades',
            templateUrl: '/app/pages/grades.html',
            resolve: {
                'auth': function(AuthService){
                    return AuthService.authenticate();
                },
            }
        });
});