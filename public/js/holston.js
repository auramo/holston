var holstonApp = angular.module('holstonApp', [
    'ngRoute',
    'autocomplete',
    'editTasting',
    'tastingList'
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
