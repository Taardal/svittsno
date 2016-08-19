'use-strict';

angular.module('movieList').component('movieList', {
    templateUrl: 'app/movie-list/movie-list.template.html',
    controller: function MovieListController($http) {

        var self = this;
        var apiUrl = "http://api.themoviedb.org/3/";
        var imageUrl = "http://image.tmdb.org/t/p/";
        var apiKey = "b041b0681fa9947874d41095ea1ca5ae";

        $http.get('./api/v1/movies/genres/ACTION').then(function (response) {
            self.movies = response.data;
        })
    }
});