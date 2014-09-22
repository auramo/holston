var holstonApp = angular.module('holstonApp', [
    'ngRoute',
    'autocomplete',
    'holstonControllers'
]);

holstonApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/editTasting/:id?', {
                templateUrl: 'partials/edit-tasting.html',
                controller: 'TastingController'
            }).
            when('/tastings', {
                templateUrl: 'partials/list-tastings.html',
                controller: 'TastingListController'
            }).
            otherwise({
                redirectTo: '/tastings'
            });
}]);

var holstonControllers = angular.module('holstonControllers', []);

holstonControllers.controller('TastingController', ['$scope', '$http', '$routeParams',
    function ($scope, $routeParams) {
        $scope.foo = ['eka', 'toka']
        $scope.beers = ['Lappari', 'AleKokki']
        $scope.tastingId = $routeParams.tastingId
        $scope.onType = function(x) { console.log('onType', x) }
    }]);

holstonControllers.controller('TastingListController', ['$scope',
    function($scope) {
        $scope.bar = [1,2,3,4]
    }]);
