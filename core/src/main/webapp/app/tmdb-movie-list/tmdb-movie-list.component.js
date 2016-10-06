'use-strict';

angular
    .module('tmdbMovieList')
    .component('tmdbMovieList', {
        templateUrl: 'app/tmdb-movie-list/tmdb-movie-list.template.html',
        controller: function ($http, notificationService) {

            var ctrl = this;

            ctrl.movies = [];

            $http.get(ctrl.url).then(function (response) {
                var theMovieDatabaseImageBaseUrl = 'http://image.tmdb.org/t/p/original';
                angular.forEach(response.data.results, function (theMovieDatabaseMovie) {
                    var movie = {};
                    movie.title = theMovieDatabaseMovie.title;
                    movie.tagline = theMovieDatabaseMovie.tagline;
                    movie.overview = theMovieDatabaseMovie.overview;
                    movie.runtime = theMovieDatabaseMovie.runtime;
                    movie.releaseDate = getTMDbReleaseDateAsDate(theMovieDatabaseMovie.release_date);
                    angular.forEach(theMovieDatabaseMovie.genres, function (genre) {
                        movie.genres.push(genre.name);
                    });
                    movie.posterPath = theMovieDatabaseImageBaseUrl + theMovieDatabaseMovie.poster_path;
                    movie.backdropPath = theMovieDatabaseImageBaseUrl + theMovieDatabaseMovie.backdrop_path;
                    ctrl.movies.push(movie);
                })
            }, function (response) {
                notificationService.error("[" + response.status + "] Could not get movies from The Movie Database");
            });

        },
        bindings: {
            url: '@'
        }
    });

function getTMDbReleaseDateAsDate(tmdbReleaseDate) {
    var year = tmdbReleaseDate.split("-")[0];
    var month = tmdbReleaseDate.split("-")[1];
    var day = tmdbReleaseDate.split("-")[2];
    return new Date(year, month, day);
}