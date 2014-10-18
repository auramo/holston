var holstonApp = angular.module('holstonApp', [
    'ngRoute',
    'autocomplete',
    'editTasting',
    'tastingList',
    'authentication'
]);

holstonApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/editTasting/:id?', {
                templateUrl: 'partials/edit-tasting.html',
                controller: 'EditTastingController'
            }).
            when('/tastings', {
                templateUrl: 'partials/list-tastings.html',
                controller: 'TastingListController'
            }).
            otherwise({
                redirectTo: '/tastings'
            });
}]);

holstonApp.controller('ApplicationController', ['$scope', 'AuthService', function($scope, AuthService) {
    $scope.currentUserEmail = ""
    $scope.loggedIn = false
    AuthService.userStatus().then(function (status) {
        $scope.currentUserEmail = status.email
        $scope.loggedIn = status.loggedIn
    })
    $scope.userRoles = ['user', 'admin']
}]);
