'use strict';

angular.module('navbar').component('navbar', {
    templateUrl: 'app/navbar/navbar.template.html',
    controller: function ($http, modalService, notificationService) {

        var ctrl = this;
        ctrl.genres = [];

        $http.get('./api/v1/genres').then(function (response) {
            angular.forEach(response.data, function (genre, i) {
                ctrl.genres[i] = getPrettyGenre(genre);
            });
        });

        ctrl.movie = {
            title: '',
            imdbId: '',
            tagline: '',
            overview: '',
            runtime: 0,
            releaseDate: new Date(),
            genres: [],
            videoFile: {
                path: ''
            },
            posterPath: '',
            backdropPath: ''
        };

        ctrl.autoFill = function (imdbId) {
            if (!imdbId) {
                notificationService.error("IMDB ID is required for auto-fill.");
            } else {
                var tmdbApiKey = 'b041b0681fa9947874d41095ea1ca5ae';
                $http.get('http://api.themoviedb.org/3/movie/' + imdbId + '?api_key=' + tmdbApiKey).then(function (response) {
                    var tmdbMovie = response.data;
                    ctrl.movie.title = tmdbMovie.title;
                    ctrl.movie.tagline = tmdbMovie.tagline;
                    ctrl.movie.overview = tmdbMovie.overview;
                    ctrl.movie.runtime = tmdbMovie.runtime;
                    ctrl.movie.releaseDate = getTMDbReleaseDateAsDate(tmdbMovie.release_date);
                    angular.forEach(tmdbMovie.genres, function (genre) {
                        ctrl.movie.genres.push(genre.name);
                    });
                    $http.get('http://api.themoviedb.org/3/movie/' + imdbId + '/images?api_key=' + tmdbApiKey).then(function (response) {
                        var imageBaseUrl = 'http://image.tmdb.org/t/p/original';
                        ctrl.movie.posterPath = imageBaseUrl + response.data.posters[0].file_path;
                        ctrl.movie.backdropPath = imageBaseUrl + response.data.backdrops[0].file_path;
                    }, function () {
                        notificationService.error("Could not get image paths for auto-fill.");
                    });
                }, function () {
                    notificationService.error("Could not auto-fill movie details.");
                });
            }
        };

        ctrl.registerMovie = function () {
            console.log(angular.toJson(ctrl.movie, true));
            $http.post('./api/v1/movies', angular.toJson(ctrl.movie)).then(function () {
                notificationService.success("Movie posted successfully.");
            }, function () {
                notificationService.error("An unexpected error occurred.")
            });
        };

        ctrl.dateFormats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy'];
        ctrl.defaultDateFormat = ctrl.dateFormats[0];
        ctrl.alternateInputFormats = ['M!/d!/yyyy'];

        ctrl.datePicker = {
            open: false
        };

        ctrl.dateOptions = {
            startingDay: 1,
            datepickerMode: 'year',
            dateDisabled: function () {

            }
        };

        ctrl.openDatePicker = function() {
            ctrl.datePicker.open = true;
        };

        ctrl.setDate = function(year, month, day) {
            ctrl.movie.releaseDate = new Date(year, month, day);
        };

        ctrl.toggleSelectedGenre = function (genre) {
            var i = ctrl.movie.genres.indexOf(genre);
            if (i > -1) {
                ctrl.movie.genres.splice(i, 1);
            } else {
                ctrl.movie.genres.push(genre);
            }
        }

    }
});

function getPrettyGenre(genre) {
    var prettyGenre = "";
    var parts = genre.split("_");
    angular.forEach(parts, function (part, i) {
        if (i > 0) {
            prettyGenre += "-";
        }
        prettyGenre += part.charAt(0) + part.slice(1).toLowerCase();
    });
    return prettyGenre;
}

function getTMDbReleaseDateAsDate(tmdbReleaseDate) {
    var year = tmdbReleaseDate.split("-")[0];
    var month = tmdbReleaseDate.split("-")[1];
    var day = tmdbReleaseDate.split("-")[2];
    return new Date(year, month, day);
}

