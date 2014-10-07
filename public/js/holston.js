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

holstonControllers.controller('TastingController', ['$scope', '$routeParams', '$http',
    function ($scope, $routeParams, $http) {
        //TODO for some reason 200 ok is always received even if we call /plaa/plaa
        $http.get('/api/beers').
            success(function(data) {
                $scope.beers = _.pluck(data.beers, 'name')
            }).
            error(function(data, status, headers, config) {
                console.log('ERROR', status)
            });
        $scope.tastingId = $routeParams.tastingId
        $scope.onType = function(x) { console.log('onType', x) }
    }]);

holstonControllers.controller('TastingListController', ['$scope',
    function($scope) {
        $scope.bar = [1,2,3,4]
    }]);
