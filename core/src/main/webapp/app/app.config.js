'use strict';

angular
    .module('svittsApp')
    .config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');
        $routeProvider.when('/genres/:genre', {
            template: '<movie-list></movie-list>'
        }).otherwise('/');
    }
    ]);
