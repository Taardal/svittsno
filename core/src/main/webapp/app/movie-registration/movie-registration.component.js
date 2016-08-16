'use strict';

angular.module('movieRegistration').component('movieRegistration', {
    templateUrl: '',
    controller: function ($http, $uibModal) {

        var self = this;
        var json = "";

        $http.post('./api/v1/movies', json).then(
            function success(response) {

            }, function error(response) {

            });

    }
});