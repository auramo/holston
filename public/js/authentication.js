
angular.module('authentication', []).service('AuthService', function($http, $q) {
    var authService = {}
    var userStatus = null
    authService.loggedIn = function() {
        console.log("userStatus in authservice", userStatus)
        if (!userStatus) return false
        return userStatus.loggedIn
    }

    authService.userStatus = function() {
        var deferred = $q.defer()
        if (userStatus) deferred.resolve(userStatus)
        else fetchUserStatus(deferred)
        return deferred.promise

        function fetchUserStatus(deferred) {
            $http.get('/api/userInfo').
                success(function(data, status, headers, config) {
                    if (data.status === "logged-in") {
                        console.log(data.email)
                        userStatus = { email: data.email, loggedIn: true};
                        deferred.resolve(userStatus)
                    }
                    else if (data.status === "anonymous") {
                        userStatus = { email: "", loggedIn: false};
                        deferred.resolve(userStatus)
                    }
                    else throw new Error("invalid response", data)
                }).
                error(function(data, status, headers, config) {
                    deferred.resolve("Error")
                    throw new Error("Error while fetching login status", status, data)
                });
        }
    }
    return authService
});

