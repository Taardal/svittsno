angular
    .module('svittsApp.navbar')
    .component('navbar', {
        templateUrl: 'app/components/navbar/navbar.template.html',
        controller: function ($http, notificationService) {

            var ctrl = this;

            ctrl.searchQuery = "";

            ctrl.genres = [];
            ctrl.dateFormats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy'];
            ctrl.alternateInputFormats = ['M!/d!/yyyy'];

            ctrl.movie = {
                title: '',
                imdbId: '',
                tagline: '',
                overview: '',
                language: '',
                edition: '',
                runtime: 0,
                releaseDate: new Date(),
                genres: [],
                videoFile: {
                    path: ''
                },
                subtitleFiles: [],
                posterPath: '',
                backdropPath: ''
            };

            ctrl.subtitleFilesString = "";

            ctrl.datePicker = {
                open: false
            };

            ctrl.dateOptions = {
                startingDay: 1,
                datepickerMode: 'year',
                dateDisabled: function () {

                }
            };

            $http.get('./api/v1/genres').then(function (response) {
                ctrl.genres = response.data;
            }, function () {
                notificationService.error("Could not get genres.");
            });

            ctrl.autoFill = function (imdbId) {
                if (!imdbId) {
                    notificationService.error("IMDB ID is required for auto-fill.");
                } else {
                    var theMovieDatabaseApiKey = 'b041b0681fa9947874d41095ea1ca5ae';
                    var theMovieDatabaseImageBaseUrl = 'http://image.tmdb.org/t/p/original';
                    $http.get('http://api.themoviedb.org/3/movie/' + imdbId + '?api_key=' + theMovieDatabaseApiKey).then(function (response) {
                        var theMovieDatabaseMovie = response.data;
                        ctrl.movie.title = theMovieDatabaseMovie.title;
                        ctrl.movie.tagline = theMovieDatabaseMovie.tagline;
                        ctrl.movie.overview = theMovieDatabaseMovie.overview;
                        ctrl.movie.language = theMovieDatabaseMovie.spoken_languages[0].name;
                        ctrl.movie.runtime = theMovieDatabaseMovie.runtime;
                        ctrl.movie.releaseDate = getTMDbReleaseDateAsDate(theMovieDatabaseMovie.release_date);
                        angular.forEach(theMovieDatabaseMovie.genres, function (genre) {
                            ctrl.movie.genres.push(genre.name);
                        });
                        ctrl.movie.posterPath = theMovieDatabaseImageBaseUrl + theMovieDatabaseMovie.poster_path;
                        ctrl.movie.backdropPath = theMovieDatabaseImageBaseUrl + theMovieDatabaseMovie.backdrop_path;
                    }, function () {
                        notificationService.error("Could not auto-fill movie details.");
                    });
                }
            };

            ctrl.registerMovie = function () {
                if (ctrl.subtitleFilesString.length > 0) {
                    ctrl.movie.subtitleFiles = getSubtitleFiles(ctrl.subtitleFilesString);
                }
                $http.post('./api/v1/movies', angular.toJson(ctrl.movie)).then(function () {
                    notificationService.success("Movie registered successfully.");
                }, function (response) {
                    var errorMessage = "";
                    angular.forEach(response.data.messages, function (message) {
                      errorMessage = errorMessage + message + " ";
                    });
                    notificationService.error("[" + response.status + "] " + errorMessage);
                });
            };

            ctrl.openDatePicker = function () {
                ctrl.datePicker.open = true;
            };

            ctrl.setDate = function (year, month, day) {
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

function getTMDbReleaseDateAsDate(tmdbReleaseDate) {
    var year = tmdbReleaseDate.split("-")[0];
    var month = tmdbReleaseDate.split("-")[1];
    var day = tmdbReleaseDate.split("-")[2];
    return new Date(year, month, day);
}

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

