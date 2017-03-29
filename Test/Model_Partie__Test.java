import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class Model_Partie__Test
{

    /**
     * Teste la methode controleBlocage() (qui se deroule comme si on est au premier tour)
     * Ce test verifie que si le prochain pion qui doit jouer a des cases atteignables,
     *      on ne rentre pas dans le premier if de la methode
     */
    @Test
    public void testControleBlocageSiPresenceCasesAtteignablesTour1()
    {
        //on a besoin d'une seule case pour ce test : la case où se trouve le pion qui va jouer
        Model_Case[] cases = new Model_Case[1];
        cases[0] = new Model_Case(0,0, Couleur.VIOLET);

        Model_Pion[] pionsBlancs = new Model_Pion[1];
        Model_Pion[] pionsNoirs = new Model_Pion[1];
        pionsBlancs[0] = Mockito.mock(Model_Pion.class);

        //pour que la mise à jour des cases atteignables du pion ne soit pas effectuee
        //on n'a pas besoin de cette maj, car je fixe la valeur de retour de getCasesAtteignables dans le mock (juste en dessous)
        Mockito.doNothing().when(pionsBlancs[0]).casesAtteignables();

        ArrayList<Model_Case> array = new ArrayList<Model_Case>();
        array.add(cases[0]);
        Mockito.when(pionsBlancs[0].getCasesAtteignables()).thenReturn(array);    //le pion a une case atteignable


        Model_Accueil accueil = new Model_Accueil();
        Model_Joueur joueurBlanc = new Model_Joueur("blanc", true, 1);
        Model_Joueur joueurNoir = new Model_Joueur("noir", false, 2);
        Model_Partie partie = Model_Partie.factPartie(accueil, joueurBlanc, joueurNoir, cases, pionsBlancs, pionsNoirs, pionsBlancs[0], true);
        //le true en dernier parametre indique qu'on est au premier tour

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);


        //c'est au tour du joueur blanc (car affecté dans le constructeur de partie)
        partie.controleBlocage();
        assertTrue(partie.isTourDuJoueurBlanc());    //s'il n'y a pas de blocage, on permet au joueur blanc de jouer
        assertFalse(partie.estGagnee());          //il ne doit pas y  avoir de blocage empechant la partie de se poursuivre

    }

    /**
     * Teste la methode controleBlocage() (qui se deroule comme si ON N'EST PAS au premier tour)
     * Ce test verifie que si le prochain pion qui doit jouer a des cases atteignables,
     *      on ne rentre pas dans le premier if de la methode
     */
    @Test
    public void testControleBlocageSiPresenceCasesAtteignablesPasTour1()
    {
        //fait quasi la meme chose que le test precedent

        Model_Case[] cases = new Model_Case[1];
        cases[0] = new Model_Case(0,0, Couleur.VIOLET);

        Model_Pion[] pionsBlancs = new Model_Pion[1];
        Model_Pion[] pionsNoirs = new Model_Pion[1];

        pionsBlancs[0] = Mockito.mock(Model_Pion.class);
        Mockito.doNothing().when(pionsBlancs[0]).casesAtteignables();

        ArrayList<Model_Case> array = new ArrayList<Model_Case>();
        array.add(cases[0]);
        Mockito.when(pionsBlancs[0].getCasesAtteignables()).thenReturn(array);


        Model_Accueil accueil = new Model_Accueil();
        Model_Joueur joueurBlanc = new Model_Joueur("blanc", true, 1);
        Model_Joueur joueurNoir = new Model_Joueur("noir", false, 2);
        Model_Partie partie = Model_Partie.factPartie(accueil, joueurBlanc, joueurNoir, cases, pionsBlancs, pionsNoirs, pionsBlancs[0], false);
        //le false en dernier parametre indique qu'ON N'EST PAS au premier tour

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);


        partie.controleBlocage();
        assertTrue(partie.isTourDuJoueurBlanc());
        assertFalse(partie.estGagnee());

    }

    /**
     * Teste la methode controleBlocage() (qui se deroule comme si ON N'EST PAS au premier tour)
     * Ce test verifie que si un bloquage empeche la partie de continuer (ex :un pion bloque force un pion bloque
     *      à jouer qui reforce le pion d'origine à jouer), alors le perdant est celui qui a joué le coup bloquant
     */
    @Test
    public void testControleBlocageSiBlocageMettantFinAPartie()
    {
        //on a besoin de deux cases pour ce test
        Model_Case[] cases = new Model_Case[2];
        cases[0] = new Model_Case(0,0, Couleur.VIOLET);
        cases[1] = new Model_Case(0,1, Couleur.BLEU);

        Model_Pion[] pionsBlancs = new Model_Pion[1];
        Model_Pion[] pionsNoirs = new Model_Pion[1];


        pionsBlancs[0] = Mockito.mock(Model_Pion.class);
        Mockito.doNothing().when(pionsBlancs[0]).casesAtteignables();     //on fixera nous meme les casesAtteignables des pions via un mock
        pionsNoirs[0] = Mockito.mock(Model_Pion.class);
        Mockito.doNothing().when(pionsNoirs[0]).casesAtteignables();



        ArrayList<Model_Case> array = new ArrayList<Model_Case>();
        Mockito.when(pionsBlancs[0].getCasesAtteignables()).thenReturn(array);    //array vide = aucune case atteignables
        Mockito.when(pionsBlancs[0].getCaseActuelle()).thenReturn(cases[1]);     //case 1 = case bleu = force pion bleu adverse à jouer
        Mockito.when(pionsBlancs[0].getCOULEUR()).thenReturn(Couleur.VIOLET);


        Mockito.when(pionsNoirs[0].getCasesAtteignables()).thenReturn(array);   //array vide = aucune case atteignables
        Mockito.when(pionsNoirs[0].getCaseActuelle()).thenReturn(cases[0]);    //case 0 = case violette = force pion violet adverse à jouer
        Mockito.when(pionsNoirs[0].getCOULEUR()).thenReturn(Couleur.BLEU);


        Model_Accueil accueil = new Model_Accueil();
        Model_Joueur joueurBlanc = new Model_Joueur("blanc", true, 1);
        Model_Joueur joueurNoir = new Model_Joueur("noir", false, 2);
        Model_Partie partie = Model_Partie.factPartie(accueil, joueurBlanc, joueurNoir, cases, pionsBlancs, pionsNoirs, pionsBlancs[0], false);
        //on est pas au premier tour, car ce genre de cas ne se rencontre pas au premier tour

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);


        assertFalse(partie.estGagnee());     //pour l'instant, personne n'a gagné

        //c'est au tour du joueur blanc (car affecté dans le constructeur de partie)
        partie.controleBlocage();
        assertTrue(partie.isTourDuJoueurBlanc());      //la methode change deux fois de joueur (car deux blocages rencontres)

        assertTrue(partie.estGagnee());            //la partie est gagnee par un joueur
        assertTrue(partie.isJoueurBlancGagnant());  //joueur blanc gagne, car joueur noir a provoque le blocage
    }

    /**
     * Teste la methode controleBlocage() (qui se deroule comme si ON N'EST PAS au premier tour)
     * Ce test verifie que si il y a un seul blocage, la methode change le joueur actif une fois
     */
    @Test
    public void testControleBlocageAvecSeulementUnBlocage()
    {
        Model_Case[] cases = new Model_Case[2];
        cases[0] = new Model_Case(0, 0, Couleur.VIOLET);
        cases[1] = new Model_Case(0, 1, Couleur.BLEU);

        Model_Pion[] pionsBlancs = new Model_Pion[1];
        Model_Pion[] pionsNoirs = new Model_Pion[1];


        pionsBlancs[0] = Mockito.mock(Model_Pion.class);
        Mockito.doNothing().when(pionsBlancs[0]).casesAtteignables();
        pionsNoirs[0] = Mockito.mock(Model_Pion.class);
        Mockito.doNothing().when(pionsNoirs[0]).casesAtteignables();

        ArrayList<Model_Case> array = new ArrayList<Model_Case>();
        Mockito.when(pionsBlancs[0].getCasesAtteignables()).thenReturn(array);  //le pion blanc n'a aucune case atteignable, et est bloque
        Mockito.when(pionsBlancs[0].getCaseActuelle()).thenReturn(cases[1]);
        Mockito.when(pionsBlancs[0].getCOULEUR()).thenReturn(Couleur.VIOLET);

        ArrayList<Model_Case> array2 = new ArrayList<Model_Case>();
        array2.add(cases[1]);                                   //le pion noir a une case atteignable : il n'est pas bloque
        Mockito.when(pionsNoirs[0].getCasesAtteignables()).thenReturn(array2);
        Mockito.when(pionsNoirs[0].getCOULEUR()).thenReturn(Couleur.BLEU);
        Mockito.when(pionsNoirs[0].getCaseActuelle()).thenReturn(cases[0]);


        Model_Accueil accueil = new Model_Accueil();
        Model_Joueur joueurBlanc = new Model_Joueur("blanc", true, 1);
        Model_Joueur joueurNoir = new Model_Joueur("noir", false, 2);
        Model_Partie partie = Model_Partie.factPartie(accueil, joueurBlanc, joueurNoir, cases, pionsBlancs, pionsNoirs, pionsBlancs[0], false);

        joueurBlanc.setPartie(partie);
        joueurNoir.setPartie(partie);


        assertFalse(partie.estGagnee());

        partie.controleBlocage();
        assertTrue(!partie.isTourDuJoueurBlanc());    //on est passe au tour du joueur noir (car le blanc est bloque)
        assertFalse(partie.estGagnee());            //personne n'a gagne pour l'instant, il y a juste le blanc qui passe son tour
    }
}