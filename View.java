import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.JButton;
import java.awt.Image;
class View extends JPanel
{
    //Initializes member variables
    Model model;

    //LAZY LOADING!!!
    static Image backgroundImage = null;
    static Image floorImage = null;

    //View Constructor Method
    View(Controller c, Model m)
    {
        c.setView(this);
        model = m;
        if (floorImage == null && backgroundImage ==null)
        {
            try
            {
                floorImage = ImageIO.read(new File("dirtGround.png"));
                backgroundImage = ImageIO.read(new File("Background.png"));
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }

    //Removes Button
    void removeButton()
    {
    }

    //Creates the Background
    public void paintComponent(Graphics g) 
    {
       // g.setColor(new Color(0, 0, 0));
        //g.fillRect(0, 1000, 1800, this.getHeight());

        //Draws the Background image to work with the Camera Position and wrapping 
        for (int i = 0; i < 5; i++)
        {
            g.drawImage(backgroundImage, (i*1800) - (model.camPos / 10),-200, null);
            g.drawImage(floorImage, (i*1800) - (model.camPos /5), 596, 2000, 596, null);
        }

         //Draws the ground using the floor .png

        //Loads and draws the images int the sprite class
        for (int i = 0; i <model.sprites.size(); i++)
        {
            Sprite s = model.sprites.get(i);
            s.load();
            s.draw(g);
        }
    }
}

