
angular.module('authentication', []).service('AuthService', function($http, $q) {
    var authService = {}
    authService.isAuthenticated = function() {
        return false
    }

    authService.userStatus = function() {
        var deferred = $q.defer()
        $http.get('/api/userInfo').
            success(function(data, status, headers, config) {
                if (data.status === "logged-in") {
                    console.log(data.email)
                    deferred.resolve(data.email)
                }
                else if (data.status === "anonymous") deferred.resolve("Not logged in")
                else throw new Error("invalid response", data)
            }).
            error(function(data, status, headers, config) {
                deferred.resolve("Error")
                throw new Error("Error while fetching login status", status, data)
            });
        return deferred.promise
    }
    return authService
});

