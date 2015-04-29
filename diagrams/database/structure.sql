SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

CREATE DATABASE IF NOT EXISTS `heig_party` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `heig_party`;

CREATE TABLE IF NOT EXISTS `game` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Identifiant de la partie',
  `creator` int(10) unsigned NOT NULL COMMENT 'Créateur de la partie',
  `difficulty` enum('EASY','MEDIUM','HARD') COLLATE utf8_bin NOT NULL DEFAULT 'EASY' COMMENT 'Difficulté du jeu',
  `squares` int(10) unsigned NOT NULL COMMENT 'Nombre de cases sur le plateau',
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Date de création de la partie',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Liste des parties créées' AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `mini_game` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID du mini-jeu',
  `name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Nom du mini-jeu',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Mini-Jeux disponibles' AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `player` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Identifiant du joueur',
  `username` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'Pseudonyme du joueur',
  `password` char(40) COLLATE utf8_bin NOT NULL COMMENT 'Hash SHA-1 du mot de passe de l''utilisateur',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickname` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `player_game` (
  `game` int(10) unsigned NOT NULL COMMENT 'Partie à laquelle ce joueur est associé',
  `player` int(10) unsigned NOT NULL COMMENT 'Identifiant d''un joueur auquel cette partie est associée'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `result` (
  `player` int(10) unsigned NOT NULL COMMENT 'Identifiant du joueur auquel est associé ce score',
  `turn` int(10) unsigned NOT NULL COMMENT 'Identifiant du tour auquel est associé ce score',
  `score` int(10) unsigned NOT NULL COMMENT 'Score qu''a effectué le joueur sur ce tour',
  `dice_score` tinyint(3) unsigned NOT NULL COMMENT 'Résultat du lancé de dé pour le joueur dans ce tour'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `turn` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'identifiant du tour',
  `game` int(10) unsigned NOT NULL COMMENT 'Partie à laquelle est associé ce tour',
  `seed` bigint(20) NOT NULL COMMENT 'Seed généré pour la partie',
  `mini_game` int(10) unsigned NOT NULL COMMENT 'Mini-jeu joué lors de ce tour',
  PRIMARY KEY (`id`),
  KEY `game` (`game`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
