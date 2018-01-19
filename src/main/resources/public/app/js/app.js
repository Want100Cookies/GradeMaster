'use strict'
var app = angular.module('gmApp', [
    'ngMaterial',
    'ngMessages',
    'ngAnimate',
    'ngResource',
    'ngCookies',
    'ui.router'
]);

app.controller('LayoutController', function ($scope, $mdSidenav, $window, $cookies, AuthService) {
    AuthService.hasRoles("ADMIN_ROLE").then((hasRole) => {
        $scope.isAdmin = hasRole;
    });
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
    };
});

app.config(function ($stateProvider) {
    $stateProvider
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
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.dashboard', {
            url: '/dashboard',
            templateUrl: '/app/pages/dashboard.html',
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.groups', {
            url: '/groups',
            templateProvider: function (AuthService) {
                return AuthService.hasRoles('ADMIN_ROLE', 'TEACHER_ROLE').then((hasRole) => {
                    if (hasRole) {
                        return '<div ng-include="\'/app/pages/teacher-groups.html\'"></div>';
                    } else {
                        return '<div ng-include="\'/app/pages/groups.html\'"></div>';
                    }
                })
            },
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            },
            controller: 'GroupsCtrl'
        })
        .state('app.grading', {
            url: '/groups/:groupId/grading',
            component: 'grading',
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            },
        })
        .state('app.grades', {
            url: '/grades',
            templateProvider: function (AuthService) {
                return AuthService.hasRoles('ADMIN_ROLE', 'TEACHER_ROLE').then((hasRole) => {
                    if (hasRole) {
                        return '<div ng-include="\'/app/pages/teacher-grades.html\'"></div>';
                    } else {
                        return '<div ng-include="\'/app/pages/grades.html\'"></div>';
                    }
                })
            },
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            },
            controller: 'GradesCtrl'
        })
        .state('app.educations', {
            url: '/educations',
            component: 'educations',
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            },
        })
        .state('app.education', {
            url: '/educations/:educationId',
            component: 'education',
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            },
        })
        .state('app.course', {
            url: '/courses/:courseId',
            component: 'course',
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            },
        })
        .state('app.groupGrade', {
            url: '/groups/:groupId/group-grade',
            component: 'groupGrade',
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            },
        });
});