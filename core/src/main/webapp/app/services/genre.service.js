angular
    .module('svittsApp')
    .factory('Genre', ['$resource', function ($resource) {
        return $resource("api/v1/genres");
    }]);