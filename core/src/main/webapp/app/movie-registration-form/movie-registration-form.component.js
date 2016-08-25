'use strict';

angular.module('movieRegistrationForm').component('movieRegistrationForm', {
    templateUrl: 'app/movie-registration-form/movie-registration-form.template.html',
    controller: function () {

        var ctrl = this;

        ctrl.bar = function () {
            console.log("bar");
            console.log(ctrl.movie.title);
            ctrl.foo();
        };

    }, bindings: {
        foo: '&',
        movie: '<'
    }
});