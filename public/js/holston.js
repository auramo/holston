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
    $scope.currentUser = ""
    AuthService.userStatus().then(function (status) { $scope.currentUser = status })
    $scope.userRoles = ['user', 'admin']
    $scope.isAuthorized = false//AuthService.isAuthorized
}]);
