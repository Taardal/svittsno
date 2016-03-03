CREATE TABLE `movie` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `imdb_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tagline` varchar(255) DEFAULT NULL,
  `overview` varchar(500) DEFAULT NULL,
  `runtime` int(10) unsigned DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `genre` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `video_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `format` varchar(10) NOT NULL,
  `size` int(10) unsigned DEFAULT NULL,
  `path` varchar(255) NOT NULL,
  `quality` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `image_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `format` varchar(10) DEFAULT NULL,
  `path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `person` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `gender` (
  `gender` char(1) NOT NULL,
  PRIMARY KEY (`gender`)
);

CREATE TABLE `role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `movie_genre` (
  `movie_id` int(10) unsigned NOT NULL,
  `genre_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`movie_id`,`genre_id`),
  KEY `genre_id` (`genre_id`),
  CONSTRAINT `movie_genre_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`),
  CONSTRAINT `movie_genre_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`)
);

CREATE TABLE `movie_video_file` (
  `movie_id` int(10) unsigned NOT NULL,
  `video_file_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`movie_id`,`video_file_id`),
  KEY `video_file_id` (`video_file_id`),
  CONSTRAINT `movie_video_file_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`),
  CONSTRAINT `movie_video_file_ibfk_2` FOREIGN KEY (`video_file_id`) REFERENCES `video_file` (`id`)
);

CREATE TABLE `person_gender` (
  `person_id` int(10) unsigned NOT NULL,
  `gender` char(1) NOT NULL,
  PRIMARY KEY (`person_id`,`gender`),
  KEY `gender` (`gender`),
  CONSTRAINT `person_gender_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `person_gender_ibfk_2` FOREIGN KEY (`gender`) REFERENCES `gender` (`gender`)
);

CREATE TABLE `person_movie_role` (
  `person_id` int(10) unsigned NOT NULL,
  `movie_id` int(10) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`person_id`,`movie_id`,`role_id`),
  KEY `movie_id` (`movie_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `person_movie_role_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `person_movie_role_ibfk_2` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`),
  CONSTRAINT `person_movie_role_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
);