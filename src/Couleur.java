class Couleur
{
    static final int MARRON = 0;
    static final int VERT = 1;
    static final int ROUGE = 2;
    static final int JAUNE = 3;

    static final int ROSE = 4;
    static final int VIOLET = 5;
    static final int BLEU = 6;
    static final int ORANGE = 7;



    public static String couleurToString(int couleur)
    {
        String nomCouleur = null;

        switch(couleur){
            case MARRON:
                nomCouleur = "Marron";
                break;
            case VERT:
                nomCouleur = "Vert";
                break;
            case ROUGE:
                nomCouleur = "Rouge";
                break;
            case JAUNE:
                nomCouleur = "Jaune";
                break;
            case ROSE:
                nomCouleur = "Rose";
                break;
            case VIOLET:
                nomCouleur = "Violet";
                break;
            case BLEU:
                nomCouleur = "Bleu";
                break;
            case ORANGE:
                nomCouleur = "Orange";
                break;
        }

        return nomCouleur;
    }

    static String[] getListeStringCouleurs()
    {
        String[] tabStringCouleur = {"Marron", "Vert", "Rouge", "Jaune", "Rose", "Violet", "Bleu", "Orange"};
        return tabStringCouleur;
    }
}
