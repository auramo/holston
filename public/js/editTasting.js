angular.module('editTasting', []).controller('EditTastingController', ['$scope', '$routeParams', '$http', '$location',
    function ($scope, $routeParams, $http, $location) {
        //TODO for some reason 200 ok is always received even if we call /plaa/plaa
        $scope.tasting = {}
        $scope.addingNewBeer = true
        $http.get('/api/beers').
            success(function(data) {
                $scope.beers = _.pluck(data.beers, 'name')
            }).
            error(function(data, status, headers, config) {
                console.log('ERROR', status)
            });
        $scope.tastingId = $routeParams.tastingId
        $scope.onBeerSelect = function(beer) {
            $scope.addingNewBeer = !_.contains($scope.beers, beer)
        }
        $scope.save = save
        function save(tasting) {
            $http.post('/api/tastings', tasting).
                success(function(data, status, headers, config) {
                    $location.path('/tastings/newAdded') //TODO add some ok message which shows with ng-if for example
                }).
                error(function(data, status, headers, config) {
                    console.log('error sending post post', status, data)
                });
        }
    }]
);
