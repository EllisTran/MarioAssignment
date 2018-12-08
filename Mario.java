import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Mario extends Sprite
{
    //Declaring Member Variables   
    int cycle;                      //Cycle is to cycle through the images in the array for left and right
    int switching = -1;             //Switching is for switching between the left array and the right array for the cycle of mario Images
    int jumpFrame;                  //Counts the frames of how long Mario can jump for
    int coinPop;                    //CoinPop allows for only one coin to pop out of the the coinBlock
    int prev_X;                     //Previous X position of Mario
    int prev_Y;                     //Previous Y position of Mario                 

    static Image mario_imagesRight[] = null;        //Sets to null for Lazy loading of images
    static Image mario_imagesLeft[] = null;         //Sets to null for lazy loading of images

    //Constructor
    Mario(Model m) 
	{
        load();
        //Initializes Variables
        jumpFrame = 0;
        this.h = 95;
        this.w = 60;
        this.model = m;     //Instiantiate Model object
    }

    //Copy Constructor
    Mario(Mario copyMario, Model modelCopy)
    {
        //Deep Copy
        super(copyMario, modelCopy);        //Calls super constructor to do further deep copy
        this.vert_vel = copyMario.vert_vel;
        this.cycle = copyMario.cycle;
        this.switching = copyMario.switching;
        this.jumpFrame = copyMario.jumpFrame;
        this.coinPop = copyMario.coinPop;
        this.prev_X = copyMario.prev_X;
        this.prev_Y = copyMario.prev_Y;
    }

    //Unmarshall Method
    Mario(Json ob, Model model)
    {
        load();
        this.model = model;

        //Sets value to whatever the json file says the values are
        this.x = (int)ob.getLong("x");
        this.y = (int)ob.getLong("y");
        w = (int)ob.getLong("w");
        h = (int)ob.getLong("h");
        category = ob.getString("Category");
    }


    //Creates JSON object that adds the member variables to the JSON object
    Json marshall()
    {
        Json ob = Json.newObject();     //Creates new Json object
        ob.add("Category", "Mario");    //Adds Mario Object
        ob.add("x", x);                 //Adds the X value
        ob.add("y", y);                 //Adds the Y value
        ob.add("w", w);                 //Adds the Width value
        ob.add("h", h);                 //Adds the Height value
        return ob;
    }

    //Method to clone mario to be deep copied
    Mario cloneMe(Model model)
    {
        return new Mario(this, model);
    }
    
    //Method to record Mario's previous destination
	void prevdestination()
    {
        prev_X = x;
        prev_Y = y;
    }

    boolean checkMario()
    {
        return true;
    }

    boolean checkBrick()
    {
        return false;
    }

    boolean checkCoinBlock()
    {
        return false;
    }

    //Draw Method
    void draw(Graphics g)
    {
        //Creates the images to cycle through
        if (switching == -1)
        {
            g.drawImage(mario_imagesRight[cycle], x - model.camPos, y, null);
        }
        if (model.marioRight)
        {
            g.drawImage(mario_imagesRight[cycle], x - model.camPos, y, null);
            cycle++;
            if (cycle == 4)
                cycle = 0;
            switching = 0;
        }
        else if (model.marioLeft)
        {
            g.drawImage(mario_imagesLeft[cycle], x - model.camPos, y, null);
            cycle++;
            if (cycle == 4)
                cycle = 0;
            switching = 1;
        }
        else if (switching == 0)
            g.drawImage(mario_imagesRight[cycle], x - model.camPos, y, null);
        else if (switching == 1)
            g.drawImage(mario_imagesLeft[cycle], x - model.camPos, y, null);
    }

     //Method to lazy load the images
    void load()
    {
        if (mario_imagesRight == null && mario_imagesLeft == null) {
            mario_imagesRight = new Image[5];
            mario_imagesLeft = new Image[5];
            try 
            {
                mario_imagesRight[0] = ImageIO.read(new File("mariofrontwards1.png"));
                mario_imagesRight[1] = ImageIO.read(new File("mariofrontwards2.png"));
                mario_imagesRight[2] = ImageIO.read(new File("mariofrontwards3.png"));
                mario_imagesRight[3] = ImageIO.read(new File("mariofrontwards4.png"));
                mario_imagesRight[4] = ImageIO.read(new File("mariofrontwards5.png"));
                mario_imagesLeft[0] = ImageIO.read(new File("mariobackwards1.png"));
                mario_imagesLeft[1] = ImageIO.read(new File("mariobackwards2.png"));
                mario_imagesLeft[2] = ImageIO.read(new File("mariobackwards3.png"));
                mario_imagesLeft[3] = ImageIO.read(new File("mariobackwards4.png"));
                mario_imagesLeft[4] = ImageIO.read(new File("mariobackwards5.png"));
            } 
            catch (Exception e) 
            {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }

    //Update Method
    void update() 
	{
        model.camPos = x - 200;     //Allows camera position scrolling according to mario's x position
		
        //Adds Gravity
        if (y < 500) {
            vert_vel += 2.1;
            y += vert_vel;
        }

        //Stops Mario when he hits ground
        if (y >= 500) {
            jumpFrame = 0;
            y = 500;
            vert_vel = 0.0;
            coinPop = 0;
        }
        jumpFrame++;                //Updates Mario's frame
    }

}
