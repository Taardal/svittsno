'use-strict';

angular
    .module('home')
    .component('home', {
        templateUrl: 'app/home/home.template.html',
        controller: function () {

            var ctrl = this;

            var theMovieDatabaseApiKey = "b041b0681fa9947874d41095ea1ca5ae";
            var theMovieDatabaseBaseUrl = "http://api.themoviedb.org/3/";

            ctrl.popularNowUrl = theMovieDatabaseBaseUrl + "movie/popular?api_key=" + theMovieDatabaseApiKey;
            ctrl.upcomingUrl = theMovieDatabaseBaseUrl + "movie/upcoming?api_key=" + theMovieDatabaseApiKey;
            ctrl.topRatedUrl = theMovieDatabaseBaseUrl + "movie/top_rated?api_key=" + theMovieDatabaseApiKey;

        }
    });