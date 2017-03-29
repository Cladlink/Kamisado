import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

class Vue_Bouton extends JButton
{
    /**
     * Construsteur d'un bouton
     * @param texte (chaine de caractère aparaissant sur le bouton)
     */
    Vue_Bouton(String texte)
    {
        super(texte, null);
        setBorderPainted(false);
        setSize(50, 20);
        setMaximumSize(new Dimension(150, 20));
        setSelected(false);
        setBackground(Color.black);
        setForeground(Color.white);
        setFocusable(false);
        Font police = new Font("Cardinal", Font.BOLD, 27);
        setFont(police);
    }

    /**
     * Paint l'objet graphique. Regroupe tous les objets graphiques
     * @param g (boite à outil servant à peindre des éléments)
     */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        if (getModel().isPressed())
        {
            g.setColor(g.getColor());
            g2.fillRect(3, 3, getWidth() - 6, getHeight() - 6);
        }
        super.paintComponent(g);
        g2.setColor(new Color(128, 0, 128));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Double(1, 1, (getWidth() - 3), (getHeight() - 3), 12, 8));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(4, getHeight() - 3, getWidth() - 4, getHeight() - 3);
        g2.dispose();
    }
}
