import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 Created by cladlink on 24/09/16.
 */
public class Model_Pion_Test
{
    /**
     * comment please
     */
    @Ignore
    @Test
    public void testSetCaseActuelle()
    {
        Model_Case caseAuPif = Mockito.mock(Model_Case.class);
        Model_Case caseAuPifDeux = Mockito.mock(Model_Case.class);
        Model_Pion pion = new Model_Pion(caseAuPif, true, Couleur.BLEU);
        pion.setCaseActuelle(caseAuPifDeux);
        Assert.assertEquals(pion.getCaseActuelle(), caseAuPifDeux);
    }
}
