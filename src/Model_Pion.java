import java.util.ArrayList;

class Model_Pion
{
    private Model_Case caseActuelle;
    private boolean estBlanc;
    private final int COULEUR;
    private ArrayList<Model_Case> casesAtteignables;
    private int relX, relY;
    private int incX, incY;

    /**
     * Constructeur d'un pion
     * @param caseInitiale (case où se trouve le pion au début de la partie)
     * @param estBlanc (indique à quel joueur appartient le pion)
     * @param COULEUR (indique de quelle couleur est ce pion)
     */
    Model_Pion(Model_Case caseInitiale, boolean estBlanc, int COULEUR)
    {
        this.estBlanc = estBlanc;
        this.caseActuelle = caseInitiale;
        this.COULEUR = COULEUR;
        this.casesAtteignables = new ArrayList<>();
    }

    /**
     * Modifie la case où se trouve actuellement le pion
     * @param nouvelleCaseActuelle (case où va se retrouver le pion)
     */
    void setCaseActuelle(Model_Case nouvelleCaseActuelle)
    {
        this.caseActuelle = nouvelleCaseActuelle;
    }

    /**
     * Liste les cases où la pièce peut se déplacer
     */
    void casesAtteignables()
    {
        casesAtteignables.clear();

        Model_Case[] plateau = caseActuelle.getPlateau().getBoard();
        int row = caseActuelle.getRow(), column = caseActuelle.getColumn();
        int decal, ligne = Model_Plateau.LIGNE;
        boolean deplacableNS = true, deplacableO = true, deplacableE = true;

        decal = estBlanc?1:-1;
        while (deplacableNS || deplacableO || deplacableE)
        {
            if (deplacableNS)
            {
                if( row + decal <=7
                        && row + decal >=0
                        && plateau[column + (row + decal) * ligne].getPion() == null)
                    casesAtteignables.add(plateau[column + (row + decal) * ligne]);
                else
                    deplacableNS = false;
            }
            if (deplacableO)
            {
                if( row + decal <=7 && column - decal >= 0
                        && row + decal >=0 && column - decal <=7
                        && plateau[(column - decal) + (row + decal) * ligne].getPion() == null)
                    casesAtteignables.add(plateau[(column - decal) + (row + decal) * ligne]);
                else
                    deplacableO = false;
            }
            if (deplacableE)
            {
                if( row + decal <=7 && column + decal <= 7
                        && row + decal >=0 && column + decal >=0
                        && plateau[(column + decal) + (row + decal) * ligne].getPion() == null)
                    casesAtteignables.add(plateau[(column + decal) + (row + decal) * ligne]);
                else
                    deplacableE= false;
            }
            decal = estBlanc?decal+1:decal-1;
        }
    }

    /**
     * Met en chaine de caractère les caractéristiques du pion
     * @return (chaine de caractère qui décrit le pion)
     */
    public String toString()
    {
        String joueur = estBlanc? "blanc": "noir";
        return "pion " + COULEUR + " du joueur " + joueur;
    }

    /**
     * Creation des pions blancs, en leur passant leur case en parametre
     * @param board
     * @return le tableau de pions
     */
    static Model_Pion[] creationPionsBlancs(Model_Case[] board)
    {

        Model_Pion[] pionsBlancs = new Model_Pion[Model_Plateau.LIGNE];
        pionsBlancs[0] = new Model_Pion(board[0],true,Couleur.MARRON);
        pionsBlancs[1] = new Model_Pion(board[1],true,Couleur.VERT);
        pionsBlancs[2] = new Model_Pion(board[2],true,Couleur.ROUGE);
        pionsBlancs[3] = new Model_Pion(board[3],true,Couleur.JAUNE);
        pionsBlancs[4] = new Model_Pion(board[4],true,Couleur.ROSE);
        pionsBlancs[5] = new Model_Pion(board[5],true,Couleur.VIOLET);
        pionsBlancs[6] = new Model_Pion(board[6],true,Couleur.BLEU);
        pionsBlancs[7] = new Model_Pion(board[7],true,Couleur.ORANGE);

        return pionsBlancs;
    }


    /**
     * Creation des pions noirs, en leur passant leur case en parametre
     * @param board
     * @return le tableau de pions
     */
    static Model_Pion[] creationPionsNoirs(Model_Case[] board)
    {
        Model_Pion[] pionsNoirs = new Model_Pion[Model_Plateau.LIGNE];

        pionsNoirs[0] = new Model_Pion(board[56],false,Couleur.ORANGE);
        pionsNoirs[1] = new Model_Pion(board[57],false,Couleur.BLEU);
        pionsNoirs[2] = new Model_Pion(board[58],false,Couleur.VIOLET);
        pionsNoirs[3] = new Model_Pion(board[59],false,Couleur.ROSE);
        pionsNoirs[4] = new Model_Pion(board[60],false,Couleur.JAUNE);
        pionsNoirs[5] = new Model_Pion(board[61],false,Couleur.ROUGE);
        pionsNoirs[6] = new Model_Pion(board[62],false,Couleur.VERT);
        pionsNoirs[7] = new Model_Pion(board[63],false,Couleur.MARRON);

        return pionsNoirs;
    }

    // GETTERS & SETTERS
    int getCOULEUR() { return COULEUR; }
    Model_Case getCaseActuelle() { return caseActuelle; }
    ArrayList<Model_Case> getCasesAtteignables() { return casesAtteignables; }
    boolean isEstBlanc() { return estBlanc; }
    int getRelX() { return relX; }
    int getRelY() { return relY; }
    int getIncX() { return incX; }
    int getIncY() { return incY; }
    void setRelX(int relX) { this.relX = relX; }
    void setRelY(int relY) { this.relY = relY; }
    void setIncX(int incX) { this.incX = incX; }
    void setIncY(int incY) { this.incY = incY; }
}