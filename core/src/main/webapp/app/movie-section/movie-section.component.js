'use-strict';

angular
    .module('movieSection')
    .component('movieSection', {
        templateUrl: 'app/movie-section/movie-section.template.html',
        controller: function ($routeParams) {

            var ctrl = this;

            if (ctrl.type == 'genre') {
                ctrl.title = $routeParams.genre;
            } else if (ctrl.type == 'search') {
                ctrl.title = 'Search results';
            } else if (ctrl.type == 'front') {

            } else {
                ctrl.title = 'No movies found.';
            }

            ctrl.url = ["movie1", "movie2", "movie3"];

        },
        bindings: {
            type: '@'
        }
    });