angular
    .module('svittsApp')
    .factory('Movie', ['$resource', function ($resource) {
        var baseUrl = 'api/v1/movies';
        return $resource(baseUrl + "/:id/", {}, {
            query: {
                method: 'GET',
                url: baseUrl + '/genres/:genre',
                isArray: true,
                transformResponse: function (response) {
                    return getAsMovies(angular.fromJson(response));
                }
            },
            search: {
                method: 'GET',
                url: baseUrl + '/search',
                isArray: true,
                transformResponse: function (response) {
                    return getAsMovies(angular.fromJson(response));
                }
            }
        });

        function getAsMovies(json) {
            var movies = [];
            angular.forEach(json, function (movie) {
                movies.push(getAsMovie(movie));
            });
            return movies;
        }

        function getAsMovie(item) {
            var movie = {
                genres: [],
                subtitleFiles: []
            };
            movie.title = item.title;
            movie.imdbId = item.imdbId;
            movie.tagline = item.tagline;
            movie.overview = item.overview;
            movie.runtime = item.runtime;
            movie.language = item.language;
            movie.releaseDate = item.releaseDate.time ? new Date(item.releaseDate.time) : null;
            angular.forEach(item.genres, function (genre) {
                movie.genres.push(genre.name);
            });
            movie.videoFile = item.videoFile;
            movie.subtitleFiles = item.subtitleFiles;
            movie.posterPath = item.posterPath;
            movie.backdropPath = item.backdropPath;
            return movie;
        }

    }]);