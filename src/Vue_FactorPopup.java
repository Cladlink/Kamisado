import javax.swing.*;
import java.util.ResourceBundle;

/**
 Created by fparty2 on 18/10/16.
 */
class Vue_FactorPopup
{
    //Il faudra gerer les traductions !!


    static void creerPopupCredits()
    {
        ResourceBundle texteInternational = ResourceBundle.getBundle("Traductions.popup");
        JPanel panel = new JPanel();
        JLabel labelTexte = new JLabel("<html>" + texteInternational.getString("jeuRealisePar")+ " :<br>" +
                "<br> Marie-Lucile Caniard " +
                "<br> Michael Boutboul " +
                "<br> Adonis N'Dolo " +
                "<br> Florian Party</html>");

        panel.add(labelTexte);
        new Vue_Fenetre_PopUp(panel,
                texteInternational.getString("credit"));
    }

    static void creerPopupText()
    {
        ResourceBundle texteInternational = ResourceBundle.getBundle("Traductions.noUndo");
        JPanel panel = new JPanel();
        JLabel labelTexte = new JLabel("<html>"
                + texteInternational.getString("Il n'y a plus de coup à annuler.")
                + "</html>");

        panel.add(labelTexte);
        new Vue_Fenetre_PopUp(panel,
                texteInternational.getString("undo"));
    }

    static void creerPopupErreurChargement()
    {
        ResourceBundle texteInternational = ResourceBundle.getBundle("Traductions.popup");
        JPanel panel = new JPanel();

        JLabel labelTexte = new JLabel("<html>"+texteInternational.getString("erreurChargement")+"</html>");
        panel.add(labelTexte);

        new Vue_Fenetre_PopUp(panel,
                texteInternational.getString("erreur"));
    }

    static void creerPopupJoueurGagnant(String nomJoueur)
    {
        ResourceBundle texteInternational = ResourceBundle.getBundle("Traductions.victoire");
        JPanel panel = new JPanel();

        JLabel labelTexte = new JLabel(nomJoueur + " " + texteInternational.getString("message"));
        panel.add(labelTexte);
        new Vue_Fenetre_PopUp(panel, texteInternational.getString("titreFenetre"));
    }
}