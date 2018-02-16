-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Ven 16 Février 2018 à 12:46
-- Version du serveur :  5.5.58-0+deb8u1
-- Version de PHP :  5.6.33-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `ghallab_fodil`
--

-- --------------------------------------------------------

--
-- Structure de la table `friend`
--

CREATE TABLE IF NOT EXISTS `friend` (
`id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_friend` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `session`
--

CREATE TABLE IF NOT EXISTS `session` (
`id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `skey` varchar(32) CHARACTER SET utf8 NOT NULL,
  `sdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `root` tinyint(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `session`
--

INSERT INTO `session` (`id`, `id_user`, `skey`, `sdate`, `root`) VALUES
(1, 2, 'eb047e853a7a4647a56e72f7b8775418', '2018-02-14 16:42:11', 0);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`id` int(11) NOT NULL,
  `login` varchar(255) NOT NULL,
  `pwd` blob NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`id`, `login`, `pwd`, `nom`, `prenom`, `mail`) VALUES
(2, 'toto', 0x2a35334339343635363938333546434446343330383035443641383131344231344141314342313643, 'Du moulin', 'jean', 'malabar@pop');

--
-- Index pour les tables exportées
--

--
-- Index pour la table `friend`
--
ALTER TABLE `friend`
 ADD PRIMARY KEY (`id`), ADD KEY `id_user` (`id_user`,`id_friend`), ADD KEY `id_friend` (`id_friend`);

--
-- Index pour la table `session`
--
ALTER TABLE `session`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `skey` (`skey`), ADD KEY `id_user` (`id_user`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `login` (`login`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `friend`
--
ALTER TABLE `friend`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `session`
--
ALTER TABLE `session`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `friend`
--
ALTER TABLE `friend`
ADD CONSTRAINT `id_friend_friend` FOREIGN KEY (`id_friend`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `id_user_friend` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `session`
--
ALTER TABLE `session`
ADD CONSTRAINT `id_user_session` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
