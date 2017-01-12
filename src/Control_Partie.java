import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

class Control_Partie extends MouseAdapter
{
    private Vue vue;
    private Model_Accueil accueil;

    private ResourceBundle texteInternational;
    private String joueurBlanc; // todo pas géré
    private String joueurNoir; // todo pas géré

    /**
     * Constructeur du controleur d'une partie
     * @param accueil (model de l'accueil)
     * @param vue (vue générale)
     */
    Control_Partie(Model_Accueil accueil, Vue vue)
    {
        this.accueil = accueil;
        this.vue = vue;
        texteInternational = ResourceBundle.getBundle("Traductions.victoire");
        vue.setPartieControl(this);
    }


    /**
     * Définition des actions à entreprendre si un écouteur détecte une action
     * @param e (evenement détecté)
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        int column = (e.getX()-360)/80;
        int row = Math.abs(((e.getY()-20)/80)-7);

        if (e.getSource().equals(vue.getVue_plateau())
                && column >= 0
                && column <=7
                && row >=0
                && row <=7)
        {
            if(accueil.getPartie().getPionMemoire() != null
            && accueil.getPartie().getPionMemoire().getCasesAtteignables() != null
                && accueil.getPartie().getPionMemoire().getCasesAtteignables().contains(
                        accueil.getPartie().getPlateau().getBoard()[row*Model_Plateau.LIGNE + column]) )
            {
                vue.getVue_plateau().deplacementAnimation(
                        accueil.getPartie().getPionMemoire().getCaseActuelle(),
                        accueil.getPartie().getPlateau().getBoard()[row*Model_Plateau.LIGNE + column],
                        accueil.getPartie().getPionMemoire());
                try
                {
                    sleep(1);
                }
                catch(InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
            Model_Case src = null;
            Model_Case dest = null;
            if (accueil.getPartie().getPionMemoire() != null) // todo a affiner
            {
                src = accueil.getPartie().getPionMemoire().getCaseActuelle();
               dest = accueil.getPartie().getPlateau().getBoard()[row*Model_Plateau.LIGNE+column];
            }
            if( accueil.getPartie().isTourDuJoueurBlanc() )
                accueil.getPartie().getJoueurBlanc().gestionTourJoueur(row, column);
            else
                accueil.getPartie().getJoueurNoir().gestionTourJoueur(row, column);

            if(!accueil.getPartie().estGagnee() && !accueil.getPartie().isTourUn())
                accueil.getPartie().controleBlocage();

            vue.repaint();
            if (!accueil.getPartie().isTourUn())
            {
                accueil.getPartie().coupFait(src, dest);
                accueil.getPartie().finTour();
            }
            if(accueil.getPartie().estGagnee()) finPartie();
        }
    }

    /**
     * Actions à entreprendre si une situation gagnante est détectée
     */
    private void finPartie()
    {
        String nomJoueur;
        vue.setPartieControl(null);

        BDD_Tools.saveHistory(accueil.getPartie().getJoueurBlanc().getId(),
                accueil.getPartie().getJoueurNoir().getId(),
                accueil.getPartie().getHistory());

        if(accueil.getPartie().isJoueurBlancGagnant())
        {
            nomJoueur = accueil.getPartie().getJoueurBlanc().getNom();
            String nomJoueurPerdant = accueil.getPartie().getJoueurNoir().getNom();
            Model_Joueur.ajouteVictoire(nomJoueur, nomJoueurPerdant);
        }
        else
        {
            nomJoueur = accueil.getPartie().getJoueurNoir().getNom();
            String nomJoueurPerdant =  accueil.getPartie().getJoueurBlanc().getNom();
            Model_Joueur.ajouteVictoire(nomJoueur,nomJoueurPerdant);
        }
        if(accueil.getPartie().isEstPartieChargee())
        {
            BDDManager bdd = new BDDManager();
            bdd.start();
            bdd.edit("DELETE FROM SAUVEGARDEPARTIE WHERE joueurBlancSave = " + accueil.getPartie().getJoueurBlanc().getId()
                    + " AND joueurNoirSave = " + accueil.getPartie().getJoueurNoir().getId());
            bdd.stop();
        }
        vue.afficherMenu();
        Vue_FactorPopup.creerPopupJoueurGagnant(nomJoueur);
    }

    void debutTour()
    {
        enableView(true);
        accueil.getPartie().casesAtteignablesProchainTour();
        vue.repaint();
    }
    void enableView(boolean state) { vue.setEnabled(state); }
    void updatePartie(int srcX, int srcY, int destX, int destY)
    {
        accueil.getPartie().setTourUn(false);
        Model_Case caseSrc = accueil.getPartie().getPlateau().getBoard()[srcX*Model_Plateau.LIGNE+srcY];
        Model_Case caseDest = accueil.getPartie().getPlateau().getBoard()[destX*Model_Plateau.LIGNE+destY];
        accueil.getPartie().getPlateau().deplacer(caseSrc, caseDest, caseSrc.getPion());
    }
    void setJoueurBlanc(String joueurBlanc)  { this.joueurBlanc = joueurBlanc; }
    void setJoueurNoir(String joueurNoir) { this.joueurNoir = joueurNoir; }
}