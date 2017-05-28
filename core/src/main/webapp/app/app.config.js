angular
    .module('svittsApp')
    .config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
        $locationProvider.hashPrefix('!');
        $routeProvider.when('/', {
            template: '<home></home>'
        }).when('/movies/:genre', {
            template: '<movies-by-genre></movies-by-genre>'
        }).when('/register', {
            template: '<registration></registration>'
        }).when('/discover', {
            template: '<discovery></discovery>'
        }).when('/search', {
            template: '<search></search>'
        });
    }]);
