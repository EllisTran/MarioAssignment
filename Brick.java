import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Brick extends Sprite
{
    //Initialize brickImage to null for Lazy Loading
    static Image brickImage = null;
    Model model;

    //Constructor
    Brick(int hor, int vert, int width, int height, Model m)
    {
        //Initialize Member Variables
        model = m;
        x = hor;
        y = vert;
        w = width;
        h = height;
    }

    //Copy constructor
    Brick(Brick brickCopy, Model modelCopy)
    {
        //Deep copy
        super(brickCopy, modelCopy);
        model = modelCopy;
    }

    //Method to clone Brick for deep copy
    Brick cloneMe(Model m)
    {
        return new Brick(this, m);
    }

    //Constructor for unmarshalling
    Brick(Json ob, Model m)
    {
        model = m;
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = (int)ob.getLong("w");
        h = (int)ob.getLong("h");
        category = ob.getString("Category");
    }

    //Creates JSON object that adds the member variables to the JSON object
    Json marshall()
    {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        ob.add("Category", "Brick");
        return ob;
    }


    boolean checkMario()
    {
        return false;
    }

    boolean checkBrick()
    {
        return true;
    }

    boolean checkCoinBlock()
    {
        return false;
    }

    //Load Method
    void load()
    {
        if (brickImage == null)
        {
            try
            {
            brickImage = ImageIO.read(new File("dirtGround.png"));
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }

    //Draw Method
    void draw(Graphics g)
    {
        g.drawImage(brickImage, x- model.camPos, y, w, h, null);        //Draws the floor image to work with the scrolling too.    
    }

    //Update Method
    void update() {}
}