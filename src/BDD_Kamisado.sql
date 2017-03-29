drop table if exists SAUVEGARDEPARTIE;
drop table if exists HISTORIQUEPARTIE;
drop table if exists JOUEUR;
CREATE TABLE JOUEUR
(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	pseudoJoueur VARCHAR(100),
	nbPartiesGagneesJoueur INT(100),
	nbPartiesPerduesJoueur INT(100)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE HISTORIQUEPARTIE
(
	id INT(100) NOT NULL AUTO_INCREMENT,
	joueurBlanc_id INT(100),
	joueurNoir_id INT(100),
	coupsJouee text,
	dateHistorique DATETIME,
	CONSTRAINT fk_historique_joueurBlanc FOREIGN KEY (joueurBlanc_id) REFERENCES JOUEUR(id),
	CONSTRAINT fk_historique_joueurNoir FOREIGN KEY (joueurNoir_id) REFERENCES JOUEUR(id),
	PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE SAUVEGARDEPARTIE
(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	joueurBlancSave INT(100),
	joueurNoirSave INT(100),
	tourSave boolean,
	etatPlateauSave TEXT,
	Historique_id INT(100),
	CONSTRAINT fk_sauvegarde_joueurBlanc FOREIGN KEY (joueurBlancSave) REFERENCES JOUEUR(id),
	CONSTRAINT fk_sauvegarde_joueurNoir FOREIGN KEY (joueurNoirSave) REFERENCES JOUEUR(id),
	CONSTRAINT fk_sauvegarde_historique FOREIGN KEY (Historique_id) REFERENCES HISTORIQUEPARTIE(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8

