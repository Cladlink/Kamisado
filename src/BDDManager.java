import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
  Created by cladlink on 12/03/16.
 */

class BDDManager
{
    private final String BDD_URL = BDD_ID.BDD_URL;
    private final String BDD_USER = BDD_ID.BDD_USER;
    private final String BDD_PASSWORD =  BDD_ID.BDD_PASSWORD;
    private final String BDD_DATABASE =  BDD_ID.BDD_DATABASE;

    private Connection connection;
    private Statement statement;


    /**
     * start()
     * sert à initialiser la connexion à la BDD
     */
    void start()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(BDD_URL, BDD_USER, BDD_PASSWORD);
            statement = connection.createStatement();
            edit("USE " + BDD_DATABASE + ";");
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * stop()
     * Sert à rompre la connexion avec le BDD
     */
    void stop()
    {

        try
        {
            connection.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * edit
     * Sert pour l'envoie de toutes requêtes sauf les SELECT
     * @param requete ()
     */
    void edit(String requete)
    {
        System.out.println(requete);
        try
        {
            statement.executeUpdate(requete);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ask(String Requete)
     * Sert à envoyer au serveur toute requête de type SELECT
     * @param requete ()
     * @return ()
     */
    ArrayList<ArrayList<String>> ask(String requete)
    {
        System.out.println(requete);
        ArrayList<ArrayList<String>> select = new ArrayList<>();

        try
        {
            ResultSet rs = statement.executeQuery(requete);
            ResultSetMetaData rsmd = rs.getMetaData();
            int nbcols = rsmd.getColumnCount();

            int i=0;
            while(rs.next())
            {
                select.add(new ArrayList<>());
                for (int j = 1; j <= nbcols; j++)
                    select.get(i).add(rs.getString(j));
                i++;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return select;
    }

    /**
     * lire()
     * permet de lire un fichier sql et de l'exécuter
     */
    void lire(String adressSQLFile)
    {
        BufferedReader lecture;
        String fichier = "", fichierTemp;
        String[] requete;
        try
        {
            lecture = new BufferedReader(new FileReader(adressSQLFile));
            try
            {
                while ((fichierTemp = lecture.readLine()) != null)
                {
                    fichier += fichierTemp;
                    fichier += " ";
                }
                requete = fichier.split(";");
                for (int i = 0; i<requete.length; i++)
                {
                    requete[i] += ";";
                    System.out.println(i);
                    System.out.println(requete[i]);
                    if (requete[i].contains("SELECT"))
                        this.ask(requete[i]);
                    else
                        this.edit(requete[i]);
                }
            }
            catch (EOFException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    lecture.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
        catch (FileNotFoundException e)
        {
            System.err.println("le fichier est introuvable");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * main
     * ce main n'est utiliser que pour créer les tables.
     */
    public static void main(String[] args)
    {
        BDDManager bdd = new BDDManager();
        bdd.start();

        //bdd.lire("src/BDD_Kamisado.sql");
        /*ArrayList<ArrayList<String>> test = bdd.ask("SELECT * FROM HISTORIQUEPARTIE;");
        for (ArrayList<String> aTest : test) System.out.println(aTest);*/

        /*bdd.edit(
                "INSERT INTO JOUEUR(pseudoJoueur, nbPartiesGagneesJoueur, nbPartiesPerduesJoueur) VALUES" +
                        "(\"toto\", 0, 0);");*/
        //System.out.println(bdd.ask("SELECT * FROM JOUEUR;"));
        bdd.edit("DELETE FROM SAUVEGARDEPARTIE;");
        //bdd.edit("DELETE FROM HISTORIQUE;");
        bdd.edit("DELETE FROM HISTORIQUEPARTIE;");
        bdd.edit("DELETE FROM JOUEUR;");
        //bdd.edit("drop table JOUEUR;");
        /*bdd.edit("INSERT INTO JOUEUR (pseudoJoueur, nbPartiesJoueur, nbPartiesGagneesJoueur," +
                " nbPartiesPerduesJoueur, nbPartiesAbandonneeJoueur, partieEnCoursJoueur, trophee1, trophee2, trophee3)" +
                       " VALUES (\"toto\", 0, 0, 0, 0, 0, false, false, false);");
        bdd.edit("INSERT INTO JOUEUR (pseudoJoueur, nbPartiesJoueur, nbPartiesGagneesJoueur, " +
                "nbPartiesPerduesJoueur, nbPartiesAbandonneeJoueur, partieEnCoursJoueur, trophee1, trophee2, trophee3)" +
                " VALUES (\"titi\", 0, 0, 0, 0, 0, false, false, false);");*/
       //bdd.edit("INSERT INTO HISTORIQUE VALUES (null, 26, 28, '2015-2-12', 'PB1213-PB1213-PB1213-PB1213-PB1213-PB1213-PB1213');");
        //bdd.edit("INSERT INTO SAUVEGARDE VALUES (null, 25, 26, null, true, 'PB12-PN13-RN65', 14);");
        /*ArrayList<ArrayList<String>> test = bdd.ask("DELETE FROM HISTORIQUE;");
        for (int i = 0; i < test.size(); i++)
        {
            System.out.println(test.get(i));
        }*/
        ArrayList<ArrayList<String>> bla = bdd.ask("SELECT * FROM SAUVEGARDEPARTIE;");
        for (int i = 0; i < bla.size(); i++)
        {
            System.out.println(bla.get(i));
        }
        bdd.stop();
    }
}
