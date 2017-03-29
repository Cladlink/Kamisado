import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Control_Historique implements ActionListener
{
    private Model_Accueil accueil;
    private Vue vue;
    private Control_Partie partie;
    private ArrayList<String> histoCoups;
    private int indice;

    Control_Historique(Model_Accueil accueil, Vue vue, Control_Partie partie)
    {
        this.accueil = accueil;
        this.vue = vue;
        this.partie = partie;
        histoCoups = new ArrayList<>();
        indice = 0;
        vue.setButtonHistoriqueControl(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        boolean isIndiceChanged = false;
        boolean isElementSuivant = false;
        if(e.getSource().equals(vue.getRetour()))
        {
            indice = 0;
            histoCoups.clear();
            vue.afficherMenu();
            return;
        }
        if (histoCoups.size() == 0)
        {
            histoCoups = vue.recupererHistoCoupsPartie();
            histoCoups.add(0, null);
        }

        if (e.getSource().equals(vue.getSuivant()))
        {
            if (indice + 1 < histoCoups.size())
            {
                indice++;
                isIndiceChanged = true;
                isElementSuivant = true;
            }
        }
        else if (e.getSource().equals(vue.getPrecedent()))
        {
            if (indice > 0)
            {
                indice--;
                isIndiceChanged=true;
            }
        }

        if (isIndiceChanged)
        {
            if (isElementSuivant)
            {
                if(histoCoups.get(indice).charAt(0) != '!')
                {
                    int rDepart = Integer.parseInt(String.valueOf(histoCoups.get(indice).charAt(0)));
                    int cDepart = Integer.parseInt(String.valueOf(histoCoups.get(indice).charAt(1)));
                    int rFinal = Integer.parseInt(String.valueOf(histoCoups.get(indice).charAt(2)));
                    int cFinal = Integer.parseInt(String.valueOf(histoCoups.get(indice).charAt(3)));

                    accueil.getPartie().getPlateau().deplacer(accueil.getPartie().getPlateau().getBoard()[8 * rDepart + cDepart],
                            accueil.getPartie().getPlateau().getBoard()[8 * rFinal + cFinal],
                            accueil.getPartie().getPlateau().getBoard()[8 * rDepart + cDepart].getPion());
                }
            }
            else
            {
                if(histoCoups.get(indice+1).charAt(0) != '!')
                {
                    indice++;
                    int rDepart = Integer.parseInt(String.valueOf(histoCoups.get(indice).charAt(2)));
                    int cDepart = Integer.parseInt(String.valueOf(histoCoups.get(indice).charAt(3)));
                    int rFinal = Integer.parseInt(String.valueOf(histoCoups.get(indice).charAt(0)));
                    int cFinal = Integer.parseInt(String.valueOf(histoCoups.get(indice).charAt(1)));

                    accueil.getPartie().getPlateau().deplacer(accueil.getPartie().getPlateau().getBoard()[8 * rDepart + cDepart],
                            accueil.getPartie().getPlateau().getBoard()[8 * rFinal + cFinal],
                            accueil.getPartie().getPlateau().getBoard()[8 * rDepart + cDepart].getPion());
                    indice--;
                }
            }
            vue.repaint();
        }
    }
}