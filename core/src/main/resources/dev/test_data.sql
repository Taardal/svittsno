INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date, video_file_path, poster_image_file_path, backdrop_image_file_path)
VALUES ('1',
        'Sherlock Holmes',
        'tt0988045',
        'Nothing escapes him.',
        'Eccentric consulting detective Sherlock Holmes and Doctor John Watson battle to bring down a new nemesis and unravel a deadly plot that could destroy England.',
        128,
        DATE '2009-12-24',
        '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes.mkv',
        '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes_poster.jpg',
        '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes_backdrop.jpg'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 1);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 2);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 6);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 16);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 20);



INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('2',
        'Sherlock Holmes: A Game of Shadows',
        'tt1515091',
        'The Game is Afoot.',
        'There is a new criminal mastermind at large--Professor Moriarty--and not only is he Holmes’ intellectual equal, but his capacity for evil and lack of conscience may give him an advantage over the detective.',
        129,
        DATE '2011-12-16',
        '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes_a_game_of_shadows\\sherlock_holmes_a_game_of_shadows.mkv',
        '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes_a_game_of_shadows_poster.jpg',
        '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes_a_game_of_shadows_backdrop.jpg'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('2', 1);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('2', 2);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('2', 6);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('2', 16);