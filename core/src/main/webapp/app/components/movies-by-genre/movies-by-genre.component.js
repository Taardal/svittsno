angular
    .module('svittsApp.moviesByGenre')
    .component('moviesByGenre', {
        templateUrl: 'app/components/movies-by-genre/movies-by-genre.template.html',
        controller: function ($routeParams, Movie) {

            var ctrl = this;

            ctrl.genre = $routeParams.genre;

            ctrl.movies = [];
            ctrl.itemsPerRow = 7;
            ctrl.rows = ctrl.movies.length / ctrl.itemsPerRow;

            ctrl.movies = Movie.query({genre: ctrl.genre});

        }
    });