import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 Created by fparty2 on 11/10/16.
 */
public class Vue_Fenetre_PopUp extends JFrame
{

    public Vue_Fenetre_PopUp(JPanel panel) //si on ne donne pes de titre à la fenetre
    {
        setContentPane(panel);
        setConfig();
        pack();
    }

    Vue_Fenetre_PopUp(JPanel panel, String titre)   //si on donne un titre à la fenetre
    {
        setContentPane(panel);

        setConfig(titre);
        pack();
    }

    private void setConfig()
    {
        setResizable(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        //setAlwaysOnTop(true);        //a mettre si besoin
        setVisible(true);
    }

    private void setConfig(String titre)
    {
        setTitle(titre);
        setResizable(false);
        setLocationRelativeTo(null);
        //setAlwaysOnTop(true);        //a mettre si besoin
        setVisible(true);
    }

    public static void main(String[] args)
    {
        final JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        JLabel lab = new JLabel("Un popUp");
        final JButton butOk = new JButton("Ok");
        panel.add(lab);
        panel.add(butOk);

        final Vue_Fenetre_PopUp popUp = new Vue_Fenetre_PopUp(panel, "gyugyuè");

        ActionListener listener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // A adapté en fonction des différents cas : quitter le programme ou simplement fermer la fenêtre
                // Histoire que le jeux se quitte pas si l'erreur est bénine.
                if (e.getSource().equals(butOk)){
                    //Arrête le programme
                    //System.exit(0);
                    //Ferme la fenêtre
                    popUp.dispose();
                }
            }
        };
        butOk.addActionListener(listener);
    }
}