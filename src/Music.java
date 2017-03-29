import javafx.embed.swing.JFXPanel;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by ndolo on 12/01/17.
 */
public abstract class Music {
    /**
     * Essai 1 InputStream
     */
        /*/*//** add this into your application code as appropriate
     // Open an input stream  to the audio file.
     InputStream in = new FileInputStream("Music/songTest.mp3");
     // Create an AudioStream object from the input stream.
     AudioStream as = new AudioStream(in);
     // Use the static class member "player" from class AudioPlayer to play clip.
     AudioPlayer.player.start(as);
     // Similarly, to stop the audio.
     AudioPlayer.player.stop(as);*/

    /**
     * Essai 2 Media & MediaPlayer
     */
        /*try {
            File f = new File("Music/songTest.mp3");
            Media hit = new Media(f.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception: " + ex.getMessage());
        }*/

    private static MediaPlayer mp;
    private static final String musicTest = "Music/zen.mp3";

    /**
     * playMedievalTheme
     * démarre la musique du thème médiéval
     */
    static void playMusicTest()
    {
        JFXPanel jfxPanel = new JFXPanel();
        mp = new MediaPlayer(new Media(Paths.get(musicTest).toUri().toString()));
        mp.play();
        mp.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * stopMusicTest
     * arrete le thème médieval
     */
    static void stopMusicTest()
    {
        if (mp != null) mp.stop();
    }
}

