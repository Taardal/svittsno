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


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('3',
        'Finding Nemo',
        'tt0266543',
        'There are 3.7 trillion fish in the ocean, they''re looking for one.',
        'A tale which follows the comedic and eventful journeys of two fish, the fretful Marlin and his young son Nemo, who are separated from each other in the Great Barrier Reef when Nemo is unexpectedly taken from his home and thrust into a fish tank in a dentist''s office overlooking Sydney Harbor. Buoyed by the companionship of a friendly but forgetful fish named Dory, the overly cautious Marlin embarks on a dangerous trek and finds himself the unlikely hero of an epic journey to rescue his son.',
        100,
        DATE '2003-11-29'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('3', 2);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('3', 3);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('3', 5);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('3', 9);


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('4',
        'The Shawshank Redemption',
        'tt0111161',
        'Fear can hold you prisoner. Hope can set you free.',
        'Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.',
        142,
        DATE '1994-09-10'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('4', 6);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('4', 8);


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('5',
        'Where to Invade Next',
        'tt4897822',
        'Prepare to be liberated.',
        'Academy Award-winning director Michael Moore returns with what may be his most provocative and hilarious film yet: Moore tells the Pentagon to "stand down" — he will do the invading for America from now on.',
        110,
        DATE '2016-02-12'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('5', 7);


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('6',
        'Fantastic Beasts and Where to Find Them',
        'tt3183660',
        '',
        'The adventures of writer Newt Scamander in New York''s secret community of witches and wizards 70 years before Harry Potter reads his book in school.',
        124,
        DATE '2016-11-11'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('6', 2);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('6', 9);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('6', 10);


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('7',
        'Braveheart',
        'tt0112573',
        'Every man dies. Not every man truly lives.',
        'Enraged at the slaughter of Murron, his new bride and childhood love, legendary Scottish warrior William Wallace slays a platoon of the local English lord''s soldiers. This leads the village to revolt and, eventually, the entire country to rise up against English rule.',
        177,
        DATE '1995-05-24'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('7', 1);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('7', 8);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('7', 12);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('7', 21);


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('8',
        'Scream',
        'tt0117571',
        'Someone has taken their love of scary movies one step too far.',
        'A killer known as Ghostface begins killing off teenagers, and as the body count begins rising, one girl and her friends find themselves contemplating the "Rules" of horror films as they find themselves living in a real-life one.',
        111,
        DATE '1996-12-19'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('8', 6);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('8', 13);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('8', 16);


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('9',
        'Mamma Mia!',
        'tt0795421',
        'Take a trip down the aisle you''ll never forget.',
        'Set on an idyllic Greek island, the plot serves as a background for a wealth of ABBA hit songs. Donna, an independent, single mother who owns a small hotel on the island is about to let go of Sophie, the spirited young daughter she''s raised alone. But Sophie has secretly invited three of her mother''s ex-lovers in the hopes of finding her father.',
        108,
        DATE '2008-06-30'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('9', 5);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('9', 15);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('9', 17);


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('10',
        'Star Wars: The Force Awakens',
        'tt2488496',
        'Every generation has a story.',
        'Thirty years after defeating the Galactic Empire, Han Solo and his allies face a new threat from the evil Kylo Ren and his army of Stormtroopers.',
        136,
        DATE '2015-12-15'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('10', 1);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('10', 2);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('10', 10);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('10', 18);


INSERT INTO movie (id, name, imdb_id, tagline, overview, runtime, release_date)
VALUES ('11',
        'Django Unchained',
        'tt1853728',
        'Life, liberty and the pursuit of vengeance.',
        'With the help of a German bounty hunter, a freed slave sets out to rescue his wife from a brutal Mississippi plantation owner.',
        165,
        DATE '2012-12-25'
);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('11', 8);
INSERT INTO movie_genre (movie_id, genre_id) VALUES ('11', 22);