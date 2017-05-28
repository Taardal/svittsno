angular
    .module('svittsApp.home')
    .component('home', {
        templateUrl: 'app/components/home/home.template.html',
        controller: function (TheMovieDb) {

            var ctrl = this;

            ctrl.upcoming = TheMovieDb.query({path: "upcoming"});
            ctrl.popular = TheMovieDb.query({path: "popular"});

        }
    });