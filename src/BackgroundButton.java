import javax.swing.*;
import java.awt.*;

/**
 * Created by Flo on 12/01/2017.
 */
public class BackgroundButton extends JButton{
    private int width;
    private int height;
    private Image image;

    public BackgroundButton(int width, int height, String nomImage){
        super();
        this.image = new ImageIcon(nomImage).getImage();

        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
    }

    public void paintComponent(Graphics g){
        //super.paintComponent(g);
        g.drawImage(image, 0, 0, width, height, null);
    }
}
