import java.util.ArrayList;
import java.util.Random;

class Model_Partie
{
    private Model_Accueil accueil;

    private boolean isTourUn;
    private boolean tourDuJoueurBlanc;
    private boolean joueurBlancGagnant;

    private Model_Plateau plateau;
    private Model_Pion pionMemoire;
    private Model_Pion dernierPionJoue;
    private Model_Pion dernierPionJoueAnim;

    private Model_Pion[] pionsBlancs;
    private Model_Pion[] pionsNoirs;

    private Model_Joueur joueurBlanc;
    private Model_Joueur joueurNoir;

    private boolean estGagnee;
    private boolean estPartieChargee;
    private String history;
    private int idCurrentPlayer;
    private Model_Case caseSrc;
    private Model_Case caseDest;
    private boolean partieFinie;
    private boolean endOfTurn;
    private boolean joueurBLancGagnant;

    /**
     * Instancie les objets qui doivent l'être avant de
     * @param accueil (accueil du jeu)
     * @return (une nouvelle instance de Partie)
     */
    static Model_Partie factPartie(Model_Accueil accueil, Model_Joueur j1, Model_Joueur j2, Model_Case[] board, Model_Pion[] pionsBlancs, Model_Pion[] pionsNoirs,
                                   Model_Pion pionMemoire, boolean isTourUn)
    {


        Model_Plateau plateau = new Model_Plateau(board);        //passer en parametre si besoin de le mocker en testant la partie
        for(int i=0; i<board.length; i++) board[i].setPlateau(plateau);


        return new Model_Partie(accueil, plateau, j1, j2, pionsBlancs, pionsNoirs, pionMemoire, isTourUn);
    }
    /**
     * Partie
     * Constructeur de Partie vide en attente de toutes les informations de l'adversaire
     * UNIQUEMENT POUR LES PARTIES EN RESEAU
     *
     */
    Model_Partie()
    {
        idCurrentPlayer = 1;
        endOfTurn = false;
    }

    /**
     * Constructeur d'une partie sans spécificité
     */
    private Model_Partie(Model_Accueil accueil, Model_Plateau plateau, Model_Joueur j1, Model_Joueur j2,
                         Model_Pion[] pionsBlancs, Model_Pion[] pionsNoirs, Model_Pion pionMemoire, boolean isTourUn)
    {
        this.accueil = accueil;
        this.plateau = plateau;
        this.estGagnee = false;
        this.tourDuJoueurBlanc = true;
        this.isTourUn = isTourUn;
        this.pionMemoire = pionMemoire;

        this.pionsBlancs = pionsBlancs;
        this.pionsNoirs = pionsNoirs;

        joueurBlanc = j1;
        joueurNoir = j2;
        history = "";
        estPartieChargee = false;
    }

    /**
     * Définit les cases atteignables du pion qui va bouger en fonction du pion qui a bougé
     */
    synchronized void casesAtteignablesProchainTour()
    {
        Model_Pion[] pionsDuTour = tourDuJoueurBlanc?pionsBlancs:pionsNoirs;

        // Le premier tour est géré séparément car c'est un cas particulier où le joueur à la possibilité de choisir
        // le pion qu'il va bouger.
        if(isTourUn)
        {
            pionMemoire = null; // en cas de undo il faut remettre le pion memoire à null
            for(Model_Pion pionsTour : pionsDuTour) pionsTour.casesAtteignables();
        }
        else
        {
            int couleurDuPionQuiDoitBouger = dernierPionJoue.getCaseActuelle().getCOULEUR();
            for (Model_Pion aPionsDuTour : pionsDuTour)
                if (aPionsDuTour.getCOULEUR() == couleurDuPionQuiDoitBouger)
                {
                    pionMemoire = aPionsDuTour;
                    break;
                }
            pionMemoire.casesAtteignables();
        }
    }


    /**
     * undo()
     *
     * @return (if undo is ok)
     */
    synchronized boolean undo()
    {
        if(history.length() == 0)
            return false;
        String[] listesCoups = history.split(":");
        String dernierCoup = listesCoups[(listesCoups.length-1)];
        String[] tabCoupDecoupe = dernierCoup.split("");

        if (dernierCoup.equals("!"))
        {
            history = "";
            for (int i = 0; i < listesCoups.length-1; i++)
                history += listesCoups[i] + ":";

            tourDuJoueurBlanc = !tourDuJoueurBlanc;
            undo();
        }
        else
        {
            int rowDepart = Integer.parseInt(tabCoupDecoupe[0]);
            int columnDepart = Integer.parseInt(tabCoupDecoupe[1]);
            int rowArrivee = Integer.parseInt(tabCoupDecoupe[2]);
            int columnArrivee = Integer.parseInt(tabCoupDecoupe[3]);
            Model_Case caseDepart = plateau.getBoard()[rowDepart*Model_Plateau.LIGNE+columnDepart];
            Model_Case caseArrivee = plateau.getBoard()[rowArrivee*Model_Plateau.LIGNE+columnArrivee];
            Model_Pion pieceBougee = caseArrivee.getPion();
            plateau.deplacer(caseArrivee, caseDepart, pieceBougee);
            history = "";
            for (int i = 0; i < listesCoups.length-1; i++)
                history += listesCoups[i] + ":";
            if (history.length() == 0)
            {
                isTourUn = true;
                tourDuJoueurBlanc = !tourDuJoueurBlanc;
                casesAtteignablesProchainTour();
                return true;
            }

            tourDuJoueurBlanc = !tourDuJoueurBlanc;

            int rowDernierpionJoue = Character.getNumericValue(listesCoups[listesCoups.length-2].charAt(2));
            int colDernierpionJoue = Character.getNumericValue(listesCoups[listesCoups.length-2].charAt(3));
            dernierPionJoue = plateau.getBoard()[rowDernierpionJoue*Model_Plateau.LIGNE + colDernierpionJoue].getPion();
            casesAtteignablesProchainTour();
        }
        return true;
    }

    /**
     * Vérifie si nous sommes ou non dans une situation gagnante, donc de fin de partie
     * @param row (ligne sur laquelle a été déplacé le dernier pion joué)
     */
    synchronized void verifieVictoire(int row)
    {
        if((tourDuJoueurBlanc && row == 7)
                || (!tourDuJoueurBlanc && row == 0))
        {
            estGagnee = true;
            joueurBlancGagnant = tourDuJoueurBlanc;
        }
    }

    public void saveDerPionJoueAnim(){
        dernierPionJoueAnim = dernierPionJoue;
    }


    /**
     * Controle si un pion ne peut plus se déplacer et dans ce cas indique une situation gagnante
     */
    synchronized void controleBlocage()
    {
        if(pionMemoire.getCasesAtteignables().isEmpty())
        {
            history+="!:";
            boolean impossibleDeJouer = false;
            boolean joueurBlancGagnantSiJeuBloque = tourDuJoueurBlanc;
            ArrayList<Model_Pion> arrayPions = new ArrayList<>();
            arrayPions.add(pionMemoire);
            dernierPionJoue = pionMemoire;
            tourDuJoueurBlanc = !tourDuJoueurBlanc;
            casesAtteignablesProchainTour();
            while(pionMemoire.getCasesAtteignables().isEmpty() && !impossibleDeJouer)
            {
                if(arrayPions.contains(pionMemoire))
                    impossibleDeJouer = true;
                else
                {
                    arrayPions.add(pionMemoire);
                    dernierPionJoue = pionMemoire;
                    setTourDuJoueurBlanc(!tourDuJoueurBlanc);
                    casesAtteignablesProchainTour();
                }
            }
            if(impossibleDeJouer)
            {
                estGagnee = true;
                partieFinie = true;
                joueurBlancGagnant = joueurBlancGagnantSiJeuBloque;
            }
        }
    }

    /**
     * Permet de déplacer un pion sur la case voulue
     * @param caseDest (case où le pion doit arriver)
     */
    synchronized void deplacerPion(Model_Case caseDest)
    {
        history += pionMemoire.getCaseActuelle().getRow() + "" +
                + pionMemoire.getCaseActuelle().getColumn() + "" +
                + caseDest.getRow() + "" +
                + caseDest.getColumn()+':';
        plateau.deplacer(pionMemoire.getCaseActuelle(), caseDest, pionMemoire);
        System.out.println(history);
        dernierPionJoue = pionMemoire;
        setTourDuJoueurBlanc(!tourDuJoueurBlanc);
        casesAtteignablesProchainTour();
    }

    /**
     *
     * @return ()
     */
    synchronized boolean save()
    {
        BDDManager bdd = new BDDManager();
        bdd.start();
        int jb = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur = '" + joueurBlanc.getNom() + "';")
                .get(0).get(0));
        int jn = Integer.parseInt(bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur = '" + joueurNoir.getNom() + "';")
                .get(0).get(0));
        System.out.println(jb);
        System.out.println(jn);
        ArrayList<ArrayList<String>> joueurs = bdd.ask("" +
                "SELECT joueurBlancSave, joueurNoirSave " +
                "FROM SAUVEGARDEPARTIE " +
                "WHERE joueurBlancSave = '" + jb +"' AND joueurNoirSave = '" + jn + "';");
        if (!joueurs.isEmpty())
            return false;

        boolean tour = tourDuJoueurBlanc; // true si joueur blanc
        String etatPlateau = "";
        for (int i = 0; i < plateau.getBoard().length; i++)
        {
            if(plateau.getBoard()[i].getPion() == null) {
                etatPlateau += " ,";
            }
            else
            {
                if(plateau.getBoard()[i].getPion().isEstBlanc())
                    etatPlateau += plateau.getBoard()[i].getPion().getCOULEUR() + "b,";
                else
                    etatPlateau += plateau.getBoard()[i].getPion().getCOULEUR() + "n,";

            }

        }
        int id = BDD_Tools.saveHistory(jb, jn, history);

        bdd.edit("INSERT INTO SAUVEGARDEPARTIE VALUES(null, '"+
                jb + "', '" +
                jn + "', " +
                tour + ", '" +
                etatPlateau + "', " +
                id + ");");
        bdd.stop();
        return true;
    }

    /**
     * jeSuisBlanc
     * Décide aleatoirement si le joueur qui créer la partie est blanc
     *
     * @return (retourne une valeur au hazard blanc ou noir)
     */
    synchronized boolean jeSuisBlanc()
    {
        Random rand = new Random();
        return rand.nextBoolean();
    }

    // ajout SD : après un coup joué, qq soit le mode -> modifier le controller
    // pour appeler cette méthode

    /**
     * CoupFait
     * permet d'enregistrer les mouvements qui ont été fait.
     *
     * @param caseSrc (case de départ du mouvement)
     * @param caseDest (case d'arrivé du mouvement)
     */
    synchronized void coupFait(Model_Case caseSrc, Model_Case caseDest)
    {
        this.caseSrc = caseSrc;
        this.caseDest = caseDest;

    }

    /**
     * finTour
     * reveil le thread à la fin d'un tour
     */
    synchronized void finTour()
    {
        endOfTurn = true;
        notifyAll();
    }


    /**
     * waitFinTour
     * Fin
     */
    synchronized void waitFinTour()
    {
        while (!endOfTurn)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        endOfTurn = false;
    }


    // GETTERS & SETTERS
    synchronized boolean isJoueurBlancGagnant() { return joueurBlancGagnant; }
    synchronized Model_Plateau getPlateau() { return plateau; }
    synchronized boolean isTourUn() { return isTourUn; }
    synchronized boolean estGagnee() { return estGagnee; }
    synchronized boolean isTourDuJoueurBlanc() { return tourDuJoueurBlanc; }
    synchronized Model_Pion getPionMemoire() { return pionMemoire; }
    synchronized void setPionMemoire(Model_Pion pionMemoire) { this.pionMemoire = pionMemoire; }
    synchronized void setTourUn(boolean tourUn) { isTourUn = tourUn; }
    synchronized Model_Joueur getJoueurBlanc() { return joueurBlanc; }
    synchronized Model_Joueur getJoueurNoir() { return joueurNoir; }
    synchronized Model_Pion getDernierPionJoue() { return dernierPionJoue; }
    synchronized String getHistory() { return history; }
    synchronized void setDernierPionJoue(Model_Pion dernierPionJoue) {
        this.dernierPionJoue = dernierPionJoue;
    }
    synchronized void setHistory(String history) {
        this.history = history;
    }
    synchronized void setTourDuJoueurBlanc(boolean tourDuJoueurBlanc) {
        this.tourDuJoueurBlanc = tourDuJoueurBlanc;
        if (tourDuJoueurBlanc) idCurrentPlayer = 1;
        else idCurrentPlayer = 2;
    }
    synchronized void setEstPartieChargee(boolean estPartieChargee) { this.estPartieChargee = estPartieChargee; }
    synchronized boolean isEstPartieChargee() { return estPartieChargee; }
    synchronized int getIdCurrentPlayer() { return idCurrentPlayer;  }
    synchronized Model_Case getCaseSrc() { return caseSrc; }
    synchronized Model_Case getCaseDest() { return caseDest; }
    synchronized boolean isPartieFinie() { return partieFinie; }
    synchronized void setCaseDest(Model_Case caseDest) { this.caseDest = caseDest; }
    synchronized Model_Accueil getAccueil() { return accueil; }
    synchronized void setAccueil(Model_Accueil accueil) { this.accueil = accueil; }
    synchronized void setIdCurrentPlayer(int idCurrentPlayer) { this.idCurrentPlayer = idCurrentPlayer; }

    public void setPartieFinie(boolean partieFinie) {
        this.partieFinie = partieFinie;
    }

    public void setJoueurBLancGagnant(boolean joueurBLancGagnant) {
        this.joueurBLancGagnant = joueurBLancGagnant;
    }

    public Model_Pion getDernierPionJoueAnim() {
        return dernierPionJoueAnim;
    }
}