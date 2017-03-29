class Model_Case
{
    private int row;
    private int column;
    private Model_Plateau plateau;
    private Model_Pion pion;
    private final int COULEUR;

    /**
     * COnstructeur du model d'une case
     * @param row (ligne à laquelle se trouve la case courante)
     * @param column (colonne à laquelle se trouve la case courante
     * @param COULEUR (couleur de la case courante)
     */
    Model_Case(int row, int column, int COULEUR)
    {
        this.COULEUR = COULEUR;
        this.row = row;
        this.column = column;
    }

    /**
     * Ajoute un pion sur la case courante
     * @param pion (pion a ajouter)
     */
    void addPion(Model_Pion pion)
    {
        this.pion = pion;
        this.pion.setCaseActuelle(this);
    }

    /**
     * Supprime le pion présent sur la case courante
     */
    void removePion() { pion = null; }

    /**
     * Indice où se trouve la case courante dans le tableau de case du plateau courant
     * @return (indice de la case courante en chaîne de caractère)
     */
    @Override
    public String toString() { return column+row*Model_Plateau.LIGNE + " "; }




    /**
     * Initialisation des cases du plateau
     */
    public static Model_Case[] initCasesPlateau()
    {
        Model_Case[] board = new Model_Case[Model_Plateau.LIGNE * Model_Plateau.LIGNE];

        board[0]= new Model_Case(0, 0, Couleur.MARRON);
        board[1]= new Model_Case(0, 1, Couleur.VERT);
        board[2]= new Model_Case(0, 2, Couleur.ROUGE);
        board[3]= new Model_Case(0, 3, Couleur.JAUNE);
        board[4]= new Model_Case(0, 4, Couleur.ROSE);
        board[5]= new Model_Case(0, 5, Couleur.VIOLET);
        board[6]= new Model_Case(0, 6, Couleur.BLEU);
        board[7]= new Model_Case(0, 7, Couleur.ORANGE);

        board[8] = new Model_Case(1, 0, Couleur.VIOLET);
        board[9] = new Model_Case(1, 1, Couleur.MARRON);
        board[10]= new Model_Case(1, 2, Couleur.JAUNE);
        board[11]= new Model_Case(1, 3, Couleur.BLEU);
        board[12]= new Model_Case(1, 4, Couleur.VERT);
        board[13]= new Model_Case(1, 5, Couleur.ROSE);
        board[14]= new Model_Case(1, 6, Couleur.ORANGE);
        board[15]= new Model_Case(1, 7, Couleur.ROUGE);

        board[16]= new Model_Case(2, 0, Couleur.BLEU);
        board[17]= new Model_Case(2, 1, Couleur.JAUNE);
        board[18]= new Model_Case(2, 2, Couleur.MARRON);
        board[19]= new Model_Case(2, 3, Couleur.VIOLET);
        board[20]= new Model_Case(2, 4, Couleur.ROUGE);
        board[21]= new Model_Case(2, 5, Couleur.ORANGE);
        board[22]= new Model_Case(2, 6, Couleur.ROSE);
        board[23]= new Model_Case(2, 7, Couleur.VERT);

        board[24]= new Model_Case(3, 0, Couleur.JAUNE);
        board[25]= new Model_Case(3, 1, Couleur.ROUGE);
        board[26]= new Model_Case(3, 2, Couleur.VERT);
        board[27]= new Model_Case(3, 3, Couleur.MARRON);
        board[28]= new Model_Case(3, 4, Couleur.ORANGE);
        board[29]= new Model_Case(3, 5, Couleur.BLEU);
        board[30]= new Model_Case(3, 6, Couleur.VIOLET);
        board[31]= new Model_Case(3, 7, Couleur.ROSE);

        board[32]= new Model_Case(4, 0, Couleur.ROSE);
        board[33]= new Model_Case(4, 1, Couleur.VIOLET);
        board[34]= new Model_Case(4, 2, Couleur.BLEU);
        board[35]= new Model_Case(4, 3, Couleur.ORANGE);
        board[36]= new Model_Case(4, 4, Couleur.MARRON);
        board[37]= new Model_Case(4, 5, Couleur.VERT);
        board[38]= new Model_Case(4, 6, Couleur.ROUGE);
        board[39]= new Model_Case(4, 7, Couleur.JAUNE);

        board[40]= new Model_Case(5, 0, Couleur.VERT);
        board[41]= new Model_Case(5, 1, Couleur.ROSE);
        board[42]= new Model_Case(5, 2, Couleur.ORANGE);
        board[43]= new Model_Case(5, 3, Couleur.ROUGE);
        board[44]= new Model_Case(5, 4, Couleur.VIOLET);
        board[45]= new Model_Case(5, 5, Couleur.MARRON);
        board[46]= new Model_Case(5, 6, Couleur.JAUNE);
        board[47]= new Model_Case(5, 7, Couleur.BLEU);

        board[48]= new Model_Case(6, 0, Couleur.ROUGE);
        board[49]= new Model_Case(6, 1, Couleur.ORANGE);
        board[50]= new Model_Case(6, 2, Couleur.ROSE);
        board[51]= new Model_Case(6, 3, Couleur.VERT);
        board[52]= new Model_Case(6, 4, Couleur.BLEU);
        board[53]= new Model_Case(6, 5, Couleur.JAUNE);
        board[54]= new Model_Case(6, 6, Couleur.MARRON);
        board[55]= new Model_Case(6, 7, Couleur.VIOLET);

        board[56]= new Model_Case(7, 0, Couleur.ORANGE);
        board[57]= new Model_Case(7, 1, Couleur.BLEU);
        board[58]= new Model_Case(7, 2, Couleur.VIOLET);
        board[59]= new Model_Case(7, 3, Couleur.ROSE);
        board[60]= new Model_Case(7, 4, Couleur.JAUNE);
        board[61]= new Model_Case(7, 5, Couleur.ROUGE);
        board[62]= new Model_Case(7, 6, Couleur.VERT);
        board[63]= new Model_Case(7, 7, Couleur.MARRON);

        return board;
    }

    // GETTERS & SETTERS
    Model_Pion getPion() { return pion; }
    int getCOULEUR() { return COULEUR; }
    Model_Plateau getPlateau() { return plateau; }
    void setPlateau(Model_Plateau modPlateau) { plateau = modPlateau; }
    int getRow() { return row; }
    int getColumn() { return column; }
    public void setRow(int row) { this.row = row; }
    void setPion(Model_Pion pion) { this.pion = pion; }
}

