class Model_Plateau
{
    private Model_Case[] board;
    final static int LIGNE = 8;

    /**
     * Constructeur initialisant l'état du plateau au début d'une partie.
     */
    Model_Plateau(Model_Case[] board)
    {
        this.board = board;
    }

    /**
     * Déplace un pion du point de vue du plateau
     * @param caseSource (case où était initialement le pion)
     * @param caseDestination (case où sur laquelle on déplace le pion)
     * @param pion (pion qui est concerné par ce déplacement)
     */
    void deplacer(Model_Case caseSource, Model_Case caseDestination, Model_Pion pion)
    {
        caseSource.removePion();
        caseDestination.addPion(pion);
    }

    // GETTERS & SETTERS
    Model_Case[] getBoard() {
        return board;
    }
}
