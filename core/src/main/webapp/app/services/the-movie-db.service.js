angular
    .module('svittsApp')
    .factory('TheMovieDb', ['$resource', 'languageService', function ($resource, languageService) {

        var apiKey = "b041b0681fa9947874d41095ea1ca5ae";
        var apiUrl = "http://api.themoviedb.org/3";
        var imageUrl = 'http://image.tmdb.org/t/p/original';

        return $resource(apiUrl + "/movie/:id/:path", {}, {
            get: {
                method: 'GET',
                params: {
                    api_key: apiKey
                },
                transformResponse: function (response) {
                    return getAsMovie(angular.fromJson(response));
                }
            },
            query: {
                method: 'GET',
                isArray: true,
                params: {
                    api_key: apiKey
                },
                transformResponse: function (response) {
                    return getAsMovies(angular.fromJson(response));
                }
            }
        });

        function getAsMovies(json) {
            var movies = [];
            angular.forEach(json.results, function (tmdbMovie) {
                movies.push(getAsMovie(tmdbMovie));
            });
            return movies;
        }

        function getAsMovie(tmdbMovie) {
            var movie = {
                genres: [],
                subtitleFiles: []
            };
            movie.title = tmdbMovie.title;
            movie.imdbId = tmdbMovie.imdb_id;
            movie.tagline = tmdbMovie.tagline;
            movie.overview = tmdbMovie.overview;
            movie.runtime = tmdbMovie.runtime;
            movie.language = languageService.languages[tmdbMovie.original_language].name;
            movie.releaseDate = getAsDate(tmdbMovie.release_date);
            angular.forEach(tmdbMovie.genres, function (genre) {
                movie.genres.push(genre.name);
            });
            movie.posterPath = imageUrl + "/" + tmdbMovie.poster_path;
            movie.backdropPath = imageUrl + "/" + tmdbMovie.backdrop_path;
            return movie;
        }

        function getAsDate(tmdbReleaseDate) {
            var year = tmdbReleaseDate.split("-")[0];
            var month = tmdbReleaseDate.split("-")[1];
            var day = tmdbReleaseDate.split("-")[2];
            return new Date(year, month, day);
        }

    }]);