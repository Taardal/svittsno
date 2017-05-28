angular
    .module('svittsApp.discovery')
    .component('discovery', {
        templateUrl: 'app/components/discovery/discovery.template.html',
        controller: function (Movie, TheMovieDb, notificationService) {

            var ctrl = this;

            ctrl.discover = function () {
                Movie.request({path: ctrl.path}).discover(function (movies) {
                    ctrl.discoveredMovies = [];
                    angular.forEach(movies, function (movie) {
                        if (movie.imdbId) {
                            TheMovieDb.get({id: movie.imdbId}, function (tmdbMovie) {
                                tmdbMovie.id = movie.id;
                                tmdbMovie.edition = movie.edition;
                                tmdbMovie.videoFile = movie.videoFile;
                                tmdbMovie.subtitleFiles = movie.subtitleFiles;
                                ctrl.discoveredMovies.push(tmdbMovie);
                            }, function (error) {
                                notificationService.error("Could not find TMDb movie by id [" + movie.imdbId + "]. [" + error + "].");
                            });
                        } else {
                            ctrl.discoveredMovies.push(movie);
                        }
                    });
                }, function (error) {
                    notificationService.error("Could not discover movies [" + error + "].");
                });
            };

            ctrl.registerDiscoveredMovies = function () {
                angular.forEach(ctrl.discoveredMovies, function (movie) {
                    Movie.request().save({
                        title: movie.title,
                        imdbId: movie.imdbId,
                        tagline: movie.tagline,
                        overview: movie.overview,
                        language: movie.language,
                        edition: movie.edition,
                        runtime: movie.runtime,
                        releaseDate: movie.releaseDate,
                        genres: movie.genres,
                        videoFile: movie.videoFile,
                        subtitleFiles: movie.subtitleFiles,
                        posterPath: movie.posterPath,
                        backdropPath: movie.backdropPath
                    }, function () {
                        notificationService.success("Movie [" + movie.title + "] registered successfully.");
                    }, function (response) {
                        var errorMessage = "";
                        angular.forEach(response.data.messages, function (message) {
                            errorMessage = errorMessage + message + " ";
                        });
                        notificationService.error("[" + response.status + "] Could not register movie [" + movie.title + "]: " + errorMessage);
                    })
                })
            };

            ctrl.reset = function () {
                ctrl.discoveredMovies = null;
                ctrl.path = "";
                ctrl.username = "";
                ctrl.password = "";
            }

        }
    });
