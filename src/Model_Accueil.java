class Model_Accueil
{
    private Model_Partie partie;

    /**
     * Constructeur du model relatif à l'accueil
     */
    Model_Accueil()
    {
        partie = null;
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

        Model_Joueur joueurBlanc = new Model_Joueur("blanc", true);
        Model_Joueur joueurNoir = new Model_Joueur("noir", false);
        this.partie = Model_Partie.factPartie(this, joueurBlanc, joueurNoir, board, pionsBlancs, pionsNoirs, null, true);

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);
    }

    // GETTERS & SETTERS
    Model_Partie getPartie() {
        return partie;
    }
}
