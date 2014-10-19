angular.module('tastingList', []).controller('TastingListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.tastingCount = 0
        $http.get('/api/tastings/count').
            success(function(data, status, headers, config) {
                $scope.tastingCount = data.count
            }).
            error(function(data, status, headers, config) {
                console.log('error getting count', status, data)
            })
    }]
);
