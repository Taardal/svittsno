'use-strict';

angular.module('movieList').component('movieList', {
    templateUrl: 'app/movie-list/movie-list.template.html',
    controller: function ($http, $routeParams, notificationService) {

        var ctrl = this;
        ctrl.genre = $routeParams.genre;

        $http.get('./api/v1/movies/genres/' + ctrl.genre).then(function (response) {
            ctrl.movies = response.data;
        });

        ctrl.playMovie = function (movie) {
            var request = {
                method: 'GET',
                url: 'http://localhost:8181/player',
                headers: {
                    path: movie.videoFile.path
                }
            };
            $http(request).then(function () {
                notificationService.success("Could not play movie. Is the player running?");
            }, function () {
                notificationService.error("Could not play movie. Is the player running?");
            });
        }

    }
});