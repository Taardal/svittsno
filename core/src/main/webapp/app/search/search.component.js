'use-strict';

angular
    .module('search')
    .component('search', {
        templateUrl: 'app/search/search.template.html',
        controller: function ($http, $routeParams) {

            var ctrl = this;
            ctrl.query = $routeParams.q;
            ctrl.url = './api/v1/movies/search?q=' + ctrl.query;

        }
    });