'use-strict';

angular.module('movieList').component('movieList', {
    // templateUrl: 'app/movie-list/movie-list.template.html',
    template: 'TBD: Detail view for <span>{{$ctrl.genre}}</span>',
    controller: ['$routeParams', function ($routeParams) {

        var ctrl = this;
        ctrl.genre = $routeParams.genre;

        $http.get('./api/v1/movies/genres/' + ctrl.genre).then(function (response) {
            ctrl.movies = response.data;
        })

    }
    ]
});