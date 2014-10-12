angular.module('editTasting', []).controller('EditTastingController', ['$scope', '$routeParams', '$http',
    function ($scope, $routeParams, $http) {
        //TODO for some reason 200 ok is always received even if we call /plaa/plaa
        $scope.tasting = {}
        $http.get('/api/beers').
            success(function(data) {
                $scope.beers = _.pluck(data.beers, 'name')
            }).
            error(function(data, status, headers, config) {
                console.log('ERROR', status)
            });
        $scope.tastingId = $routeParams.tastingId
        $scope.onType = function(x) { console.log('onType', x) }
        $scope.save = save
        function save(tasting) { console.log("save", tasting); console.log(tasting) }
    }]
);
