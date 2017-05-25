angular
    .module('svittsApp.search')
    .component('search', {
        templateUrl: 'app/components/search/search.template.html',
        controller: function ($routeParams, Movie) {

            var ctrl = this;

            ctrl.q = $routeParams.q;
            ctrl.movies = Movie.search({q: ctrl.q});

        }
    });