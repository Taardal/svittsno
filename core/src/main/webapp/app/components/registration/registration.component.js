angular
    .module('svittsApp.registration')
    .component('registration', {
        templateUrl: 'app/components/registration/registration.template.html',
        controller: function (Movie, TheMovieDb, Genre, notificationService) {

            var ctrl = this;

            ctrl.genres = Genre.query();

            ctrl.autoFill = function () {
                if (!ctrl.movie.imdbId) {
                    notificationService.error("IMDb id is required for auto-fill.");
                } else {
                    TheMovieDb.get({id: ctrl.movie.imdbId}, function (movie) {
                        ctrl.movie = movie;
                    }, function (error) {
                        notificationService.error("Could not auto-fill movie details [" + error + "].");
                    });
                }
            };

            ctrl.registerMovie = function () {
                if (ctrl.subtitleFilesString) {
                    ctrl.movie.subtitleFiles = getSubtitleFiles(ctrl.subtitleFilesString);
                }
                Movie.request().save({
                    title: ctrl.movie.title,
                    imdbId: ctrl.movie.imdbId,
                    tagline: ctrl.movie.tagline,
                    overview: ctrl.movie.overview,
                    language: ctrl.movie.language,
                    edition: ctrl.movie.edition,
                    runtime: ctrl.movie.runtime,
                    releaseDate: ctrl.movie.releaseDate,
                    genres: ctrl.movie.genres,
                    videoFile: ctrl.movie.videoFile,
                    subtitleFiles: ctrl.movie.subtitleFiles,
                    posterPath: ctrl.movie.posterPath,
                    backdropPath: ctrl.movie.backdropPath
                }, function () {
                    notificationService.success("Movie [" + ctrl.movie.title + "] registered successfully.");
                    ctrl.reset();
                }, function (response) {
                    var errorMessage = "";
                    angular.forEach(response.data.messages, function (message) {
                        errorMessage = errorMessage + message + " ";
                    });
                    notificationService.error("[" + response.status + "] " + errorMessage);
                })
            };

            ctrl.reset = function () {
                ctrl.movie = {
                    title: '',
                    imdbId: '',
                    tagline: '',
                    overview: '',
                    language: '',
                    edition: '',
                    runtime: "",
                    releaseDate: "",
                    genres: [],
                    videoFile: {
                        path: ''
                    },
                    subtitleFiles: [],
                    posterPath: '',
                    backdropPath: ''
                };
            };

            ctrl.toggleSelectedGenre = function (genre) {
                var i = ctrl.movie.genres.indexOf(genre);
                if (i > -1) {
                    ctrl.movie.genres.splice(i, 1);
                } else {
                    ctrl.movie.genres.push(genre);
                }
            };

            function getSubtitleFiles(subtitleFilesString) {
                var subtitleFiles = [];
                angular.forEach(subtitleFilesString.replace(/\r?\n|\r/, "").replace(" ", "").split(","), function (subtitleFileString) {
                    var subtitleFileInfo = subtitleFileString.split("|");
                    var subtitleFile = {
                        path: subtitleFileInfo[0],
                        language: subtitleFileInfo[1] ? subtitleFileInfo[1] : ""
                    };
                    subtitleFiles.push(subtitleFile);
                });
                return subtitleFiles;
            }

        }
    });
