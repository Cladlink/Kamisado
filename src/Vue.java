import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.*;


class Vue extends JFrame
{
    private Model_Accueil accueil;
    private Vue_Plateau vue_plateau;
    private int xSize, ySize;
    private Vue_Bouton lancerPartieRapide;
    private Vue_Bouton lancerPartieLocale;
    private Vue_Bouton lancerPartieContreIA;
    private Vue_Bouton lancerPartieEnReseau;
    private Vue_Bouton chargerPartie;
    private Vue_Bouton historique;
    private Vue_Bouton statistiquesDuJoueur;
    private JButton options;
    private JButton credits;
    private Vue_Bouton quitter;
    private Vue_Bouton retourMenu;
    private Vue_Bouton lancerPartieLocale2;
    private Vue_Bouton nouveauPseudo;
    private Vue_Bouton nouveauPseudoCreerReseau;
    private Vue_Bouton nouveauPseudoRejoindreReseau;
    private Vue_Bouton rejoindrePartieReseauBoutonMenu;
    private Vue_Bouton creerPartieReseauBoutonMenu;
    private JMenuItem undoMenu, retourMenuPrincipalMenu, quitterMenu;
    private Vue_Bouton precedent;
    private Vue_Bouton retour;
    private Vue_Bouton suivant;
    private JLabel titre;
    private JPanel background;
    private JLabel joueur1;
    private JLabel joueur2;
    private JComboBox listePseudo1;
    private JFrame vueHisto;
    private JPanel boutonsMenu;

    // Options
    private JLabel titreOptions;
    private JButton francaisFlag;
    private JButton anglaisFlag;
    private JLabel france;
    private JLabel angleterre;
    private JRadioButton musiqueOn;
    private JRadioButton musiqueOff;
    private ButtonGroup musique;
    private JLabel choixMusique;
    private JLabel choixLangue;

    // Menu
    JMenu parametres;
    JMenuBar barMenu;

    private JComboBox listePseudo2;
    private ResourceBundle texteInternationalBoutons;
    private ResourceBundle texteInternationalLabels;
    private Vue_Bouton rejoindrePartieReseau;

    /**
     * Constructeur de la vu
     * @param accueil (model utilisé pour l'accueil)
     */
    Vue(Model_Accueil accueil)
    {
        this.accueil = accueil;
        Toolkit tk = Toolkit.getDefaultToolkit();
        xSize = (int) tk.getScreenSize().getWidth();
        ySize = (int) tk.getScreenSize().getHeight();

        initAttribut();
        creerWidgetAccueil();

        setUndecorated(true);
        setSize(xSize, ySize);
        setTitle("Kamisado");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Initialisation de tous les attribus de la classe
     */
    public void initAttribut()
    {
        texteInternationalBoutons = ResourceBundle.getBundle("Traductions.boutons");
        texteInternationalLabels = ResourceBundle.getBundle("Traductions.labels");

        titre = new JLabel(texteInternationalBoutons.getString("titre"));
        titre.setHorizontalAlignment(JLabel.CENTER);
        lancerPartieRapide = new Vue_Bouton(texteInternationalBoutons.getString("partieRapide"));
        lancerPartieLocale = new Vue_Bouton(texteInternationalBoutons.getString("partieLocale"));
        lancerPartieContreIA = new Vue_Bouton(texteInternationalBoutons.getString("defierIA"));

        lancerPartieEnReseau = new Vue_Bouton(texteInternationalBoutons.getString("partieReseau"));
        chargerPartie = new Vue_Bouton(texteInternationalBoutons.getString("chargerPartie"));
        historique = new Vue_Bouton(texteInternationalBoutons.getString("historique"));
        statistiquesDuJoueur = new Vue_Bouton(texteInternationalBoutons.getString("statistiques"));
        options=new BackgroundButton(50, 50, "Images/iconesBouton/option.jpg");
        credits=new BackgroundButton(50, 50, "Images/iconesBouton/credit.jpg");
        quitter=new Vue_Bouton(texteInternationalBoutons.getString("quitter"));
        retourMenu=new Vue_Bouton(texteInternationalBoutons.getString("retourMenu"));
        lancerPartieLocale2=new Vue_Bouton(texteInternationalBoutons.getString("partieLocale"));
        nouveauPseudo=new Vue_Bouton(texteInternationalBoutons.getString("ajouterPseudo"));
        precedent=new Vue_Bouton(texteInternationalBoutons.getString("precedent"));
        suivant=new Vue_Bouton(texteInternationalBoutons.getString("suivant"));
        retour=new Vue_Bouton(texteInternationalBoutons.getString("retour"));
        rejoindrePartieReseau = new Vue_Bouton(texteInternationalBoutons.getString("rejoindrePartie"));
        rejoindrePartieReseauBoutonMenu = new Vue_Bouton(texteInternationalBoutons.getString("rejoindreUnePartieEnReseau"));
        creerPartieReseauBoutonMenu = new Vue_Bouton(texteInternationalBoutons.getString("creerUnePartieEnReseau"));
        nouveauPseudoCreerReseau = new Vue_Bouton(texteInternationalBoutons.getString("ajouterPseudo"));
        nouveauPseudoRejoindreReseau = new Vue_Bouton(texteInternationalBoutons.getString("ajouterPseudo"));

        // Options
        france = new JLabel(new ImageIcon("Images/drapeaux/franceFlag.jpg"));
        angleterre = new JLabel(new ImageIcon("Images/drapeaux/anglaisFlag.jpg"));
        francaisFlag = new JButton(france.getIcon());
        anglaisFlag = new JButton(angleterre.getIcon());
        francaisFlag.setBackground(Color.black);
        anglaisFlag.setBackground(Color.black);
        francaisFlag.setBorder(null);
        anglaisFlag.setBorder(null);
        titreOptions = new JLabel(texteInternationalLabels.getString("titreOption"));
        musiqueOff = new JRadioButton(texteInternationalBoutons.getString("musiqueOff"));
        musiqueOn = new JRadioButton(texteInternationalBoutons.getString("musiqueOn"), true);
        musique = new ButtonGroup();
        musique.add(musiqueOn);
        musique.add(musiqueOff);
        choixMusique = new JLabel(texteInternationalLabels.getString("musique"));
        choixLangue = new JLabel(texteInternationalLabels.getString("langue"));


        majListeJoueur();

        joueur1 = new JLabel(texteInternationalLabels.getString("pseudoJ1"));
        joueur2 = new JLabel(texteInternationalLabels.getString("pseudoJ2"));

        GraphicsEnvironment fontLabel = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsEnvironment fontTitre = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsEnvironment fontChoix = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try
        {
            fontLabel.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font/Cardinal.ttf")));
            fontTitre.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font/Ace Records.ttf")));
            fontChoix.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font/CalligraphyFLF.ttf")));
        }
        catch (FontFormatException fe)
        {
            Vue_FactorPopup.creerPopupErreurChargement();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
        Font policeTitre = new Font("Ace Records", Font.BOLD, 150);
        Font policeTitreSecondaire = new Font("Ace Records", Font.BOLD, 70);
        Font policeLabel = new Font("Cardinal", Font.BOLD, 25);
        titre.setFont(policeTitre);
        titreOptions.setFont(policeTitreSecondaire);
        joueur1.setFont(policeLabel);
        joueur2.setFont(policeLabel);
        choixLangue.setFont(policeLabel);
        choixMusique.setFont(policeLabel);
        musiqueOff.setFont(policeLabel);
        musiqueOn.setFont(policeLabel);
    }

    /**
     * initMenuPartie
     * Instancie les attributs de la bar de menu
     *
     */
    void initMenuPartie()
    {
        barMenu = new JMenuBar();

        JMenu optionPartie = new JMenu("Fichier");
        parametres = new JMenu("Options");

        retourMenuPrincipalMenu = new JMenuItem(texteInternationalBoutons.getString("retourMenu"));
        quitterMenu = new JMenuItem(texteInternationalBoutons.getString("quitter"));
        undoMenu = new JMenuItem(texteInternationalBoutons.getString("undo"));

        optionPartie.add(retourMenuPrincipalMenu);
        optionPartie.addSeparator();
        optionPartie.add(quitterMenu);



        barMenu.add(optionPartie);

        setJMenuBar(barMenu);
    }

    void addUndo()
    {
        parametres.add(undoMenu);
        barMenu.add(parametres);
    }

    /**
     * boolJOptionPane
     * Fenetre de dialogue qui demande confiramtion a l'utilisateur (choix YES ou NO)
     * @param message (texte affiché)
     * @return choix de l'utilisateur
     *
     */
    boolean boolJOptionPane(String message)
    {
        int answer = JOptionPane.showConfirmDialog(null, message, null, JOptionPane.YES_NO_OPTION);
        return (answer == JOptionPane.YES_OPTION);
    }

    /**
     * Organise la vue de l'accueil
     */
    private void creerWidgetAccueil()
    {
        boutonsMenu = new JPanel(new GridLayout(9, 1, 0, 15));
        boutonsMenu.setOpaque(false);
        boutonsMenu.add(lancerPartieRapide);
        boutonsMenu.add(lancerPartieLocale);
        boutonsMenu.add(lancerPartieContreIA);
        boutonsMenu.add(creerPartieReseauBoutonMenu);
        boutonsMenu.add(rejoindrePartieReseauBoutonMenu);
        boutonsMenu.add(chargerPartie);
        boutonsMenu.add(historique);
        boutonsMenu.add(statistiquesDuJoueur);
        //boutonsMenu.add(options);
        //boutonsMenu.add(credits);
        boutonsMenu.add(quitter);

        JPanel iconesOptionEtCredit = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        iconesOptionEtCredit.setOpaque(false);
        iconesOptionEtCredit.add(options);
        iconesOptionEtCredit.add(credits);




        JPanel center = new JPanel(new GridLayout(1, 3, 0, 0));
        center.getMinimumSize().width = xSize;
        center.setOpaque(false);
        JPanel gauche = new JPanel();
        gauche.setOpaque(false);
        //gauche.getPreferredSize().width = (int)(xSize*0.33333);
        center.add(gauche);


        //boutonsMenu.getPreferredSize().width = (int)(xSize*0.33333);
        center.add(boutonsMenu);
        //iconesOptionEtCredit.getPreferredSize().width =  (int)(xSize*0.33333);
        center.add(iconesOptionEtCredit);


        JPanel organisation = new JPanel(new BorderLayout());
        organisation.setOpaque(false);
        organisation.add(titre, BorderLayout.NORTH);
        organisation.add(center, BorderLayout.SOUTH);

        // Mis ene place du fond d'écran
        background = new BackgroundPanel(xSize, ySize, "Images/Fonds/fond1.jpg");//new ImageIcon("Images/Fonds/fond1.jpg"));
        background.setSize(xSize, ySize);
        background.setLayout(new FlowLayout());
        background.add(organisation, BorderLayout.SOUTH);

        setContentPane(background);
    }

    void creerWidgetChoixPseudos()
    {
        JPanel pseudos = new JPanel(new GridLayout(7, 3, 50, 20));
        pseudos.setOpaque(false);

        pseudos.add(Box.createVerticalGlue());
        pseudos.add(Box.createVerticalGlue());
        pseudos.add(Box.createVerticalGlue());

        pseudos.add(joueur1);
        pseudos.add(joueur2);
        pseudos.add(Box.createVerticalGlue());

        pseudos.add(listePseudo1);
        pseudos.add(listePseudo2);
        pseudos.add(nouveauPseudo);

        pseudos.add(Box.createVerticalGlue());
        pseudos.add(Box.createVerticalGlue());
        pseudos.add(Box.createVerticalGlue());

        pseudos.add(Box.createVerticalGlue());
        pseudos.add(Box.createVerticalGlue());
        pseudos.add(lancerPartieLocale2);

        pseudos.add(Box.createVerticalGlue());
        pseudos.add(Box.createVerticalGlue());
        pseudos.add(retourMenu);

        JPanel organisation = new JPanel(new BorderLayout());
        organisation.setOpaque(false);
        organisation.add(titre, BorderLayout.NORTH);
        organisation.add(pseudos, BorderLayout.SOUTH);

        // Mise en place du fond d'écran
        background = new BackgroundPanel(xSize, ySize, "Images/Fonds/fond1.jpg");//new ImageIcon("Images/Fonds/fond1.jpg"));
        background.setSize(xSize, ySize);
        background.setLayout(new FlowLayout());
        background.add(organisation, BorderLayout.CENTER);

        setContentPane(background);
    }

    /**
     * Lance la vue du plateau
     */
    void creerWidgetPartie()
    {
        initMenuPartie();
        setControlMenu(new Control_Partie_Menu(this, accueil));
        setContentPane(vue_plateau);
    }

    /**
     * Lance la vue des options
     */
    void creerWidgetOptions()
    {
        JPanel titres = new JPanel(new GridLayout(2,1));
        titres.setOpaque(false);
        titres.add(titre);
        titres.add(titreOptions);

        JPanel options = new JPanel(new GridLayout(3,3, 0, 20));
        options.setOpaque(false);
        francaisFlag.setOpaque(false);
        anglaisFlag.setOpaque(false);
        musiqueOff.setOpaque(false);
        musiqueOn.setOpaque(false);

        options.add(choixLangue);
        options.add(francaisFlag);
        options.add(anglaisFlag);

        options.add(choixMusique);
        options.add(musiqueOn);
        options.add(musiqueOff);

        options.add(Box.createVerticalGlue());
        options.add(Box.createVerticalGlue());
        options.add(retourMenu);

        JPanel organisation = new JPanel(new BorderLayout());
        organisation.setOpaque(false);
        organisation.add(titres, BorderLayout.NORTH);
        organisation.add(options, BorderLayout.SOUTH);

        // Mise en place du fond d'écran
        background = new BackgroundPanel(xSize, ySize, "Images/Fonds/fond1.jpg");//new ImageIcon("Images/Fonds/fond1.jpg"));
        background.setLayout(new FlowLayout());
        background.add(organisation, BorderLayout.CENTER);

        setContentPane(background);
    }

    /**
     * creerWidgetRejoindrePartieReseau
     * Place les éléments de la vue pour le formulaire permettant de rejoindre une partie en réseau
     *
     */
    void creerWidgetRejoindrePartieReseau()
    {
        JPanel titreJeu = new JPanel();
        titreJeu.setOpaque(false);
        titreJeu.add(titre);

        JPanel formulaire = new JPanel(new GridLayout(13, 1, 100, 0));
        formulaire.setOpaque(false);
        formulaire.add(joueur1);
        formulaire.add(listePseudo1);

        JPanel nouveauJ = new JPanel(new GridLayout(6, 1, 0, 30));
        nouveauJ.setOpaque(false);
        nouveauJ.add(nouveauPseudoRejoindreReseau);
        nouveauJ.add(rejoindrePartieReseau);
        nouveauJ.add(Box.createVerticalGlue());
        nouveauJ.add(Box.createVerticalGlue());
        nouveauJ.add(retourMenu);

        JPanel nouveauJP = new JPanel();
        nouveauJP.add(Box.createHorizontalStrut(100));
        nouveauJP.setOpaque(false);
        nouveauJP.add(nouveauJ);

        JPanel organisation = new JPanel(new BorderLayout());
        organisation.setOpaque(false);
        organisation.add(titreJeu, BorderLayout.NORTH);
        organisation.add(formulaire, BorderLayout.CENTER);
        organisation.add(nouveauJP, BorderLayout.EAST);


        // Mise en place du fond d'écran
        background = new BackgroundPanel(xSize, ySize, "Images/Fonds/fond1.jpg");//new ImageIcon("Images/Fonds/fond1.jpg"));
        background.setLayout(new FlowLayout());
        background.add(organisation, BorderLayout.CENTER);

        setContentPane(background);
    }

    /**
     * creerWidgetCreerPartieReseau
     * Place les éléments de la vue pour le formulaire de création d'une partie en réseau
     */
    void creerWidgetCreerPartieReseau()
    {
        JPanel titreJeu = new JPanel();
        titreJeu.setOpaque(false);
        titreJeu.add(titre);

        JPanel formulaire = new JPanel(new GridLayout(13, 1, 100, 0));
        formulaire.setOpaque(false);
        formulaire.add(joueur1);
        formulaire.add(listePseudo1);

        JPanel nouveauJ = new JPanel(new GridLayout(6, 1, 0, 30));
        nouveauJ.setOpaque(false);
        nouveauJ.add(nouveauPseudoCreerReseau);
        nouveauJ.add(lancerPartieEnReseau);
        nouveauJ.add(Box.createVerticalGlue());
        nouveauJ.add(Box.createVerticalGlue());
        nouveauJ.add(retourMenu);

        JPanel nouveauJP = new JPanel();
        nouveauJP.add(Box.createHorizontalStrut(100));
        nouveauJP.setOpaque(false);
        nouveauJP.add(nouveauJ);

        JPanel organisation = new JPanel(new BorderLayout());
        organisation.setOpaque(false);
        organisation.add(titreJeu, BorderLayout.NORTH);
        organisation.add(formulaire, BorderLayout.CENTER);
        organisation.add(nouveauJP, BorderLayout.EAST);


        // Mise en place du fond d'écran
        background = new BackgroundPanel(xSize, ySize, "Images/Fonds/fond1.jpg");//new ImageIcon("Images/Fonds/fond1.jpg"));
        background.setLayout(new FlowLayout());
        background.add(organisation, BorderLayout.CENTER);

        setContentPane(background);
    }

    /**
     * afficherMenu
     * Permet de relancer la musique du menu et d'afficher les boutons du menu
     *
     */
    void afficherMenu()
    {
        //MusiqueChess.playMedievalTheme();
        creerWidgetAccueil();
        setVisible(true);
    }

    /**
     * Ecoute les evenements sur les cases du plateau
     * @param e (ecouteur de type MouseListener)
     */
    void setPartieControl(MouseListener e)
    {
        if (vue_plateau != null)
            vue_plateau.addMouseListener(e);
    }

    void removePartieControl(MouseListener e)
    {
        if (vue_plateau != null)
            vue_plateau.removeMouseListener(e);
    }

    /**
     * Ecoute les évènements sur les boutons du menu
     * @param listener (ecouteur de type ActionListener)
     */
    void setButtonControl(ActionListener listener)
    {
        lancerPartieRapide.addActionListener(listener);
        lancerPartieLocale.addActionListener(listener);
        lancerPartieContreIA.addActionListener(listener);
        lancerPartieEnReseau.addActionListener(listener);
        chargerPartie.addActionListener(listener);
        historique.addActionListener(listener);
        statistiquesDuJoueur.addActionListener(listener);
        options.addActionListener(listener);
        credits.addActionListener(listener);
        quitter.addActionListener(listener);
        retourMenu.addActionListener(listener);
        lancerPartieLocale2.addActionListener(listener);
        nouveauPseudo.addActionListener(listener);
        francaisFlag.addActionListener(listener);
        anglaisFlag.addActionListener(listener);
        musiqueOff.addActionListener(listener);
        musiqueOn.addActionListener(listener);
        creerPartieReseauBoutonMenu.addActionListener(listener);
        rejoindrePartieReseauBoutonMenu.addActionListener(listener);
        rejoindrePartieReseau.addActionListener(listener);
        nouveauPseudoCreerReseau.addActionListener(listener);
        nouveauPseudoRejoindreReseau.addActionListener(listener);
    }

    void setButtonHistoriqueControl(ActionListener listener)
    {
        retour.addActionListener(listener);
        suivant.addActionListener(listener);
        precedent.addActionListener(listener);
    }


    /**
     * setControlMenu
     * Ecoute les evenements du menu
     * @param e (ecouteur de type ActionListener)
     */
    void setControlMenu(ActionListener e)
    {
        retourMenuPrincipalMenu.addActionListener(e);
        undoMenu.addActionListener(e);
        quitterMenu.addActionListener(e);
    }

    void choixHistoriqueAConsulter()
    {
        int i,j;
        BDDManager bdd = new BDDManager();
        bdd.start();

        // On récupère les id des joueurs ayants sauvegardé une partie
        ArrayList<ArrayList<String>> idJoueursHistorique = bdd.ask("SELECT joueurBlanc_id, joueurNoir_id" +
                " FROM HISTORIQUEPARTIE;");

        System.out.println(idJoueursHistorique);
        if(idJoueursHistorique.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Il n'y a pas d'historique disponible.",
                    "Voir l'historique d'une partie", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList<ArrayList<String>> datesParties = bdd.ask("SELECT dateHistorique FROM HISTORIQUEPARTIE;");

        String[][] pseudosJoueurs = new String[idJoueursHistorique.size()][2];

        String[] joueursBlancs = new String[idJoueursHistorique.size()];
        String[] joueursNoirs = new String[idJoueursHistorique.size()];
        String[] dates = new String[datesParties.size()];

        // On récupère les pseudos corespondants à chaque id récupérés précédemment
        for (i = 0; i < idJoueursHistorique.size(); i++)
        {
            for(j=0; j<idJoueursHistorique.get(i).size(); j++)
            {
                pseudosJoueurs[i][j] = bdd.ask("SELECT JOUEUR.pseudoJoueur FROM JOUEUR WHERE JOUEUR.id = "
                        + idJoueursHistorique.get(i).get(j) + ";").get(0).get(0) + "";
            }
            joueursBlancs[i] = pseudosJoueurs[i][0];
            joueursNoirs[i] = pseudosJoueurs[i][1];
        }
        for (i = 0; i < datesParties.size(); i++)
            for(j=0; j<datesParties.get(i).size(); j++)
                dates[i] = datesParties.get(i).get(j);

        // On crée une liste qui va être affichée dans une fenetre popup pour que l'utilisateur choisisse
        // quelle sauvegarde il veut reprendre
        String[] possibilitesParties = new String[idJoueursHistorique.size()];
        for(i=0; i<possibilitesParties.length; i++)
            possibilitesParties[i] = joueursBlancs[i] + " VS " + joueursNoirs[i] + " le " + dates[i];

        // On crée la boite de dialogue
        accueil.setPartieAVisualiser((String) JOptionPane.showInputDialog(null, "De quelle partie voulez-vous charger l'historique ?",
                "Voir l'historique d'une partie", JOptionPane.QUESTION_MESSAGE, null, possibilitesParties,
                possibilitesParties[0]));

        bdd.stop();
    }

    /**
     * messagePop
     * Permet d'afficher une fenetre pop-up avec un champs de saisi pour récupérer une information
     * @param message (texte à afficher)
     * @return fenetre qui s'affiche
     */
    String messagePop(String message)
    {
        return JOptionPane.showInputDialog(this, message, "ChessMaster", JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * jOptionMessage
     * Fenetre de dialogue permettant de donner une information a l'utilisateur
     * @param message (texte affiché)
     *
     */
    void jOptionMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message, "A propos", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * majListeJoueur
     * Met à jour la liste des joueurs pour le formulaire
     */
    void majListeJoueur()
    {
        listePseudo1 = new JComboBox(accueil.listePseudos());
        listePseudo2 = new JComboBox(accueil.listePseudos());
    }

    void creerWidgetAfficherHistorique()
    {
        vue_plateau = new Vue_Plateau(this, accueil);
        vueHisto = new JFrame();

        JPanel panelBoard = new JPanel();
        panelBoard.setLayout(new GridLayout(1,1));
        panelBoard.add(vue_plateau);

        JPanel panelButton = new JPanel();
        panelButton.add(precedent);
        panelButton.add(suivant);
        panelButton.add(retour);

        JPanel panelGeneral = new JPanel();
        panelGeneral.setLayout(new BoxLayout(panelGeneral, BoxLayout.Y_AXIS));
        panelGeneral.add(panelBoard);
        panelGeneral.add(panelButton);

        setContentPane(panelGeneral);
    }

    ArrayList<String> recupererHistoCoupsPartie()
    {
        BDDManager bdd = new BDDManager();
        bdd.start();

        // partieAVisualiser au format : "pseudo1 VS pseudo2"
        String[] partieAVisualiserSplitee = accueil.getPartieAVisualiser().split(" ");
        String joueurBlanc = partieAVisualiserSplitee[0];
        String joueurNoir = partieAVisualiserSplitee[2];
        String dates = partieAVisualiserSplitee[4] + " " + partieAVisualiserSplitee[5];

        String idJB = bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + joueurBlanc + "';").get(0).get(0);
        String idJN = bdd.ask("SELECT id FROM JOUEUR WHERE pseudoJoueur LIKE '" + joueurNoir + "';").get(0).get(0);
        String requete = "SELECT coupsJouee FROM HISTORIQUEPARTIE "
                + "WHERE joueurBlanc_id = " + idJB
                + " AND joueurNoir_id = " + idJN
                + " AND dateHistorique = '" + dates
                + "';";
        String histo = bdd.ask(requete).get(0).get(0);
        ArrayList<String> coups = new ArrayList<>();
        for(int i=0; i<histo.split(":").length; i++)
            coups.add(histo.split(":")[i]);
        bdd.stop();
        return coups;
    }

    /**
     * Propose une liste déroulante des joueurs de la base de donnée
     */
    int statistiquesJoueur()
    {
        int i;
        BDDManager bdd = new BDDManager();
        bdd.start();

        ArrayList<ArrayList<String>> listeJoueur = bdd.ask("SELECT * FROM JOUEUR;");
        if(listeJoueur.isEmpty())
        {
            jOptionMessage("Aucun joueur dans la base de données. Créez des joueurs dans \"Partie Locale\".");
            return 0;
        }
        String[] pseudoJoueurs = new String[listeJoueur.size()];
        for(i=0;i<listeJoueur.size(); i++)
            pseudoJoueurs[i] = listeJoueur.get(i).get(1);

        accueil.setPseudoChoisi((String) JOptionPane.showInputDialog(null, "Afficher les statistique du joueur :",
                "Statistiques", JOptionPane.QUESTION_MESSAGE, null, pseudoJoueurs,
                pseudoJoueurs[0]));

        bdd.stop();
        return 1;
    }

    /**
     * Provenance : Jeux d'Echec
     * Affiche les statistiques du joueur dont le nom est passé en paramètre
     * @param pseudo (joueur dont les statistiques doivent être affiché)
     */
    void fenetreStatsJoueur(String pseudo)
    {
        BDDManager bdd = new BDDManager();
        bdd.start();

        ArrayList<ArrayList<String>> caracteristique = bdd.ask(
                "SELECT * FROM JOUEUR WHERE pseudoJoueur = '" + pseudo + "';");
        int partiesJouees = Integer.parseInt(caracteristique.get(0).get(2)) +
                Integer.parseInt(caracteristique.get(0).get(3));

        String stats = "\n\nPseudo : " + caracteristique.get(0).get(1) + "\n" +
                "Nombre de parties jouées : " + partiesJouees + "\n" +
                "Nombre de parties gagnées : " + caracteristique.get(0).get(2) + "\n" +
                "Nombre de parties perdues : " + caracteristique.get(0).get(3) + "\n";

        JOptionPane.showMessageDialog(this, "Statistiques :" + stats, "Statistiques d'un joueur", JOptionPane.INFORMATION_MESSAGE);

        bdd.stop();
    }

    void afficherPartiesACharger()
    {
        BDDManager bdd = new BDDManager();
        bdd.start();
        //Récupération des id des joueurs qui ont une partie sauvegardée
        ArrayList<ArrayList<String>> joueurs = bdd.ask("SELECT joueurBlancSave, joueurNoirSave FROM SAUVEGARDEPARTIE;");

        // Cas ou il n'y a pas de parties sauvegardée
        if(joueurs.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Il n'y a pas de partie enregistrée.", "Continuer une partie", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Construction des lignes à proposer en fonction du pseudo des deux joueurs
        String[] nomPartiesDispos = new String[joueurs.size()];
        String[] pseudos = new String[2];
        for(int i=0; i<joueurs.size(); i++)
        {
            // Transformation idJoueur --> pseudo
            pseudos[0] = bdd.ask("SELECT pseudoJoueur FROM JOUEUR WHERE id = " + joueurs.get(i).get(0)).get(0).get(0);
            pseudos[1] = bdd.ask("SELECT pseudoJoueur FROM JOUEUR WHERE id = " + joueurs.get(i).get(1)).get(0).get(0);
            nomPartiesDispos[i] = pseudos[0] + " VS " + pseudos[1];
        }

        // On crée la boite de dialogue
        accueil.setPartieACharger((String) JOptionPane.showInputDialog(null, "Quelle partie voulez-vous continuer ?",
                "Continuer une partie", JOptionPane.QUESTION_MESSAGE, null, nomPartiesDispos,
                nomPartiesDispos[0]));

        bdd.stop();
    }
    // GETTERS & SETTERS
    Vue_Bouton getLancerPartieContreIA() { return lancerPartieContreIA; }
    Vue_Plateau getVue_plateau() { return vue_plateau; }
    void setVue_plateau(Vue_Plateau vue_plateau) { this.vue_plateau = vue_plateau; }
    void display(){
        setVisible(true);
        updateSizePanelBouton();
        setVisible(true);
    }
    Vue_Bouton getLancerPartieLocale() { return lancerPartieLocale; }
    Vue_Bouton getLancerPartieEnReseau() { return lancerPartieEnReseau; }
    Vue_Bouton getChargerPartie() { return chargerPartie; }
    Vue_Bouton getHistorique() { return historique; }
    Vue_Bouton getStatistiquesDuJoueur() { return statistiquesDuJoueur; }
    JButton getOptions() { return options;}
    JButton getCredits() { return credits; }
    Vue_Bouton getQuitter() { return quitter; }
    JMenuItem getRetourMenuPrincipalMenu() {
        return retourMenuPrincipalMenu;
    }
    JMenuItem getUndoMenu() {
        return undoMenu;
    }
    JMenuItem getQuitterMenu() {
        return quitterMenu;
    }
    Vue_Bouton getRetourMenu() {
        return retourMenu;
    }
    Vue_Bouton getLancerPartieLocale2() {
        return lancerPartieLocale2;
    }
    Vue_Bouton getNouveauPseudo() {
        return nouveauPseudo;
    }
    JComboBox getListePseudo1() {
        return listePseudo1;
    }
    JComboBox getListePseudo2() {
        return listePseudo2;
    }
    Vue_Bouton getPrecedent() {
        return precedent;
    }
    Vue_Bouton getRetour() {
        return retour;
    }
    Vue_Bouton getSuivant() {
        return suivant;
    }
    JButton getFrancaisFlag() {
        return francaisFlag;
    }
    void setFrancaisFlag(JButton francaisFlag) {
        this.francaisFlag = francaisFlag;
    }
    JButton getAnglaisFlag() {
        return anglaisFlag;
    }
    void setAnglaisFlag(JButton anglaisFlag) {
        this.anglaisFlag = anglaisFlag;
    }
    JLabel getFrance() {
        return france;
    }
    void setFrance(JLabel france) {
        this.france = france;
    }
    JLabel getAngleterre() {
        return angleterre;
    }
    void setAngleterre(JLabel angleterre) {
        this.angleterre = angleterre;
    }
    Vue_Bouton getLancerPartieRapide() {
        return lancerPartieRapide;
    }
    JRadioButton getMusiqueOn() {
        return musiqueOn;
    }
    void setMusiqueOn(JRadioButton musiqueOn) {
        this.musiqueOn = musiqueOn;
    }
    JRadioButton getMusiqueOff() {
        return musiqueOff;
    }
    void setMusiqueOff(JRadioButton musiqueOff) {
        this.musiqueOff = musiqueOff;
    }
    Vue_Bouton getRejoindrePartieReseauBoutonMenu() {
        return rejoindrePartieReseauBoutonMenu;
    }
    void setRejoindrePartieReseauBoutonMenu(Vue_Bouton rejoindrePartieReseauBoutonMenu) {
        this.rejoindrePartieReseauBoutonMenu = rejoindrePartieReseauBoutonMenu;
    }
    Vue_Bouton getCreerPartieReseauBoutonMenu() {
        return creerPartieReseauBoutonMenu;
    }
    void setCreerPartieReseauBoutonMenu(Vue_Bouton creerPartieReseauBoutonMenu) {
        this.creerPartieReseauBoutonMenu = creerPartieReseauBoutonMenu;
    }
    Vue_Bouton getRejoindrePartieReseau() {
        return rejoindrePartieReseau;
    }
    void setRejoindrePartieReseau(Vue_Bouton rejoindrePartieReseau) {
        this.rejoindrePartieReseau = rejoindrePartieReseau;
    }
    Vue_Bouton getNouveauPseudoCreerReseau() {
        return nouveauPseudoCreerReseau;
    }
    void setNouveauPseudoCreerReseau(Vue_Bouton nouveauPseudoCreerReseau) {
        this.nouveauPseudoCreerReseau = nouveauPseudoCreerReseau;
    }
    Vue_Bouton getNouveauPseudoRejoindreReseau() {
        return nouveauPseudoRejoindreReseau;
    }

    void setNouveauPseudoRejoindreReseau(Vue_Bouton nouveauPseudoRejoindreReseau) {
        this.nouveauPseudoRejoindreReseau = nouveauPseudoRejoindreReseau;
    }

    void updateSizePanelBouton(){
        boutonsMenu.setPreferredSize(new Dimension(boutonsMenu.getWidth(), ySize-titre.getHeight()-150));
    }
}