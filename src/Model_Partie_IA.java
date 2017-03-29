import java.util.Arrays;

//blanc = joueur


/**
 * On définira dans cette classe et dans un soucis d'économie de place :
 * - les pions blancs et noirs seront définis de la facon suivante :
 * <p>
 * ----------------------
 * 00 = pion blanc marron
 * 01 = pion blanc vert
 * 02 = pion blanc rouge
 * 03 = pion blanc jaune
 * 04 = pion blanc rose
 * 05 = pion blanc violet
 * 06 = pion blanc bleu
 * 07 = pion blanc orange
 * ----------------------
 * 08 = pion noir marron
 * 09 = pion noir vert
 * 10 = pion noir rouge
 * 11 = pion noir jaune
 * 12 = pion noir rose
 * 13 = pion noir violet
 * 14 = pion noir bleu
 * 15 = pion noir orange
 * ----------------------
 * <p>
 * - les couleurs :
 * 0 = marron
 * 1 = vert
 * 2 = rouge
 * 3 = jaune
 * 4 = rose
 * 5 = violet
 * 6 = bleu
 * 7 = orange
 */
class Model_Partie_IA {
    int lvl1;
    int lvl3;
    int lvl5;
    int lvl7;
    static final byte MARRON = 0;
    static final byte GREEN = 1;
    static final byte RED = 2;
    static final byte YELLOW = 3;
    static final byte PINK = 4;
    static final byte VIOLET = 5;
    static final byte BLUE = 6;
    static final byte ORANGE = 7;

    static final byte LIGNE = 8;
    static final byte NBCASESATTEIGNABLESPOSSIBLESPREMIERTOUR = 14;
    private static final byte NBCASESATTEIGNABLESPOSSIBLESJOUEURCOURANT = 15;
    private static final byte PIONBLANCMARRON = 0;
    private static final byte PIONBLANCVERT = 1;
    private static final byte PIONBLANCROUGE = 2;
    private static final byte PIONBLANCJAUNE = 3;
    private static final byte PIONBLANCROSE = 4;
    private static final byte PIONBLANCVIOLET = 5;
    private static final byte PIONBLANCBLEU = 6;
    private static final byte PIONBLANCORANGE = 7;
    private static final byte PIONNOIRMARRON = 8;
    private static final byte PIONNOIRVERT = 9;
    private static final byte PIONNOIRROUGE = 10;
    private static final byte PIONNOIRJAUNE = 11;
    private static final byte PIONNOIRROSE = 12;
    private static final byte PIONNOIRVIOLET = 13;
    private static final byte PIONNOIRBLEU = 14;
    private static final byte PIONNOIRORANGE = 15;

    private boolean isTourUn = true;
    private boolean tourDuJoueurBlanc = true;
    private boolean joueurBlancGagnant = false;
    private boolean estGagnee = false;

    private byte[] plateau;
    private byte[] plateauCase;

    //pion selectionné (choix au tour 1 / pion qui doit être joué pour les autres tours)
    private byte pionMemoire = -1;
    private byte casePionMemoire = -1;
    private byte couleurPionAJouer = -1;

    //pion qui a été joué au dernier tour
    private byte dernierPionJoue = -1;
    private byte caseDernierPionJoue = -1;
    private byte caseDestDernierPionJoue = -1;

    // 14 = nombre de cases atteignables max
    // on notera -1 pour les cases non utilisées
    public byte[] casesAtteignablesJoueurCourant = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    // 8 = nb pions
    private byte[][] casesAtteignablesTourUn = new byte[8][14];

    private static final byte VICTOIRE_IA = 0;
    private static final byte DEFAITE_IA = 1;
    private static final byte NI_VICTOIRE_NI_DEFAITE = 2;
    private static final byte PROFONDEUR_MINMAX = 7;    //pair : IA qui finit de jouer : pas bon pour evalFinProfondeur




    public static final int MOINS_INFINI = -1_000_000;
    public static final int INFINI = 1_000_000;



    //constantes de cotation
    private static final int PTS_DEFAITE_IA = -10_000;
    private static final int PTS_VICTOIRE_IA = 10_000;
    private static final int PEUT_ATTEINDRE_UNE_CASE_AU_BOUT_DU_PLATEAU = 10;
    private static final int PEUT_ATTEINDRE_DEUX_CASES_AU_BOUT_DU_PLATEAU = 20;
    private static final int PEUT_ATTEINDRE_TROIS_CASES_AU_BOUT_DU_PLATEAU = 25;


    //attributs simulerCoup
    private byte sauvegardeCouleur;
    private byte sauvegardePlateau1;
    private byte sauvegardePlateauPionMem;
    private byte sauvegardeDerPionJoue;
    private byte sauvegardeCaseDerPionJoue;
    private byte[] sauvegardeCasesAtteignablesJoueurCourant;
    private byte sauvegardeCasePionMemoire;

    Model_Partie_IA() {
        plateau = new byte[LIGNE * LIGNE];
        Arrays.fill(plateau, (byte) -1);
        placePionsBlancs();
        placePionsNoirs();
        initPlateauCase();

    }


    private void initPlateauCase() {
        plateauCase = new byte[64];
        plateauCase[0] = MARRON;
        plateauCase[1] = GREEN;
        plateauCase[2] = RED;
        plateauCase[3] = YELLOW;
        plateauCase[4] = PINK;
        plateauCase[5] = VIOLET;
        plateauCase[6] = BLUE;
        plateauCase[7] = ORANGE;

        plateauCase[8] = VIOLET;
        plateauCase[9] = MARRON;
        plateauCase[10] = YELLOW;
        plateauCase[11] = BLUE;
        plateauCase[12] = GREEN;
        plateauCase[13] = PINK;
        plateauCase[14] = ORANGE;
        plateauCase[15] = RED;

        plateauCase[16] = BLUE;
        plateauCase[17] = YELLOW;
        plateauCase[18] = MARRON;
        plateauCase[19] = VIOLET;
        plateauCase[20] = RED;
        plateauCase[21] = ORANGE;
        plateauCase[22] = PINK;
        plateauCase[23] = GREEN;

        plateauCase[24] = YELLOW;
        plateauCase[25] = RED;
        plateauCase[26] = GREEN;
        plateauCase[27] = MARRON;
        plateauCase[28] = ORANGE;
        plateauCase[29] = BLUE;
        plateauCase[30] = VIOLET;
        plateauCase[31] = PINK;

        plateauCase[32] = PINK;
        plateauCase[33] = VIOLET;
        plateauCase[34] = BLUE;
        plateauCase[35] = ORANGE;
        plateauCase[36] = MARRON;
        plateauCase[37] = GREEN;
        plateauCase[38] = RED;
        plateauCase[39] = YELLOW;

        plateauCase[40] = GREEN;
        plateauCase[41] = PINK;
        plateauCase[42] = ORANGE;
        plateauCase[43] = RED;
        plateauCase[44] = VIOLET;
        plateauCase[45] = MARRON;
        plateauCase[46] = YELLOW;
        plateauCase[47] = BLUE;

        plateauCase[48] = RED;
        plateauCase[49] = ORANGE;
        plateauCase[50] = PINK;
        plateauCase[51] = GREEN;
        plateauCase[52] = BLUE;
        plateauCase[53] = YELLOW;
        plateauCase[54] = MARRON;
        plateauCase[55] = VIOLET;

        plateauCase[56] = ORANGE;
        plateauCase[57] = BLUE;
        plateauCase[58] = VIOLET;
        plateauCase[59] = PINK;
        plateauCase[60] = YELLOW;
        plateauCase[61] = RED;
        plateauCase[62] = GREEN;
        plateauCase[63] = MARRON;
    }

    private void placePionsBlancs() {
        plateau[7] = PIONBLANCORANGE;
        plateau[6] = PIONBLANCBLEU;
        plateau[5] = PIONBLANCVIOLET;
        plateau[4] = PIONBLANCROSE;
        plateau[3] = PIONBLANCJAUNE;
        plateau[2] = PIONBLANCROUGE;
        plateau[1] = PIONBLANCVERT;
        plateau[0] = PIONBLANCMARRON;
    }

    private void placePionsNoirs() {
        plateau[63] = PIONNOIRMARRON;
        plateau[62] = PIONNOIRVERT;
        plateau[61] = PIONNOIRROUGE;
        plateau[60] = PIONNOIRJAUNE;
        plateau[59] = PIONNOIRROSE;
        plateau[58] = PIONNOIRVIOLET;
        plateau[57] = PIONNOIRBLEU;
        plateau[56] = PIONNOIRORANGE;
    }

    void setCasesAtteignablesPremierTour() {
        for (int i = 0; i < casesAtteignablesTourUn.length; i++) {
            for (int j = 0; j < casesAtteignablesTourUn[i].length; j++)
                casesAtteignablesTourUn[i][j] = -1;
        }


        for (int i = 0; i < casesAtteignablesTourUn.length; i++) {
            int row = i / 8, column = i % 8;
            int decal = 1, curseur = 0;
            boolean deplacableNS = true, deplacableO = true, deplacableE = true;

            while (deplacableNS || deplacableO || deplacableE) {
                if (deplacableNS) {
                    if (row + decal <= 7
                            && plateau[column + (row + decal) * LIGNE] < 0) {
                        casesAtteignablesTourUn[i][curseur] = (byte) (column + (row + decal) * LIGNE);
                        curseur++;
                    } else
                        deplacableNS = false;
                }
                if (deplacableO) {
                    if (row + decal <= 7 && column - decal >= 0
                            && plateau[(column - decal) + (row + decal) * LIGNE] < 0) {
                        casesAtteignablesTourUn[i][curseur] = (byte) ((column - decal) + (row + decal) * LIGNE);
                        curseur++;
                    } else
                        deplacableO = false;
                }
                if (deplacableE) {
                    if (row + decal <= 7 && column + decal <= 7
                            && plateau[(column + decal) + (row + decal) * LIGNE] < 0) {
                        casesAtteignablesTourUn[i][curseur] = (byte) ((column + decal) + (row + decal) * LIGNE);
                        curseur++;

                    } else
                        deplacableE = false;
                }
                decal++;
            }
        }
    }

    public void setCasesAtteignablesJoueurCourant(boolean isTourBlanc, byte casePion) {     //utilisee pour vraiment modifier les casesAtteignables courrantes
        //On réinitialise le tableau de case atteignable
        setCasesAtteignablesJoueurCourant(casesAtteignablesJoueurCourant, isTourBlanc, casePion);
    }


    public void setCasesAtteignablesJoueurCourant(byte[] casesAtteignablesJoueurCourant, boolean isTourBlanc, byte casePion) {    //pour tester les cases atteignables d'un pion sans influer sur les elements de la partie
        //On réinitialise le tableau de case atteignable
        for (int i = 0; i < casesAtteignablesJoueurCourant.length; i++)
            casesAtteignablesJoueurCourant[i] = -1;
        int row = casePion / 8, column = casePion % 8;
        int decal, curseur = 0;
        if (isTourBlanc)
            decal = 1;
        else
            decal = -1;
        boolean deplacableNS = true, deplacableO = true, deplacableE = true;

        while (deplacableNS || deplacableO || deplacableE) {
            if (deplacableNS) {
                if (row + decal <= 7
                        && row + decal >= 0
                        && plateau[column + (row + decal) * LIGNE] < 0) {
                    casesAtteignablesJoueurCourant[curseur] = (byte) (column + (row + decal) * LIGNE);
                    curseur++;
                } else
                    deplacableNS = false;
            }
            if (deplacableO) {
                if (row + decal <= 7 && column - decal >= 0
                        && row + decal >= 0 && column - decal <= 7
                        && plateau[(column - decal) + (row + decal) * LIGNE] < 0) {
                    casesAtteignablesJoueurCourant[curseur] = (byte) ((column - decal) + (row + decal) * LIGNE);
                    curseur++;
                } else
                    deplacableO = false;
            }
            if (deplacableE) {
                if (row + decal <= 7 && column + decal <= 7
                        && row + decal >= 0 && column + decal >= 0
                        && plateau[(column + decal) + (row + decal) * LIGNE] < 0) {
                    casesAtteignablesJoueurCourant[curseur] = (byte) ((column + decal) + (row + decal) * LIGNE);
                    curseur++;

                } else
                    deplacableE = false;
            }
            if (isTourBlanc)
                decal++;
            else
                decal--;
        }
    }

    public void simulerCoup(){
        sauvegardeCouleur = couleurPionAJouer;
        sauvegardePlateauPionMem = plateau[casePionMemoire];
        sauvegardeDerPionJoue = dernierPionJoue;
        sauvegardeCaseDerPionJoue = caseDernierPionJoue;
        sauvegardeCasesAtteignablesJoueurCourant = Arrays.copyOf(casesAtteignablesJoueurCourant,
                casesAtteignablesJoueurCourant.length);
        sauvegardeCasePionMemoire = casePionMemoire;
    }

    public void retabliCoup(byte meilleur_coup){
        couleurPionAJouer = plateauCase[casesAtteignablesJoueurCourant[meilleur_coup]];
        plateau[casesAtteignablesJoueurCourant[meilleur_coup]] = pionMemoire;
        plateau[casePionMemoire] = -1;
        caseDestDernierPionJoue = casesAtteignablesJoueurCourant[meilleur_coup];
        caseDernierPionJoue = casePionMemoire;
        dernierPionJoue = pionMemoire;
        tourDuJoueurBlanc = true;
    }

    public int testerCoup(int i){

        int val;
        //simuler(coup actuel)
        sauvegardePlateau1 = plateau[casesAtteignablesJoueurCourant[i]];

        plateau[casePionMemoire] = -1;
        plateau[casesAtteignablesJoueurCourant[i]] = pionMemoire;
        caseDernierPionJoue = casesAtteignablesJoueurCourant[i];
        dernierPionJoue = pionMemoire;
        couleurPionAJouer = plateauCase[casesAtteignablesJoueurCourant[i]];
        pionMemoire = getCouleurPionAJouer();

        for (byte j=0; j< plateau.length; j++)
            if (plateau[j] == pionMemoire)
            {
                casePionMemoire = j;
                break;
            }
        setCasesAtteignablesJoueurCourant(!tourDuJoueurBlanc, getCasePionMemoire());



        val = max(PROFONDEUR_MINMAX, MOINS_INFINI, INFINI);
        /*if(val > maxVal) {
            maxVal = val;
            meilleur_coup = i;
        }*/


        //annuler_coup(coup_actuel)
        couleurPionAJouer = sauvegardeCouleur;
        caseDernierPionJoue = sauvegardeCaseDerPionJoue;
        casePionMemoire = sauvegardeCasePionMemoire;
        plateau[casePionMemoire] = sauvegardePlateauPionMem;
        pionMemoire = dernierPionJoue;
        dernierPionJoue = sauvegardeDerPionJoue;
        casesAtteignablesJoueurCourant = Arrays.copyOf(sauvegardeCasesAtteignablesJoueurCourant,
                sauvegardeCasesAtteignablesJoueurCourant.length);
        plateau[casesAtteignablesJoueurCourant[i]] = sauvegardePlateau1;

        return val;
    }





    private int max(byte profondeur, int alpha, int beta) {         //alpha et beta : pour l'elagage alpha-beta (couper des branches)
        if(caseDernierPionJoue >=56 && caseDernierPionJoue <64  && tourDuJoueurBlanc){    //prodondeur pair = IA vient de jouer
            return eval(DEFAITE_IA);
        }
        else if(caseDernierPionJoue >=0 && caseDernierPionJoue < 8 && !tourDuJoueurBlanc){
            return eval(VICTOIRE_IA);
        }
        else if(profondeur == 0){
            return eval(NI_VICTOIRE_NI_DEFAITE);
        }



        int v;   //nombre de "points" du coup testé

        if(!tourDuJoueurBlanc){   //si tour du joueur blanc (different car valeur changee dans le for)
            v = INFINI;
        }
        else{
            v = MOINS_INFINI;
        }

        byte sauvegardeCouleur = couleurPionAJouer;
        byte sauvegardePlateau1;
        byte sauvegardePlateauPionMem = plateau[casePionMemoire];
        byte sauvegardeDerPionJoue = dernierPionJoue;
        byte[] sauvegardeCasesAtteignablesJoueurCourant = Arrays.copyOf(casesAtteignablesJoueurCourant,
                casesAtteignablesJoueurCourant.length);
        byte sauvegardeCasePionMemoire = casePionMemoire;
        byte sauvegardeCaseDerPionJoue = caseDernierPionJoue;


        if(!controlBlocage()){
            byte i;
            for(i = 0 ; i<casesAtteignablesJoueurCourant.length && casesAtteignablesJoueurCourant[i] !=-1 ; i++) {
                //simuler(coup_actuel);
                tourDuJoueurBlanc = !tourDuJoueurBlanc;     //false si max
                sauvegardePlateau1 = plateau[casesAtteignablesJoueurCourant[i]];


                plateau[casePionMemoire] = -1;
                plateau[casesAtteignablesJoueurCourant[i]] = pionMemoire;
                caseDernierPionJoue = casesAtteignablesJoueurCourant[i];
                dernierPionJoue = pionMemoire;
                couleurPionAJouer = plateauCase[casesAtteignablesJoueurCourant[i]];

                byte ajoutPionMemoire = (byte)((tourDuJoueurBlanc)?8:0);
                pionMemoire = (byte)(ajoutPionMemoire + getCouleurPionAJouer());


                for (byte j=0; j< plateau.length; j++)
                    if (plateau[j] == pionMemoire)
                    {
                        casePionMemoire = j;
                        break;
                    }
                setCasesAtteignablesJoueurCourant(!tourDuJoueurBlanc, getCasePionMemoire());



                if(!tourDuJoueurBlanc){     //IA
                    v = Math.max(v, max((byte)(profondeur - 1), alpha, beta));
                    alpha = Math.max(alpha, v);
                }
                else{
                    v = Math.min(v, max((byte)(profondeur - 1), alpha, beta));
                    beta = Math.min(beta, v);
                }



                //annuler_coup(coup_actuel);
                couleurPionAJouer = sauvegardeCouleur;
                casePionMemoire = sauvegardeCasePionMemoire;
                caseDernierPionJoue = sauvegardeCaseDerPionJoue;
                plateau[casePionMemoire] = sauvegardePlateauPionMem;
                pionMemoire = dernierPionJoue;
                dernierPionJoue = sauvegardeDerPionJoue;
                casesAtteignablesJoueurCourant = Arrays.copyOf(sauvegardeCasesAtteignablesJoueurCourant,
                        sauvegardeCasesAtteignablesJoueurCourant.length);
                plateau[casesAtteignablesJoueurCourant[i]] = sauvegardePlateau1;
                tourDuJoueurBlanc = !tourDuJoueurBlanc;       //ou true

                if(beta<=alpha){
                    break;   //cut beta (tour de l'IA) ou alpha (tour du joueur)
                }
            }
        }
        else{    //blocage

            boolean condition;

            tourDuJoueurBlanc = !tourDuJoueurBlanc;
            couleurPionAJouer = getPlateauCase()[getCasePionMemoire()];

            // On retrouve le pion qui doit jouer et on le met dans le pion mémoire
            for (int i = 0; i < plateau.length; i++) {
                condition = (tourDuJoueurBlanc)
                        ? plateau[i] != -1 && plateau[i] % 8 == getCouleurPionAJouer() % 8 && plateau[i] > 7
                        : plateau[i] != -1 && plateau[i] == getCouleurPionAJouer();

                if (condition) {
                    pionMemoire = plateau[i];     //ca a reverse
                    casePionMemoire = (byte)i;
                }
            }

            // On calcul les cases atteignables du pion mémoire
            setCasesAtteignablesJoueurCourant(!tourDuJoueurBlanc, getCasePionMemoire());


            v = max((byte)(profondeur - 1), alpha, beta);



            couleurPionAJouer = sauvegardeCouleur;
            casePionMemoire = sauvegardeCasePionMemoire;
            pionMemoire = dernierPionJoue;

            casesAtteignablesJoueurCourant = Arrays.copyOf(sauvegardeCasesAtteignablesJoueurCourant,
                    sauvegardeCasesAtteignablesJoueurCourant.length);
            tourDuJoueurBlanc = !tourDuJoueurBlanc;       //ou true
        }

        return v;
    }

    private int eval(byte situation){
        int pts;

        if(situation == VICTOIRE_IA){
            pts = PTS_VICTOIRE_IA;
        }
        else if(situation == DEFAITE_IA){
            pts = PTS_DEFAITE_IA;
        }
        else{    //situation = NI_VICTOIRE_NI_DEFAITE
            pts = evalFinProfondeur();
        }

        return pts;
    }

    private int evalFinProfondeur() {
        int pts = 0;

        pts += evalFinDeplacementEtGagnerDeXFacon();
        //a completer

        return pts;
    }

    private int evalFinDeplacementEtGagnerDeXFacon() {
        byte[] casesAtteignables = new byte[NBCASESATTEIGNABLESPOSSIBLESJOUEURCOURANT];
        byte idCase = 0;

        byte compteurAtteintFinPartie;
        byte compteurDeplacements_IA = 0;
        byte pts = 0;

        for(byte pion : plateau){
            idCase++;
            compteurAtteintFinPartie = 0;
            if(pion >=8){                   //IA
                setCasesAtteignablesJoueurCourant(casesAtteignables, false, idCase);

                for(byte j = 0 ; j<casesAtteignables.length && casesAtteignables[j] != -1; j++){
                    compteurDeplacements_IA++;
                    if(casesAtteignables[j] >=0 && casesAtteignables[j]<8){
                        compteurAtteintFinPartie++;
                    }
                }

                switch (compteurAtteintFinPartie){
                    case 1:
                        pts += PEUT_ATTEINDRE_UNE_CASE_AU_BOUT_DU_PLATEAU;
                        break;
                    case 2:
                        pts += PEUT_ATTEINDRE_DEUX_CASES_AU_BOUT_DU_PLATEAU;
                        break;
                    case 3:
                        pts += PEUT_ATTEINDRE_TROIS_CASES_AU_BOUT_DU_PLATEAU;
                        break;
                }
            }
            else if(pion <8 && pion != -1){                   //joueur
                setCasesAtteignablesJoueurCourant(casesAtteignables, true, idCase);

                for(byte j = 0 ; j<casesAtteignables.length && casesAtteignables[j] != -1; j++){
                    compteurDeplacements_IA++;
                    if(casesAtteignables[j] >=56 && casesAtteignables[j]<64){
                        compteurAtteintFinPartie++;
                    }
                }

                switch (compteurAtteintFinPartie){
                    case 1:
                        pts -= PEUT_ATTEINDRE_UNE_CASE_AU_BOUT_DU_PLATEAU;
                        break;
                    case 2:
                        pts -= PEUT_ATTEINDRE_DEUX_CASES_AU_BOUT_DU_PLATEAU;
                        break;
                    case 3:
                        pts -= PEUT_ATTEINDRE_TROIS_CASES_AU_BOUT_DU_PLATEAU;
                        break;
                }
            }
        }

        pts += compteurDeplacements_IA;

        return pts;
    }






    void deplacerPiece(byte caseArrivee) {
        // On indique que le pion est maintenant sur la case cliqué
        plateau[caseArrivee] = pionMemoire;
        // On supprime le pion de son ancien emplacement
        plateau[casePionMemoire] = -1;
        // On enregistre le pion qui vient d'être bougé
        dernierPionJoue = pionMemoire;
    }

    boolean controlBlocage() {
        return casesAtteignablesJoueurCourant[0] == -1;
    }

    byte[] getPlateau() {
        return plateau;
    }

    byte[] getPlateauCase() {
        return plateauCase;
    }

    boolean isTourUn() {
        return isTourUn;
    }

    void setTourUn(boolean tourUn) {
        isTourUn = tourUn;
    }

    boolean isTourDuJoueurBlanc() {
        return tourDuJoueurBlanc;
    }

    void setTourDuJoueurBlanc(boolean tourDuJoueurBlanc) {
        this.tourDuJoueurBlanc = tourDuJoueurBlanc;
    }

    byte getPionMemoire() {
        return pionMemoire;
    }

    void setPionMemoire(byte pionMemoire) {
        this.pionMemoire = pionMemoire;
    }

    byte getDernierPionJoue() {
        return dernierPionJoue;
    }

    byte[] getCasesAtteignablesJoueurCourant() {
        return casesAtteignablesJoueurCourant;
    }

    byte[][] getCasesAtteignablesTourUn() {
        return casesAtteignablesTourUn;
    }

    void setCasePionMemoire(byte casePionMemoire) {
        this.casePionMemoire = casePionMemoire;
    }

    byte getCasePionMemoire() {
        return casePionMemoire;
    }

    byte getCouleurPionAJouer() {
        return couleurPionAJouer;
    }

    void setCouleurPionAJouer(byte couleurPionAJouer) {
        this.couleurPionAJouer = couleurPionAJouer;
    }

    void setCasesAtteignablesJoueurCourant(byte[] casesAtteignablesJoueurCourant) {
        this.casesAtteignablesJoueurCourant = casesAtteignablesJoueurCourant;
    }

    static byte getNBCASESATTEIGNABLESPOSSIBLESJOUEURCOURANT() {
        return NBCASESATTEIGNABLESPOSSIBLESJOUEURCOURANT;
    }

    public boolean isEstGagnee() {
        return estGagnee;
    }

    public void setEstGagnee(boolean estGagnee) {
        this.estGagnee = estGagnee;
    }

    public byte getCaseDernierPionJoue() {
        return caseDernierPionJoue;
    }

    public byte getCaseDestDernierPionJoue() {
        return caseDestDernierPionJoue;
    }
}
