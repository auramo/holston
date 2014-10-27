angular.module('tastingList', []).controller('TastingListController', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
        $scope.tastingCount = 0
        $scope.ratings = []
        $scope.showNewAdded = $routeParams.action === "newAdded"

        $http.get('/api/ratings').
            success(function(data, status, headers, config) {
                $scope.ratings = data.ratings
            }).
            error(function(data, status, headers, config) {
                console.log('error getting count', status, data)
            })
    }]
);
