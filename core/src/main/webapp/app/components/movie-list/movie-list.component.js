angular
    .module('svittsApp.movieList')
    .component('movieList', {
        templateUrl: 'app/components/movie-list/movie-list.template.html',
        controller: function ($http, $routeParams, Movie, notificationService) {

            var ctrl = this;

            ctrl.itemsPerRow = 7;

            ctrl.playMovie = function (movie) {
                console.log("Playing movie: " + movie);
                Movie.request().play({path: movie.videoFile.path}, function () {
                    notificationService.success("Playing movie " + movie.title);
                }, function (error) {
                    notificationService.error("Could not play movie. Is the player running?");
                });
            };

            ctrl.isMetadataEmpty = function () {
                return !angular.element('#metadata').text();
            };

            ctrl.shouldAddSeparator = function () {
                var text = angular.element('#metadata').text();
                return text && !text.endsWith('|');
            }

        },
        bindings: {
            data: '<',
            itemsPerRow: '<'
        }
    });