angular
    .module('svittsApp.movieList')
    .component('movieList', {
        templateUrl: 'app/components/movie-list/movie-list.template.html',
        controller: function ($http, $routeParams, notificationService) {

            var ctrl = this;

            ctrl.itemsPerRow = 7;

            ctrl.playMovie = function (movie) {
                var payload = {
                    path: movie.videoFile.path
                };
                $http.post('http://localhost:8181', angular.toJson(payload)).then(function () {
                    notificationService.success("Playing movie " + movie.title);
                }, function () {
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