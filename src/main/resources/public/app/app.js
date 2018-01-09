'use strict';
var app = angular.module('gmApp', [
    'ngMaterial'
]);

app.controller('LayoutController', function($scope, $mdSidenav){
    $scope.openSideNav = function () {
        $mdSidenav('left').open();
      };
})