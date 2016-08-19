'use strict';

angular.module('navbar').component('navbar', {
    templateUrl: 'app/navbar/navbar.template.html',
    controller: function ($http, modalService) {

        var self = this;
        self.genres = [];

        $http.get('./api/v1/genres').then(function (response) {
            angular.forEach(response.data, function (genre, i) {
                self.genres[i] = getPrettyGenre(genre);
            });
        });

        self.open = function () {
            console.log("navbar open");
            modalService.open();
        }

    }
});

function getPrettyGenre(genre) {
    var prettyGenre = "";
    var parts = genre.split("_");
    angular.forEach(parts, function (part, i) {
        if (i > 0) {
            prettyGenre += "-";
        }
        prettyGenre += part.charAt(0) + part.slice(1).toLowerCase();
    });
    return prettyGenre;
}

