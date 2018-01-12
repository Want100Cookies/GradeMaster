'use strict';
var app = angular.module('gmApp', [
    'ngMaterial',
    'ngRoute'
]);

app.controller('LayoutController', function ($scope, $mdSidenav) {
    $scope.openSideNav = function () {
        $mdSidenav('left').open();
    };
    $scope.closeSideNav = function () {
        $mdSidenav('left').close();
    };
});

app.config(function ($routeProvider){
    $routeProvider
    .when('/', {
        
    })
    .when('/login', {

    })
    .when('/groups', {

    })
    .when('/grades', {

    });
});

app.directive('activeLink', ['$location', function (location) {
    return{
        restrict: 'A',
        link: function(scope, element, attrs, controller){
            var clazz = attrs.activeLink;
            var path = attrs.href;
            path = path.substring(2);
            scope.location = location;
            scope.$watch('location.path()', function (newPath){
                if(path === newPath) {
                    element.addClass(clazz);
                } else {
                    element.removeClass(clazz);
                }
            });
        }
    };
}]);