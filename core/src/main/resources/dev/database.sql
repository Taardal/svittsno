DROP TABLE IF EXISTS movie;
CREATE TABLE movie (
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  imdb_id VARCHAR(255) DEFAULT NULL,
  tagline VARCHAR(255) DEFAULT NULL,
  overview VARCHAR(510) DEFAULT NULL,
  runtime SMALLINT UNSIGNED DEFAULT NULL,
  release_date DATE DEFAULT NULL,
  video_file_path VARCHAR(255) DEFAULT NULL,
  poster_image_file_path VARCHAR(255) DEFAULT NULL,
  backdrop_image_file_path VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);
DROP TABLE IF EXISTS genre;
CREATE TABLE genre (
  id TINYINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
DROP TABLE IF EXISTS movie_genre;
CREATE TABLE movie_genre (
  movie_id VARCHAR(255) NOT NULL,
  genre_id TINYINT NOT NULL,
  PRIMARY KEY (movie_id,genre_id),
  KEY genre_id (genre_id),
  CONSTRAINT movie_genre_ibfk_1 FOREIGN KEY (movie_id) REFERENCES movie (id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO genre (id, name) VALUES (1, 'ACTION');
INSERT INTO genre (id, name) VALUES (2, 'ADVENTURE');
INSERT INTO genre (id, name) VALUES (3, 'ANIMATION');
INSERT INTO genre (id, name) VALUES (4, 'BIOGRAPHY');
INSERT INTO genre (id, name) VALUES (5, 'COMEDY');
INSERT INTO genre (id, name) VALUES (6, 'CRIME');
INSERT INTO genre (id, name) VALUES (7, 'DOCUMENTARY');
INSERT INTO genre (id, name) VALUES (8, 'DRAMA');
INSERT INTO genre (id, name) VALUES (9, 'FAMILY');
INSERT INTO genre (id, name) VALUES (10, 'FANTASY');
INSERT INTO genre (id, name) VALUES (11, 'FILM_NOIR');
INSERT INTO genre (id, name) VALUES (12, 'HISTORY');
INSERT INTO genre (id, name) VALUES (13, 'HORROR');
INSERT INTO genre (id, name) VALUES (14, 'MUSIC');
INSERT INTO genre (id, name) VALUES (15, 'MUSICAL');
INSERT INTO genre (id, name) VALUES (16, 'MYSTERY');
INSERT INTO genre (id, name) VALUES (17, 'ROMANCE');
INSERT INTO genre (id, name) VALUES (18, 'SCI_FI');
INSERT INTO genre (id, name) VALUES (19, 'SPORT');
INSERT INTO genre (id, name) VALUES (20, 'THRILLER');
INSERT INTO genre (id, name) VALUES (21, 'WAR');
INSERT INTO genre (id, name) VALUES (22, 'WESTERN');