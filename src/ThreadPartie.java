import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
  Created by cladlink on 12/04/16.
 */
class ThreadPartie extends Thread
{
    private String monPseudo, pseudoAdversaire;
    private boolean jeSuisBlanc;
    private Model_Accueil accueil;
    private boolean isServer;
    private Socket comm;
    private Control_Partie controller;
    private Control_Menu_Accueil cbm;
    private int port;

    // ajout SD
    private String ipServer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int id; // =1 si joueur blanc et 2 si joueur noir

    /**
     * ThreadPartie
     * initie la partie pour le client
     *
     * @param accueil (model)
     * @param controller (controller de la partie)
     * @param port (port de communication)
     * @param isServer (suis-je serveur)
     * @param ipServer (ip de communication)
     * @param pseudo (pseudonyme du joueur)
     * @param cbm (controlleur du menu d'accueil)
     */
    ThreadPartie(Model_Accueil accueil, Control_Partie controller, int port, boolean isServer,
                 String ipServer, String pseudo, Control_Menu_Accueil cbm)
    {
        this.accueil = accueil;
        this.controller = controller;
        this.port = port;
        this.isServer = isServer;
        this.ipServer = ipServer;
        this.monPseudo = pseudo;
        this.cbm = cbm;
        this.jeSuisBlanc = accueil.getPartie().jeSuisBlanc();
    }

    /**
     * run
     *
     */
    @Override
    public void run()
    {
        boolean stop = false;

        if(isServer)
            try
            {
                initServer();
            }
            catch(ClassNotFoundException | IOException e)
            {
                e.printStackTrace();
            }
        else
            try
            {
                initClient();
            }
            catch (ClassNotFoundException | IOException e)
            {
                e.printStackTrace();
            }

        Model_Partie partie = accueil.getPartie();
        try
        {
            while (!stop)
            {
                if ( id == partie.getIdCurrentPlayer() )
                {
                    System.out.println("C'est à moi de jouer");
                    controller.debutTour();
                    partie.waitFinTour();
                    int rowSrc = partie.getCaseSrc().getRow();
                    int colSrc = partie.getCaseSrc().getColumn();
                    int rowDest = partie.getCaseDest().getRow();
                    int colDest = partie.getCaseDest().getColumn();
                    oos.writeInt(rowSrc);
                    oos.writeInt(colSrc);
                    oos.writeInt(rowDest);
                    oos.writeInt(colDest);
                    oos.writeBoolean(partie.isPartieFinie());
                    oos.flush();
                }
                else
                {
                    System.out.println("C'est à l'adversaire de jouer");
                    controller.enableView(false);
                    int srcX = ois.readInt();
                    int srcY = ois.readInt();
                    int destX = ois.readInt();
                    int destY = ois.readInt();
                    boolean partieFinie = ois.readBoolean();
                    if (partieFinie)
                    {
                        stop = true;
                        controller.updatePartie(srcX, srcY, destX, destY);
                        partie.setTourDuJoueurBlanc(!partie.isTourDuJoueurBlanc());
                        partie.setJoueurBLancGagnant(!jeSuisBlanc);
                        controller.finPartieReseauPerdant(id);
                    }
                    else
                        controller.updatePartie(srcX, srcY, destX, destY);
                    partie.setTourDuJoueurBlanc(!partie.isTourDuJoueurBlanc());
                    accueil.getPartie().setDernierPionJoue(partie.getPlateau().getBoard()[destX*Model_Plateau.LIGNE+destY].getPion());
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * initServer
     * initie les sockets pour le serveur
     *
     * @throws IOException ()
     * @throws ClassNotFoundException ()
     */
    private void initServer() throws IOException,ClassNotFoundException
    {
        ServerSocket conn = new ServerSocket(port);
        comm = conn.accept();
        oos = new ObjectOutputStream(comm.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(comm.getInputStream());
        oos.writeObject(monPseudo);
        oos.writeBoolean(!jeSuisBlanc);
        oos.flush();
        pseudoAdversaire = (String)ois.readObject();
        if (jeSuisBlanc)
            accueil.demarrerPartieReseau(pseudoAdversaire, monPseudo, true);
        else
            accueil.demarrerPartieReseau(monPseudo, pseudoAdversaire, true);
        setId();
        cbm.initPartie();
    }

    /**
     * initClient
     * initialise les sockets pour le client
     *
     * @throws IOException ()
     * @throws ClassNotFoundException ()
     */
    private void initClient() throws IOException,ClassNotFoundException
    {
        comm = new Socket(ipServer, port);
        ois = new ObjectInputStream(comm.getInputStream());
        oos = new ObjectOutputStream(comm.getOutputStream());
        oos.flush();
        pseudoAdversaire = (String)ois.readObject();
        jeSuisBlanc = ois.readBoolean();
        oos.writeObject(monPseudo);
        oos.flush();
        setId();
        if (jeSuisBlanc)
            accueil.demarrerPartieReseau(pseudoAdversaire, monPseudo, true);
        else
            accueil.demarrerPartieReseau(monPseudo, pseudoAdversaire, true);
        cbm.initPartie();
    }

    /**
     * setID
     * définit l'ID en fonction de si le joueur est blanc ou noir
     */
    private void setId()
    {
        if (jeSuisBlanc)
            id = 1;
        else
            id = 2;
    }
}