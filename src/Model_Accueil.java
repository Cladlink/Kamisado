import java.util.ArrayList;

class Model_Accueil
{
    private Model_Partie partie;
    private Model_Partie_IA partieIa;
    private String partieAVisualiser;
    private String pseudoJoueurBlanc;
    private String pseudoJoueurNoir;
    private String pseudoChoisi;
    private String partieACharger;
    private String langue;
    private boolean ia = false;
    private String adresseIpReseau;


    /**
     * Constructeur du model relatif à l'accueil
     */
    Model_Accueil()
    {
        partie = null;
        partieIa = null;
        partieAVisualiser = "";
        pseudoJoueurBlanc = "";
        pseudoJoueurNoir = "";
        pseudoChoisi = "";
        partieACharger = "";
        langue = "fr";
    }


    /**
     * Instanciation d'une partie
     */
    void demarrerPartie()
    {
        Model_Case[] board = Model_Case.initCasesPlateau();
        Model_Pion[] pionsBlancs = Model_Pion.creationPionsBlancs(board);
        Model_Pion[] pionsNoirs = Model_Pion.creationPionsNoirs(board);

        for(int i=0; i<Model_Plateau.LIGNE; i++) board[i].addPion(pionsBlancs[i]);
        for(int i=0; i<pionsNoirs.length; i++) board[56+i].addPion(pionsNoirs[i]);
        int idBlanc, idNoir;
        BDDManager bdd = new BDDManager();
        bdd.start();
        idBlanc = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + pseudoJoueurBlanc + "';").get(0).get(0));
        idNoir = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + pseudoJoueurNoir + "';").get(0).get(0));
        Model_Joueur joueurBlanc = new Model_Joueur(pseudoJoueurBlanc, true, idBlanc);
        Model_Joueur joueurNoir = new Model_Joueur(pseudoJoueurNoir, false, idNoir);
        this.partie = Model_Partie.factPartie(this, joueurBlanc, joueurNoir, board, pionsBlancs, pionsNoirs, null, true);

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);
        bdd.stop();
    }
    /**
     * Instanciation d'une partie
     */
    void demarrerPartieReseau(String pseudoAdversaire, String monPseudo, boolean b)
    {
        Model_Case[] board = Model_Case.initCasesPlateau();
        Model_Pion[] pionsBlancs = Model_Pion.creationPionsBlancs(board);
        Model_Pion[] pionsNoirs = Model_Pion.creationPionsNoirs(board);

        for(int i=0; i<Model_Plateau.LIGNE; i++) board[i].addPion(pionsBlancs[i]);
        for(int i=0; i<pionsNoirs.length; i++) board[56+i].addPion(pionsNoirs[i]);
        int idBlanc, idNoir;
        BDDManager bdd = new BDDManager();
        bdd.start();
        idBlanc = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + monPseudo + "';").get(0).get(0));
        idNoir = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + pseudoAdversaire + "';").get(0).get(0));
        Model_Joueur joueurBlanc = new Model_Joueur(monPseudo, true, idBlanc);
        Model_Joueur joueurNoir = new Model_Joueur(pseudoAdversaire, false, idNoir);
        this.partie = Model_Partie.factPartie(this, joueurBlanc, joueurNoir, board, pionsBlancs, pionsNoirs, null, true);
        // ajout SD
        if (partie.isTourDuJoueurBlanc())
            partie.setIdCurrentPlayer(1);
        else
            partie.setIdCurrentPlayer(2);
        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);

        bdd.stop();
    }

    void demarrerPartieRapide()
    {
        Model_Case[] board = Model_Case.initCasesPlateau();
        Model_Pion[] pionsBlancs = Model_Pion.creationPionsBlancs(board);
        Model_Pion[] pionsNoirs = Model_Pion.creationPionsNoirs(board);

        for(int i=0; i<Model_Plateau.LIGNE; i++) board[i].addPion(pionsBlancs[i]);
        for(int i=0; i<pionsNoirs.length; i++) board[56+i].addPion(pionsNoirs[i]);
        int idBlanc, idNoir;
        idBlanc = 0;
        idNoir = 1;
        Model_Joueur joueurBlanc = new Model_Joueur("Joueur Blanc", true, idBlanc);
        Model_Joueur joueurNoir = new Model_Joueur("Joueur Noir", false, idNoir);
        this.partie = Model_Partie.factPartie(this, joueurBlanc, joueurNoir, board, pionsBlancs, pionsNoirs, null, true);

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);
    }

    void demarrerPartieFictive()
    {
        Model_Case[] board = Model_Case.initCasesPlateau();
        Model_Pion[] pionsBlancs = Model_Pion.creationPionsBlancs(board);
        Model_Pion[] pionsNoirs = Model_Pion.creationPionsNoirs(board);

        for(int i=0; i<Model_Plateau.LIGNE; i++) board[i].addPion(pionsBlancs[i]);
        for(int i=0; i<pionsNoirs.length; i++) board[56+i].addPion(pionsNoirs[i]);
        int idBlanc, idNoir;
        pseudoJoueurBlanc = partieAVisualiser.split(" ")[0];
        pseudoJoueurNoir = partieAVisualiser.split(" ")[2];
        BDDManager bdd = new BDDManager();
        bdd.start();
        idBlanc = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + pseudoJoueurBlanc + "';").get(0).get(0));
        idNoir = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + pseudoJoueurNoir + "';").get(0).get(0));
        Model_Joueur joueurBlanc = new Model_Joueur(pseudoJoueurBlanc, true, idBlanc);
        Model_Joueur joueurNoir = new Model_Joueur(pseudoJoueurNoir, false, idNoir);
        this.partie = Model_Partie.factPartie(this, joueurBlanc, joueurNoir, board, pionsBlancs, pionsNoirs, null, true);

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);
    }

    void demarrerPartieChargee()
    {
        Model_Case[] board = Model_Case.initCasesPlateau();
        Model_Pion[] pionsBlancs = Model_Pion.creationPionsBlancs(board);
        Model_Pion[] pionsNoirs = Model_Pion.creationPionsNoirs(board);

        BDDManager bdd = new BDDManager();
        bdd.start();

        int idBlanc, idNoir;

        idBlanc = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + pseudoJoueurBlanc + "';").get(0).get(0));
        idNoir = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + pseudoJoueurNoir + "';").get(0).get(0));

        String etatPlateau = bdd.ask("SELECT etatPlateauSave FROM SAUVEGARDEPARTIE WHERE joueurBlancSave = " + idBlanc).get(0).get(0);
        boolean turn = Boolean.getBoolean(bdd.ask("SELECT tourSave FROM SAUVEGARDEPARTIE WHERE joueurBlancSave = " + idBlanc).get(0).get(0));
        String history = bdd.ask("" +
                "SELECT coupsJouee" +
                " FROM HISTORIQUEPARTIE" +
                " JOIN SAUVEGARDEPARTIE ON HISTORIQUEPARTIE.id = SAUVEGARDEPARTIE.Historique_id" +
                " WHERE joueurBlancSave = " + idBlanc).get(0).get(0);

        String[] placesPions = etatPlateau.split(",");

        for(int i=0; i<placesPions.length; i++)
        {
            if (placesPions[i].charAt(0) != ' ' && placesPions[i].charAt(1) == 'b')
            {
                for (Model_Pion pionsBlanc : pionsBlancs)
                {
                    if (pionsBlanc.getCOULEUR() == Character.getNumericValue(placesPions[i].charAt(0))) {
                        board[i].setPion(pionsBlanc);
                        pionsBlanc.setCaseActuelle(board[i]);
                    }
                }
            }
            else if (placesPions[i].charAt(0) != ' ' && placesPions[i].charAt(1) == 'n')
            {
                for (Model_Pion pionsNoir : pionsNoirs)
                {
                    if (pionsNoir.getCOULEUR() == Character.getNumericValue(placesPions[i].charAt(0)))
                    {
                        board[i].setPion(pionsNoir);
                        pionsNoir.setCaseActuelle(board[i]);
                    }
                }
            }
        }

        Model_Joueur joueurBlanc = new Model_Joueur(pseudoJoueurBlanc, true, idBlanc);
        Model_Joueur joueurNoir = new Model_Joueur(pseudoJoueurNoir, false, idNoir);
        this.partie = Model_Partie.factPartie(this, joueurBlanc, joueurNoir, board, pionsBlancs, pionsNoirs, null, true);

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);
        partie.setTourDuJoueurBlanc(turn);

        String[] tabCoupDecoupe = history.split(",");
        int rowArrivee = Character.getNumericValue(tabCoupDecoupe[tabCoupDecoupe.length-1].charAt(2));
        int columnArrivee = Character.getNumericValue(tabCoupDecoupe[tabCoupDecoupe.length-1].charAt(3));

        partie.setDernierPionJoue(board[rowArrivee*Model_Plateau.LIGNE+columnArrivee].getPion());
        partie.setHistory(history);
        partie.setTourUn(false);
        partie.setEstPartieChargee(true);
    }

    /**
     * initie la partie contre l'IA
     */
    void demarrerPartieContreLIA()
    {
        ia = true;
        partieIa = new Model_Partie_IA();
    }
    /**
     * lancementPartieReseau
     *
     *
     */
    void initPartieReseau()
    {
        this.partie = new Model_Partie();
    }

    void ajouterNouveauJoueur(String pseudo)
    {
        BDDManager bdd = new BDDManager();
        bdd.start();
        bdd.edit("INSERT INTO JOUEUR VALUES (null, \"" + pseudo + "\", 0, 0);");
        bdd.stop();
    }

    String[] listePseudos()
    {
        BDDManager bdd = new BDDManager();
        bdd.start();
        ArrayList<ArrayList<String>> joueurs = bdd.ask("SELECT JOUEUR.pseudoJoueur FROM JOUEUR;");
        ArrayList<String> listeJoueurs = new ArrayList<>();
        for (ArrayList<String> joueur : joueurs)
            for (String aJoueur : joueur)
                listeJoueurs.add(aJoueur);
        String[] liste = new String[listeJoueurs.size()];
        for(int i=0; i<listeJoueurs.size(); i++)
            liste[i] = listeJoueurs.get(i);

        bdd.stop();
        return liste;
    }

    // GETTERS & SETTERS
    Model_Partie getPartie() { return partie; }
    boolean getia() { return ia; }
    Model_Partie_IA getPartieIa() { return partieIa; }
    void setPartieAVisualiser(String partieAVisualiser) {
        this.partieAVisualiser = partieAVisualiser;
    }
    String getPartieAVisualiser() {
        return partieAVisualiser;
    }
    void setPseudoJoueurBlanc(String pseudoJoueurBlanc) {
        this.pseudoJoueurBlanc = pseudoJoueurBlanc;
    }
    void setPseudoJoueurNoir(String pseudoJoueurNoir) {
        this.pseudoJoueurNoir = pseudoJoueurNoir;
    }
    String getPseudoJoueurBlanc() {
        return pseudoJoueurBlanc;
    }
    String getPseudoJoueurNoir() {
        return pseudoJoueurNoir;
    }
    String getPseudoChoisi() { return pseudoChoisi; }
    void setPseudoChoisi(String pseudoChoisi) {
        this.pseudoChoisi = pseudoChoisi;
    }
    String getPartieACharger() {
        return partieACharger;
    }
    void setPartieACharger(String partieACharger) {
        this.partieACharger = partieACharger;
    }
    String getLangue() {
        return langue;
    }
    void setLangue(String langue) {
        this.langue = langue;
    }
    void setAdresseIpReseau(String adresseIpReseau) { this.adresseIpReseau = adresseIpReseau; }
    String getAdresseIpReseau() { return adresseIpReseau; }
}