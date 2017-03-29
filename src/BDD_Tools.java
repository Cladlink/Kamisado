/**
 Created by cladlink on 06/11/16.
 */

class BDD_Tools
{
    static int saveHistory(int j1, int j2, String history)
    {
        String requeteHistorique = "INSERT INTO HISTORIQUEPARTIE VALUES (null, "
                + j1 + ", "
                + j2 + ", '"
                + history + "', now());";
        BDDManager bdd = new BDDManager();
        bdd.start();
        bdd.edit(requeteHistorique);
        int id = Integer.parseInt(bdd.ask("SELECT id FROM HISTORIQUEPARTIE ORDER BY id DESC LIMIT 1")
                .get(0).get(0));
        System.out.println("id  " + id);
        bdd.stop();
        return id;
    }
}
