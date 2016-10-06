'use strict';

angular
    .module('svittsApp')
    .config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');
        $routeProvider.when('/', {
            template: '<home></home>'
        }).when('/movies/:genre', {
            template: '<movies-by-genre></movies-by-genre>'
        }).when('/search', {
            template: '<search></search>'
        });
    }]);
