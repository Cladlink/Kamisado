import java.util.Locale;

class Kamisado
{
    public static void main (String[] args)
    {
        Locale locale = new Locale("fr");
        Locale.setDefault(locale);

        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Control_Group controler = new Control_Group();
                //Music.playMusicTest();
            }
        });
    }
}
