'use strict';

describe('movieList', function() {

    // Load the module that contains the `movieList` component before each test
    beforeEach(module('movieList'));

    // Test the controller
    describe('MovieListController', function() {

        it('should create a `movies` model with 3 movies', inject(function($componentController) {
            var ctrl = $componentController('movieList');

            expect(ctrl.movies.length).toBe(3);
        }));

    });

});
