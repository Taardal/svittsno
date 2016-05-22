INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('1',
        'Sherlock Holmes',
        'tt0988045',
        'Nothing escapes him.',
        'Eccentric consulting detective Sherlock Holmes and Doctor John Watson battle to bring down a new nemesis and unravel a deadly plot that could destroy England.',
        128,
        DATE '2009-12-24'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 1);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 2);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 6);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 16);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('1', 20);
INSERT INTO video_file (id, path) VALUES ('1', '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes.mkv');
INSERT INTO movie_video_file (movie_id, video_file_id) VALUES ('1', '1');
INSERT INTO image_file (id, path, type) VALUES ('1', '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes_poster.jpg', 'POSTER');
INSERT INTO image_file (id, path, type) VALUES ('2', '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes_backdrop.jpg', 'BACKDROP');
INSERT INTO movie_image_file (movie_id, image_file_id) VALUES ('1', '1');
INSERT INTO movie_image_file (movie_id, image_file_id) VALUES ('1', '2');



INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('2',
        'Sherlock Holmes: A Game of Shadows',
        'tt1515091',
        'The Game is Afoot.',
        'There is a new criminal mastermind at large--Professor Moriarty--and not only is he Holmes’ intellectual equal, but his capacity for evil and lack of conscience may give him an advantage over the detective.',
        129,
        DATE '2011-12-16'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('2', 1);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('2', 2);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('2', 6);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('2', 16);
INSERT INTO video_file (id, path) VALUES ('2', '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes_a_game_of_shadows\\sherlock_holmes_a_game_of_shadows.mkv');
INSERT INTO movie_video_file (movie_id, video_file_id) VALUES ('2', '2');
INSERT INTO image_file (id, path, type) VALUES ('3', '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes_a_game_of_shadows_poster.jpg', 'POSTER');
INSERT INTO image_file (id, path, type) VALUES ('4', '\\\\TAARDAL-SERVER\\Misc\\svitts\\sherlock_holmes\\sherlock_holmes_a_game_of_shadows_backdrop.jpg', 'BACKDROP');
INSERT INTO movie_image_file (movie_id, image_file_id) VALUES ('2', '3');
INSERT INTO movie_image_file (movie_id, image_file_id) VALUES ('2', '4');