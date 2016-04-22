CREATE TABLE IF NOT EXISTS `movie` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `imdb_id` varchar(255) DEFAULT NULL,
  `tagline` varchar(255) DEFAULT NULL,
  `overview` varchar(255) DEFAULT NULL,
  `runtime` int(10) unsigned DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `genre` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `movie_genre` (
  `movie_id` varchar(255) NOT NULL,
  `genre_id` varchar(255) NOT NULL,
  PRIMARY KEY (`movie_id`,`genre_id`),
  KEY `genre_id` (`genre_id`),
  CONSTRAINT `movie_genre_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE,
  CONSTRAINT `movie_genre_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS `video_file` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `format` varchar(10) NOT NULL,
  `size` int(10) unsigned DEFAULT NULL,
  `path` varchar(255) NOT NULL,
  `quality` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `movie_video_file` (
  `movie_id` varchar(255) NOT NULL,
  `video_file_id` varchar(255) NOT NULL,
  PRIMARY KEY (`movie_id`,`video_file_id`),
  KEY `video_file_id` (`video_file_id`),
  CONSTRAINT `movie_video_file_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE,
  CONSTRAINT `movie_video_file_ibfk_2` FOREIGN KEY (`video_file_id`) REFERENCES `video_file` (`id`) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS `image_file` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `format` varchar(10) DEFAULT NULL,
  `path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `person` (
  `id` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `job` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `person_movie_job` (
  `person_id` varchar(255) NOT NULL,
  `movie_id` varchar(255) NOT NULL,
  `job_id` varchar(255) NOT NULL,
  PRIMARY KEY (`person_id`,`movie_id`,`job_id`),
  KEY `movie_id` (`movie_id`),
  KEY `job_id` (`job_id`),
  CONSTRAINT `person_movie_job_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE,
  CONSTRAINT `person_movie_job_ibfk_2` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE,
  CONSTRAINT `person_movie_job_ibfk_3` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`) ON DELETE CASCADE
);