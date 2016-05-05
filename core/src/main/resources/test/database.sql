CREATE TABLE IF NOT EXISTS movie (
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  imdb_id VARCHAR(255) DEFAULT NULL,
  tagline VARCHAR(255) DEFAULT NULL,
  overview VARCHAR(255) DEFAULT NULL,
  runtime SMALLINT UNSIGNED DEFAULT NULL,
  release_date DATE DEFAULT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS genre (
  id TINYINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS movie_genre (
  movie_id VARCHAR(255) NOT NULL,
  genre_id TINYINT NOT NULL,
  PRIMARY KEY (movie_id,genre_id),
  KEY genre_id (genre_id),
  CONSTRAINT movie_genre_ibfk_1 FOREIGN KEY (movie_id) REFERENCES movie (id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS video_file (
  id VARCHAR(255) NOT NULL,
  path VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS movie_video_file (
  movie_id VARCHAR(255) NOT NULL,
  video_file_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (movie_id,video_file_id),
  KEY video_file_id (video_file_id),
  CONSTRAINT movie_video_file_ibfk_1 FOREIGN KEY (movie_id) REFERENCES movie (id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS image_file (
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) DEFAULT NULL,
  fileType VARCHAR(10) DEFAULT NULL,
  path VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS person (
  id VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  date_of_birth DATE NOT NULL,
  gender VARCHAR(15) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS job (
  id TINYINT NOT NULL,
  name VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS person_movie_job (
  movie_id VARCHAR(255) NOT NULL,
  person_id VARCHAR(255) NOT NULL,
  job_id TINYINT NOT NULL,
  PRIMARY KEY (movie_id, person_id, job_id),
  KEY person_id (person_id),
  KEY job_id (job_id),
  CONSTRAINT person_movie_job_ibfk_2 FOREIGN KEY (movie_id) REFERENCES movie (id) ON UPDATE CASCADE ON DELETE CASCADE
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