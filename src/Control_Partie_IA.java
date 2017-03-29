import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

class Control_Partie_IA extends MouseAdapter
{

    private final Model_Accueil accueil;
    private final Vue vue;
    private ResourceBundle texteInternational;
    private boolean actif;

    Control_Partie_IA(Model_Accueil accueil, Vue vue)
    {
        this.accueil = accueil;
        this.vue = vue;
        texteInternational = ResourceBundle.getBundle("Traductions.victoire");
        vue.setPartieControl(this);

        actif = true;
    }

    /**
     * Définition des actions à entreprendre si un écouteur détecte une action
     * @param e (evenement détecté)
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(actif) {
            actif = false;

            Control_Partie_IA self = this;
            Thread r = new Thread() {
                @Override
                public void run() {
                    byte i;
                    int column = (int) Math.floor((e.getX() - 360) / 80.0);
                    int row = -(int) Math.floor(((e.getY() - 20) / 80.0) - 7);
                    byte[] plateau = accueil.getPartieIa().getPlateau();
                    byte index = (byte) (8 * row + column);


                    if (e.getSource().equals(vue.getVue_plateau())
                            && column >= 0
                            && column <= 7
                            && row >= 0
                            && row <= 7) {
                        if (accueil.getPartieIa().isTourUn()) {
                            // Joueur blanc = humain et c'est lui qui commence
                            // On regarde si il y a un pion sur la case cliquée et si le pion appartient au joueur
                            if (plateau[index] != -1 && index < 8) {
                                accueil.getPartieIa().setPionMemoire(plateau[index]);
                                accueil.getPartieIa().setCasePionMemoire(index);
                                accueil.getPartieIa().setCasesAtteignablesJoueurCourant(accueil.getPartieIa().getCasesAtteignablesTourUn()[accueil.getPartieIa().getPionMemoire()]);
                                vue.getVue_plateau().repaint();

                            }
                            // Si il n'y a pas de pion sur la case cliquée et qu'il y a un pionMemoire
                            else if (plateau[index] == -1 && accueil.getPartieIa().getPionMemoire() != -1) {
                                // On vérifie que la case cliquée est dans les cases atteignables du pion en mémoire
                                boolean isCaseAtteignable = false;
                                for (i = 0; i < Model_Partie_IA.NBCASESATTEIGNABLESPOSSIBLESPREMIERTOUR; i++) {
                                    if (accueil.getPartieIa().getCasesAtteignablesTourUn()[accueil.getPartieIa().getPionMemoire()][i]
                                            == index) {
                                        isCaseAtteignable = true;
                                        break;
                                    }
                                }
                                if (isCaseAtteignable) {
                                    // On récupère la couleur de la case ou va se déplacer le pion
                                    accueil.getPartieIa().setCouleurPionAJouer(accueil.getPartieIa().getPlateauCase()[index]);
                                    // Déplacement de la pièce
                                    accueil.getPartieIa().deplacerPiece(index);

                                    // On passe à l'autre joueur et on précise que le premier tour, qui est spécial à traiter, est
                                    // terminé
                                    accueil.getPartieIa().setTourUn(false);
                                    accueil.getPartieIa().setTourDuJoueurBlanc(false);

                                    //vue.getVue_plateau().repaint();

                                    // On prépare le tour d'après
                                    // On retrouve le pion qui doit jouer et on le met dans le pion mémoire
                                    for (i = 0; i < plateau.length; i++)
                                        if (plateau[i] != -1 && plateau[i] % 8 == accueil.getPartieIa().getCouleurPionAJouer() % 8
                                                && plateau[i] > 7) {
                                            accueil.getPartieIa().setPionMemoire(plateau[i]);
                                            accueil.getPartieIa().setCasePionMemoire(i);
                                            break;
                                        }

                                    // On calcul les cases atteignables du pion mémoire
                                    accueil.getPartieIa().setCasesAtteignablesJoueurCourant(false, accueil.getPartieIa().getCasePionMemoire());
                                    // On fait jouer l'IA
//                        accueil.getPartieIa().evaluate();
                                    accueil.getPartieIa().simulerCoup();
                                    int maxVal = Model_Partie_IA.MOINS_INFINI;
                                    byte meilleur_coup = 0;

                                    byte nbCases = 0;
                                    for (byte k = 0; k < accueil.getPartieIa().casesAtteignablesJoueurCourant.length && accueil.getPartieIa().casesAtteignablesJoueurCourant[k] != -1; k++) {
                                        nbCases++;
                                    }
                                    vue.getVue_plateau().createProgressBar();
                                    for (byte k = 0; k < nbCases; k++) {
                                        int val = accueil.getPartieIa().testerCoup(k);
                                        if (val > maxVal) {
                                            maxVal = val;
                                            meilleur_coup = k;
                                        }
                                        int pourcentage = (int) (k / (double) nbCases * 100.0);
                                        vue.getVue_plateau().updateProgressBar(pourcentage);
                                    }
                                    vue.getVue_plateau().deleteProgressBar();

                                    accueil.getPartieIa().retabliCoup(meilleur_coup);

                                    // On prépare le tour suivant
                                    // On retrouve le pion qui doit jouer et on le met dans le pion mémoire
                                    for (i = 0; i < plateau.length; i++)
                                        if (accueil.getPartieIa().getPlateau()[i] != -1
                                                && accueil.getPartieIa().getPlateau()[i] == accueil.getPartieIa().getCouleurPionAJouer()) {
                                            accueil.getPartieIa().setPionMemoire(accueil.getPartieIa().getPlateau()[i]);
                                            accueil.getPartieIa().setCasePionMemoire(i);
                                        }
                                    //On calcul les cases atteignables du pion pour le tour suivant
                                    accueil.getPartieIa().setCasesAtteignablesJoueurCourant(true, accueil.getPartieIa().getCasePionMemoire());
                                }
                            }
                        }
                        // Si ce n'est pas le premier tour
                        else {
                            if (plateau[index] == -1) {
                                // On vérifie si le pion est bloqué
                                boolean isBloque = accueil.getPartieIa().controlBlocage();
                                if (!isBloque) {
                                    // On vérifie que la case cliquée est dans les cases atteignables du pion en mémoire
                                    boolean isCaseAtteignable = false;
                                    for (i = 0; i < accueil.getPartieIa().getCasesAtteignablesJoueurCourant().length; i++)
                                        if (accueil.getPartieIa().getCasesAtteignablesJoueurCourant()[i] == index) {
                                            isCaseAtteignable = true;
                                            break;
                                        }

                                    if (isCaseAtteignable) {
                                        // On reprend la couleur de la case ou va être déplacer le pion pour le tour d'après
                                        accueil.getPartieIa().setCouleurPionAJouer(accueil.getPartieIa().getPlateauCase()[index]);
                                        // Déplacement de la pièce
                                        accueil.getPartieIa().deplacerPiece(index);
                                        accueil.getPartieIa().setTourDuJoueurBlanc(false);

                                        //vue.repaint();
                                        // On vérifie si il y a victoire ou pas
                                        if (index > 55 && index <= 63) {
                                            accueil.getPartieIa().setEstGagnee(true);
                                            vue.removePartieControl(self);
                                            Vue_FactorPopup.creerPopupJoueurGagnant(texteInternational.getString("joueurBlancGagnant"));
                                            return;
                                        }

                                        // On retrouve le pion qui doit jouer et on le met dans le pion mémoire
                                        for (i = 0; i < plateau.length; i++)
                                            if (plateau[i] != -1 && plateau[i] % 8 == accueil.getPartieIa().getCouleurPionAJouer() % 8
                                                    && plateau[i] > 7) {
                                                accueil.getPartieIa().setPionMemoire(plateau[i]);
                                                accueil.getPartieIa().setCasePionMemoire(i);
                                            }

                                        // On calcul les cases atteignables du pion mémoire
                                        accueil.getPartieIa().setCasesAtteignablesJoueurCourant(false, accueil.getPartieIa().getCasePionMemoire());
                                    }
                                }
                                // Si le pion du joueur blanc (humain) est bloqué
                                else {
                                    // On trouve la couleur de la case sur laquelle se trouve le pion bloqué
                                    accueil.getPartieIa().setCouleurPionAJouer(accueil.getPartieIa().getPlateauCase()[accueil.getPartieIa().getCasePionMemoire()]);
                                    // On retrouve le pion qui doit jouer et on le met dans le pion mémoire
                                    for (i = 0; i < plateau.length; i++)
                                        if (plateau[i] != -1 && plateau[i] % 8 == accueil.getPartieIa().getCouleurPionAJouer() % 8
                                                && plateau[i] > 7) {
                                            accueil.getPartieIa().setPionMemoire(plateau[i]);
                                            accueil.getPartieIa().setCasePionMemoire(i);
                                        }
                                    // On calcul les cases atteignables du pion mémoire
                                    accueil.getPartieIa().setCasesAtteignablesJoueurCourant(false, accueil.getPartieIa().getCasePionMemoire());
                                    accueil.getPartieIa().setTourDuJoueurBlanc(false);
                                }
                            }

                            // Tour du joueur noir (IA)
                            if (!accueil.getPartieIa().isTourDuJoueurBlanc()) {
                                // On vérifie si il y a blocage ou pas
                                boolean isBloque = accueil.getPartieIa().controlBlocage();
                                if (!isBloque) {
                                    // On fait le déplacement aléatoire de l'IA
                                    //byte caseAlea = accueil.getPartieIa().evaluate();
                                    accueil.getPartieIa().simulerCoup();
                                    int maxVal = Model_Partie_IA.MOINS_INFINI;
                                    byte meilleur_coup = 0;

                                    byte nbCases = 0;
                                    for (byte k = 0; k < accueil.getPartieIa().casesAtteignablesJoueurCourant.length && accueil.getPartieIa().casesAtteignablesJoueurCourant[k] != -1; k++) {
                                        nbCases++;
                                    }
                                    vue.getVue_plateau().createProgressBar();
                                    for (byte k = 0; k < nbCases; k++) {
                                        int val = accueil.getPartieIa().testerCoup(k);
                                        if (val > maxVal) {
                                            maxVal = val;
                                            meilleur_coup = k;
                                        }
                                        int pourcentage = (int) (k / (double) nbCases * 100.0);
                                        vue.getVue_plateau().updateProgressBar(pourcentage);
                                    }
                                    vue.getVue_plateau().deleteProgressBar();

                                    accueil.getPartieIa().retabliCoup(meilleur_coup);
                                    vue.repaint();

                                    // On vérifie si il y a victoire ou pas
                                    if (accueil.getPartieIa().getCasesAtteignablesJoueurCourant()[meilleur_coup] < 8
                                            && accueil.getPartieIa().getCasesAtteignablesJoueurCourant()[meilleur_coup] >= 0) {

                                        accueil.getPartieIa().setEstGagnee(true);
                                        vue.removePartieControl(self);
                                        Vue_FactorPopup.creerPopupJoueurGagnant(texteInternational.getString("IAgagnant"));
                                    }

                                    // On prépare le tour suivant
                                    // On retrouve le pion qui doit jouer et on le met dans le pion mémoire
                                    for (i = 0; i < plateau.length; i++)
                                        if (accueil.getPartieIa().getPlateau()[i] != -1
                                                && accueil.getPartieIa().getPlateau()[i] == accueil.getPartieIa().getCouleurPionAJouer()) {
                                            accueil.getPartieIa().setPionMemoire(accueil.getPartieIa().getPlateau()[i]);
                                            accueil.getPartieIa().setCasePionMemoire(i);
                                        }
                                    //On calcul les cases atteignables du pion pour le tour suivant
                                    accueil.getPartieIa().setCasesAtteignablesJoueurCourant(true, accueil.getPartieIa().getCasePionMemoire());
                                } else {
                                    accueil.getPartieIa().setCouleurPionAJouer(accueil.getPartieIa().getPlateauCase()[accueil.getPartieIa().getCasePionMemoire()]);
                                    // On retrouve le pion qui doit jouer et on le met dans le pion mémoire
                                    for (i = 0; i < plateau.length; i++)
                                        if (accueil.getPartieIa().getPlateau()[i] != -1
                                                && accueil.getPartieIa().getPlateau()[i] == accueil.getPartieIa().getCouleurPionAJouer()) {
                                            accueil.getPartieIa().setPionMemoire(accueil.getPartieIa().getPlateau()[i]);
                                            accueil.getPartieIa().setCasePionMemoire(i);
                                        }
                                    // On calcul les cases atteignables du pion mémoire
                                    accueil.getPartieIa().setCasesAtteignablesJoueurCourant(true, accueil.getPartieIa().getCasePionMemoire());
                                    accueil.getPartieIa().setTourDuJoueurBlanc(true);
                                }
                            }
                        }
                    }
                    vue.getVue_plateau().updateUI();
                    actif = true;
                }

            };
            r.start();
        }




    }
}