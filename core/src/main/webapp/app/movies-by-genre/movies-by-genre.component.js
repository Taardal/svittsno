'use-strict';

angular
    .module('moviesByGenre')
    .component('moviesByGenre', {
        templateUrl: 'app/movies-by-genre/movies-by-genre.template.html',
        controller: function ($routeParams) {

            var ctrl = this;
            ctrl.genre = $routeParams.genre;
            ctrl.url = './api/v1/movies/genres/' + ctrl.genre;

        }
    });