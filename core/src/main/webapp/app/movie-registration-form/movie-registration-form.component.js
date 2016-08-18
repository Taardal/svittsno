'use strict';

angular.module('movieRegistrationForm').component('movieRegistrationForm', {
    templateUrl: 'app/movie-registration-form/movie-registration-form.template.html',
    controller: function () {

        var self = this;
        self.movie = {
            title: '',
            imdbId: ''
        };
        self.form = {};


        self.register = function () {
            console.log('Registered ' + self.movie.title + ' ' + self.movie.imdbId)
        }

    }
});