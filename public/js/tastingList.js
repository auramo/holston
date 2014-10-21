angular.module('tastingList', []).controller('TastingListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.tastingCount = 0
        $scope.ratings = []

        $http.get('/api/ratings').
            success(function(data, status, headers, config) {
                $scope.ratings = data.ratings
            }).
            error(function(data, status, headers, config) {
                console.log('error getting count', status, data)
            })
    }]
);
