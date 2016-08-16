'use-strict';

angular.module('movieList').component('movieList', {
    templateUrl: 'app/movie-list/movie-list.template.html',
    controller: function MovieListController($http) {
        var self = this;
        $http.get('./api/v1/movies/genres/ACTION').then(function (response) {
            self.movies = response.data;
        })
    }
});