class Model_Joueur
{
    private final static BDDManager bdd = new BDDManager();
    private int id;
    private String nom;
    private Model_Partie model_partie;
    private boolean estJoueurBlanc;


    Model_Joueur(String nom, boolean joueurBlanc, int id)
    {
        this.id = id;
        this.nom = nom;
        estJoueurBlanc = joueurBlanc;
    }

    void gestionTourJoueur(int row, int column)
    {
        Model_Case casesPlateau[] = model_partie.getPlateau().getBoard();
        int ligne = Model_Plateau.LIGNE;
        //utiliser autre chose (sqrt(casesPlateau.length) par ex) si besoin d'un mock plus reduit

        if (model_partie.isTourUn()) // si tour un
        {
            if(casesPlateau[column+row*ligne].getPion() != null // si je clique un pion et qu'il est blanc
                    && estJoueurBlanc == casesPlateau[column+row*ligne].getPion().isEstBlanc() )
            {
                model_partie.setPionMemoire(casesPlateau[column+row*ligne].getPion()); // je mets le pion en mémoire
            }
            else if(casesPlateau[column+row*ligne].getPion() == null // si je clique sur une case sans pion
                    // et qu'il y a un pion en mémoire et que cette case est atteignable par le pion en mémoire
                    && model_partie.getPionMemoire() != null
                    && model_partie.getPionMemoire().getCasesAtteignables().contains(casesPlateau[column+row*ligne]))
            {
                model_partie.setTourUn(false); // le tour 1 est fini
                model_partie.deplacerPion(casesPlateau[column+row*ligne]); // je déplace mon pion
            }
        }
        else if(casesPlateau[column+row*ligne].getPion() == null // si c'est pas le tour 1
                // et que je clique sur une case sans pion accessible par le ion mémoire
                && model_partie.getPionMemoire().getCasesAtteignables().contains(casesPlateau[column+row*ligne]))
        {
            model_partie.verifieVictoire(row); // je vérifie si je gagne
            model_partie.deplacerPion(casesPlateau[column+row*ligne]); // je déplace mon pion
        }
    }

    public boolean isJoueurBlanc() {
        return estJoueurBlanc;
    }

    /**
     * Provenance : Echec
     * Met à jour les stats des participants d'une partie
     * @param JoueurGagnant
     * @param JoueurPerdant
     */
    static void ajouteVictoire(String JoueurGagnant, String JoueurPerdant)
    {
        bdd.start();
        bdd.edit("UPDATE JOUEUR " +
                "SET nbPartiesGagneesJoueur = nbPartiesGagneesJoueur+1 " +
                "WHERE pseudoJoueur = \"" + JoueurGagnant + "\";");
        bdd.edit("UPDATE JOUEUR " +
                "SET nbPartiesPerduesJoueur = nbPartiesPerduesJoueur+1 " +
                "WHERE pseudoJoueur = \"" + JoueurPerdant + "\";");
        bdd.stop();
    }

    // getters & setters
    public void setPartie(Model_Partie model_partie2V2) { this.model_partie = model_partie2V2; }
    String getNom() { return nom; }
    int getId() { return id; }
}
