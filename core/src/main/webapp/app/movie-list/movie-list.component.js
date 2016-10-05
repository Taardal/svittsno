'use-strict';

angular
    .module('movieList')
    .component('movieList', {
        templateUrl: 'app/movie-list/movie-list.template.html',
        controller: function ($http, $routeParams, notificationService) {

            var ctrl = this;

            ctrl.movies = [];

            $http.get(ctrl.url).then(function (response) {
                ctrl.movies = response.data;
            }, function (response) {
                notificationService.error("[" + response.status + "] Could not get movies");
            });

            ctrl.playMovie = function (movie) {
                var request = {
                    method: 'GET',
                    url: 'http://localhost:8181',
                    headers: {
                        'path': movie.videoFile.path
                    }
                };
                var payload = {
                    path: movie.videoFile.path
                };
                console.log(angular.toJson(payload));
                $http.post('http://localhost:8181', angular.toJson(payload)).then(function () {
                    notificationService.success("Playing movie " + movie.title);
                }, function () {
                    notificationService.error("Could not play movie. Is the player running?");
                });
            }

        },
        bindings: {
            url: '@'
        }
    });