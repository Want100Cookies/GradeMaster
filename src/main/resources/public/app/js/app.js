'use strict'
var app = angular.module('gmApp', [
    'ngMaterial',
    'ngMessages',
    'ngAnimate',
    'ngResource',
    'ngCookies',
    'ui.router'
]);

app.controller('LayoutController', function ($scope, $mdSidenav, $window, $cookies, $mdDialog, AuthService, NotificationService, UserService) {

    this.newNotifications = [];
    this.oldNotifications = [];
    this.patchNotifications = [];
    this.user = {};

    (this.getNotifications = () => {
        return NotificationService.getNotifications().then((resp) => {
            this.newNotifications = [];
            this.oldNotifications = [];
            this.patchNotifications = [];
            resp.data.forEach(n => {
                if (!n.seen) this.newNotifications.push(n);
                else this.oldNotifications.push(n);
            });
        });
    })();

    this.hasNewNotifications = () => {
        if (this.newNotifications.length > 0) return true;
        return false;
    }

    AuthService.hasRoles("ADMIN_ROLE").then((hasRole) => {
        $scope.isAdmin = hasRole;
    });

    this.openSideNav = () => {
        $mdSidenav('left').open();
    };
    this.closeSideNav = () => {
        $mdSidenav('left').close();
    };
    this.showNotifications = ($mdOpenMenu, ev) => {
        this.getNotifications().then((resp) => {
            $mdOpenMenu(ev);
        });
    }
    this.showOldNotifications = ($mdOpenMenu, ev) => {
        this.getNotifications().then((resp) => {
            $mdOpenMenu(ev);
        });
    }
    this.showAccount = ($mdOpenMenu, ev) => {
        $mdOpenMenu(ev);
    }
    (this.getUser = () => {
        return UserService.getSelf().then((resp) => {
            this.user = resp.data;
            return resp.data;
        });
    })();
    this.readNotification = (notification) => {
        if (!notification.seen) {
            notification.seen = true;
            this.patchNotifications.push(notification);
        }
    }
    this.readAllNotifications = () => {
        NotificationService.readNotification().then(() => {
            this.getNotifications();
        });
    }
    $scope.$on("$mdMenuClose", () => {
        const promises = this.patchNotifications.map(n => NotificationService.readNotification(n.id));
        Promise.all(promises).then((results) => {
            this.getNotifications();
        });
    });
    this.logout = () => {
        angular.forEach($cookies.getAll(), (v, k) => {
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
        .state('retard', {
            url: '/retard',
            templateUrl: '/app/pages/retard.html',
            controller: 'RetardCtrl'
        })
        .state('reset', {
            url: '/reset?token',
            templateUrl: '/app/pages/reset.html',
            controller: 'ResetCtrl'
        })
        .state('verify', {
            url: '/verify?email&token',
            templateUrl: '/app/pages/verify.html',
            controller: 'VerifyCtrl'
        })
        .state('app', {
            url: '',
            templateUrl: '/app/pages/app.html',
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            }
        })
        .state('app.dashboard', {
            url: '/dashboard',
            templateProvider: function (AuthService) {
                return AuthService.hasRoles('STUDENT_ROLE').then((hasRole) => {
                    if (hasRole) {
                        return '<div ng-include="\'/app/pages/dashboard.html\'"></div>';
                    } else {
                        return AuthService.hasRoles('TEACHER_ROLE').then((hasRole) => {
                            if (hasRole) {
                                return '<div ng-include="\'/app/pages/dashboard-teacher.html\'"></div>';
                            } else {
                                return AuthService.hasRoles('ADMIN_ROLE').then((hasRole) => {
                                    if (hasRole) {
                                        return '<div ng-include="\'/app/pages/dashboard-admin.html\'"></div>';
                                    }
                                })
                            }
                        })
                    }
                })
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
        })
        .state('app.finalGroupOverview', {
            url: '/groups/:groupId/final-overview',
            component: 'finalGroupOverview',
            resolve: {
                'auth': (AuthService) => {
                    return AuthService.authenticate();
                },
            },
        })
        .state('app.empty', {
            component: 'empty'
        });
});