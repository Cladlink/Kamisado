import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class   Vue_Plateau extends JPanel
{
    private Model_Accueil accueil;
    private Vue vue;
    private BufferedImage[] imagesPionsJoueurBlanc;
    private BufferedImage[] imagesPionsJoueurNoir;
    private BufferedImage background;
    private int pas;
    private boolean deplacementEnCours=false;
    private Model_Case depart;

    private JProgressBar jProgressBar;

    private Timer timerAnim;


    private final int SIZECASE = 80;
    private final int EPAISSEUR_CASE_JOUEE_PAR_IA = 5;

    /**
     * COnstructeur d'une vue de plateau
     * @param vue (vue générale)
     * @param accueil (model relatif à l'accueil du jeu)
     */
    Vue_Plateau(Vue vue, Model_Accueil accueil)
    {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(boxLayout);

        depart = null; // attente animation
        this.vue = vue;
        this.accueil = accueil;

        imagesPionsJoueurBlanc = new BufferedImage[Model_Plateau.LIGNE];
        imagesPionsJoueurNoir = new BufferedImage[Model_Plateau.LIGNE];
        String[] listeCouleurs = Couleur.getListeStringCouleurs();

        try
        {
            for(int i = 0 ; i<listeCouleurs.length; i++)
            {
                imagesPionsJoueurBlanc[i] = ImageIO.read(new File("Images/Jetons/SpiraleNoire/spirale" + listeCouleurs[i] + ".png"));
                imagesPionsJoueurNoir[i] = ImageIO.read(new File("Images/Jetons/VagueNoire/vague" + listeCouleurs[i] + ".png"));
            }
            background = ImageIO.read(new File("Images/Fonds/fond2.jpg"));
        }
        catch(IOException e2)
        {
            //Cas d'erreur à gérer !!
        }
    }

    /**
     * Met en place les dimensions du plateau
     * @return (dimensions du plateau)
     */
    @Override
    public Dimension getPreferredSize() { return new Dimension(8*SIZECASE,8*SIZECASE); }

    /**
     * Paint l'objet graphique. Regroupe tous les objets graphiques
     * @param g (boite à outil servant à peindre des éléments)
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, vue.getWidth(), vue.getHeight(), null);
        int i, x, y, tailleDuPlateau = 64;
        int couleurCase, couleurPion;
        if(accueil.getia())
        {
            for(i=0; i<tailleDuPlateau; i++)
            {
                couleurCase = accueil.getPartieIa().getPlateauCase()[i];
                switch (couleurCase) {
                    case Model_Partie_IA.MARRON:
                        g.setColor(new Color(87, 37, 0, 175));
                        break;
                    case Model_Partie_IA.PINK:
                        g.setColor(new Color(239, 128, 179, 175));
                        break;
                    case Model_Partie_IA.ORANGE:
                        g.setColor(new Color(245, 132, 40, 175));
                        break;
                    case Model_Partie_IA.RED:
                        g.setColor(new Color(238, 58, 67, 175));
                        break;
                    case Model_Partie_IA.GREEN:
                        g.setColor(new Color(0, 162, 95, 175));
                        break;
                    case Model_Partie_IA.YELLOW:
                        g.setColor(new Color(255, 222, 0, 175));
                        break;
                    case Model_Partie_IA.BLUE:
                        g.setColor(new Color(0, 121, 194, 175));
                        break;
                    case Model_Partie_IA.VIOLET:
                        g.setColor(new Color(124, 66, 153, 175));
                        break;
                }
                x = i % 8;
                y = i / 8;
                g.fillRect(x * SIZECASE + 360, -y * SIZECASE + 580, SIZECASE, SIZECASE);
            }
            //dessine les pieces
            Boolean pionSurLaCase, estBlanc;
            for(i=0; i<tailleDuPlateau; i++)
            {
                pionSurLaCase = !(accueil.getPartieIa().getPlateau()[i]==-1);
                if(pionSurLaCase)
                {
                    estBlanc = accueil.getPartieIa().getPlateau()[i]<8;
                    couleurPion = accueil.getPartieIa().getPlateau()[i]%8;
                    BufferedImage[] typePion = estBlanc?imagesPionsJoueurBlanc:imagesPionsJoueurNoir;
                    x = i%8;
                    y = i/8;
                    g.drawImage(typePion[couleurPion], x * SIZECASE +380 - SIZECASE/4, -y * SIZECASE +620 - SIZECASE/2, null);
                }
            }
            byte dernierCaseJouer = accueil.getPartieIa().getCaseDernierPionJoue();
            if( dernierCaseJouer!=-1) {
                Stroke oldStroke = ((Graphics2D)g).getStroke();
                ((Graphics2D)g).setStroke(new BasicStroke(EPAISSEUR_CASE_JOUEE_PAR_IA));


                g.setColor(new Color(125, 0, 0));
                x = accueil.getPartieIa().getCaseDernierPionJoue() % 8;
                y = accueil.getPartieIa().getCaseDernierPionJoue() / 8;
                int xP = accueil.getPartieIa().getCaseDestDernierPionJoue() % 8;
                int yP = accueil.getPartieIa().getCaseDestDernierPionJoue() / 8;

                float alpha = 0.20f;
                int type = AlphaComposite.SRC_OVER;
                AlphaComposite composite =
                        AlphaComposite.getInstance(type, alpha);
                ((Graphics2D) g).setComposite(composite);
                BufferedImage[] typePion;
                if(accueil.getPartieIa().getDernierPionJoue() < 8)
                    typePion = imagesPionsJoueurBlanc;
                else
                    typePion = imagesPionsJoueurNoir;

                g.drawImage(typePion[accueil.getPartieIa().getDernierPionJoue()%8],
                        x  * SIZECASE + 380 - SIZECASE/4,
                        -y * SIZECASE + 620 - SIZECASE/2,
                        null);
                composite = AlphaComposite.getInstance(type, 1f);
                ((Graphics2D) g).setComposite(composite);
            }

            if(!accueil.getPartieIa().isEstGagnee()) {
                g.setColor(new Color(255, 255, 255, 120));
                for (byte c : accueil.getPartieIa().getCasesAtteignablesJoueurCourant())
                    if (c != -1) {
                        x = c % 8;
                        y = c / 8;
                        g.fillOval(x * SIZECASE + 380 - SIZECASE / 4, (8 - y) * SIZECASE - 20 - SIZECASE / 2, SIZECASE, SIZECASE);
                    }
            }

            return;
        }

        Model_Pion pionSurLaCase;

        for(i=0; i<tailleDuPlateau; i++)
        {
            couleurCase = accueil.getPartie().getPlateau().getBoard()[i].getCOULEUR();
            switch (couleurCase)
            {
                case Couleur.MARRON:
                    g.setColor(new Color(87,37,0,175)); break;
                case Couleur.ROSE:
                    g.setColor(new Color(239,128,179,175)); break;
                case Couleur.ORANGE:
                    g.setColor(new Color(245,132,40,175)); break;
                case Couleur.ROUGE:
                    g.setColor(new Color(238,58,67,175)); break;
                case Couleur.VERT:
                    g.setColor(new Color(0,162,95,175)); break;
                case Couleur.JAUNE:
                    g.setColor(new Color(255,222,0,175)); break;
                case Couleur.BLEU:
                    g.setColor(new Color(0,121,194,175)); break;
                case Couleur.VIOLET:
                    g.setColor(new Color(124,66,153,175)); break;
            }
            x = i%8;
            y = i/8;
            g.fillRect(x * SIZECASE + 360, -y * SIZECASE + 580, SIZECASE, SIZECASE);
        }

        //dessine les pieces
        for(i=0; i<tailleDuPlateau; i++)
        {
            pionSurLaCase = accueil.getPartie().getPlateau().getBoard()[i].getPion();
            if(pionSurLaCase != null)
            {
                couleurPion = pionSurLaCase.getCOULEUR();
                BufferedImage[] typePion = pionSurLaCase.isEstBlanc()?imagesPionsJoueurBlanc:imagesPionsJoueurNoir;

                x = i%8;
                y = i/8;

                // MICHAEL
                /*
                On prends le pion a dessiner : soit il s'agit du pion animation soit d'un autre pion
                si c'est le pion animation on part de sa source et on va jusqu'à la case actuelle
                 */
                Model_Pion pionAdessiner = accueil.getPartie().getPlateau().getBoard()[i].getPion();
                Model_Pion dernierPionJoue = accueil.getPartie().getDernierPionJoueAnim();
                if (dernierPionJoue != null
                    && dernierPionJoue.equals(pionAdessiner)
                    && deplacementEnCours)
                {
                    System.out.println(dernierPionJoue.getCaseActuelle().getColumn() + " " + dernierPionJoue.getCaseActuelle().getRow());
                    g.drawImage(typePion[couleurPion],
                            depart.getColumn() * SIZECASE + pionSurLaCase.getRelX() + 380 - SIZECASE/4,
                            -depart.getRow() * SIZECASE - pionSurLaCase.getRelY() + 620 - SIZECASE/2,
                            null);
                }
                else
                    g.drawImage(typePion[couleurPion],
                            x  * SIZECASE + 380 - SIZECASE/4,
                            -y * SIZECASE + 620 - SIZECASE/2,
                            null);
            }
        }
        if(accueil.getPartie().getPionMemoire() != null)
        {
            ArrayList<Model_Case> cases= accueil.getPartie().getPionMemoire().getCasesAtteignables();
                g.setColor(new Color(255,255,255,120));
                for(Model_Case c : cases)
                    g.fillOval(
                            c.getColumn() * SIZECASE +380 - SIZECASE/4,
                            (8-c.getRow()) * SIZECASE -20 - SIZECASE/2,
                            SIZECASE, SIZECASE);
        }
    }
    // MICHAEL JUSQU'EN BAS
    /**
     * deplacementAnimation
     * classe gérant le parcours de la pièce de la case de départ à la case d'arrivée
     *
     * @param depart (case de départ)
     * @param arrive (case d'arrivée)
     * @param piece (pièce qui a bougé)
     */
    void deplacementAnimation(Model_Case depart, Model_Case arrive, Model_Pion piece)
    {
        this.depart = depart;
        //reinitialise le rel de piece
        piece.setRelX(0);
        piece.setRelY(0);

        piece.setIncX(((arrive.getColumn()-depart.getColumn())*80)/(10));
        piece.setIncY(((arrive.getRow()-depart.getRow())*80)/(10));

        pas = 0;

        timerAnim = new Timer(30, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //calcule nouvelle position
                piece.setRelX(piece.getRelX()+piece.getIncX());
                piece.setRelY(piece.getRelY()+piece.getIncY());
                repaint();

                pas++;
                if (pas >=10)
                {
                    piece.setRelX(0);
                    piece.setRelY(0);

                    stopTimerAnim();
                }
            }
        });
        timerAnim.start();
        deplacementEnCours = true;
    }

    /**
     * stopTimerAnim
     * Arrête l'animation
     */
    private void stopTimerAnim()
    {
        timerAnim.stop();
        deplacementEnCours=false;
    }

    public void createProgressBar(){
        jProgressBar= new JProgressBar(0, 100);
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(true);
        this.add(jProgressBar);
        this.validate();
    }
    public void updateProgressBar(int pourcentage){
        jProgressBar.setValue(pourcentage);
        this.validate();
    }

    public void deleteProgressBar(){
        this.remove(jProgressBar);
        this.validate();
    }
}
