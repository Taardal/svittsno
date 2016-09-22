'use strict';

angular
    .module('svittsApp')
    .config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');
        $routeProvider.when('/', {
            templateUrl: 'home.html'
        }).when('/genres/:genre', {
            template: '<movie-section type="genre"></movie-section>'
        }).otherwise('/');
    }]);
