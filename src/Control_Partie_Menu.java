import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 Created by cladlink on 06/11/16.
 */
class Control_Partie_Menu implements ActionListener
{
    private Vue vue;
    private Model_Accueil accueil;
    Control_Partie_Menu(Vue vue, Model_Accueil accueil)
    {
        this.vue = vue;
        this.accueil = accueil;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(vue.getQuitterMenu()))
        {
            if(!accueil.getia())
            {
                if (!(accueil.getPartie().getHistory().length() == 0))
                {
                       boolean sauvegarde = vue.boolJOptionPane("Voulez-vous sauvegarder avant de quitter ?");
                    if (sauvegarde)
                    {
                        if (!accueil.getPartie().save())
                            vue.messagePop("Vous ne pouvez pas enregistrer car vous avez déjà une partie interrompue.");
                        System.exit(0);
                    }
                    System.exit(0);
                }
                else
                    System.exit(0);
            }
            else{
                System.exit(0);
            }
        }

        else if (e.getSource().equals(vue.getRetourMenuPrincipalMenu()))
        {
            if(!accueil.getia()){
                if (!(accueil.getPartie().getHistory().length() == 0))
                {
                    boolean sauvegarde = vue.boolJOptionPane("Voulez-vous sauvegarder avant de quitter ?");
                    if (sauvegarde)
                    {
                        if(!accueil.getPartie().save())
                            vue.jOptionMessage("Vous ne pouvez pas enregistrer car vous avez déjà une partie interrompue.");
                    }
                }
            }
            vue.setJMenuBar(null);
            vue.afficherMenu();
        }

        else if (e.getSource().equals(vue.getUndoMenu()))
        {
            boolean undo = vue.boolJOptionPane("Voulez-vous annuler le dernier coup ?");
            if (undo)
            {
                if(!accueil.getPartie().undo())
                    Vue_FactorPopup.creerPopupText();
                vue.repaint();
            }
        }
    }
}