angular
    .module('svittsApp')
    .factory('Movie', ['$resource', function ($resource) {
        var baseUrl = 'api/v1/movies';
        return {
            request: function (headers) {
                if (!headers) {
                    headers = {}
                }
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
                    },
                    discover: {
                        method: 'GET',
                        url: 'api/v1/discover/local',
                        isArray: true,
                        headers: {
                            svitts_path: headers.path,
                            svitts_username: headers.username,
                            svitts_password: headers.password
                        },
                        transformResponse: function (response) {
                            return getAsMovies(angular.fromJson(response));
                        }
                    },
                    play: {
                        method: 'POST',
                        url: 'http://localhost:8181'
                    }
                })
            }
        };

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
            movie.edition = item.edition;
            movie.tagline = item.tagline;
            movie.overview = item.overview;
            movie.runtime = item.runtime;
            movie.language = item.language;
            movie.releaseDate = item.releaseDate && item.releaseDate.time ? new Date(item.releaseDate.time) : null;
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