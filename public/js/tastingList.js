angular.module('tastingList', []).controller('TastingListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.tastingCount = 0
        $scope.tastings = [
            {'beer': 'Ale Kokki', 'brewery': 'Joku virolaine', 'averageRating': 44},
            {'beer': 'Saku', 'brewery': 'Saku varmaa', 'averageRating': 22}
        ]
        $http.get('/api/tastings/count').
            success(function(data, status, headers, config) {
                $scope.tastingCount = data.count
            }).
            error(function(data, status, headers, config) {
                console.log('error getting count', status, data)
            })
    }]
);
