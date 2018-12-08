import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.Random;
class Coin extends Sprite
{
    //Declaring member variables
    Model model;
    int moveCoinX;                  //Allows for the random spawning of the coin's X position

    static Image coin = null;       //Lazy Loading for coin

    //Constructor
    Coin(Model m, int x2, int y2)
    {           
        model = m;                          //Initializes Model
        Random rand = new Random();         //Initializes random
        moveCoinX = rand.nextInt(21) - 10;  //Moves the coin to random x positions
        x = x2;
        y = y2;
        this.w = 75;                        //Sets the width
        this.h = 75;                        //Sets the height
        this.vert_vel = -20.0;              //Sets the vertical velocity of the coins to go up and down
    }

    //Coin copy constructor
    Coin(Coin copyCoin, Model copyModel)
    {
        super(copyCoin,copyModel);          //Calls super constructor to deep copy
        model = copyModel;                  //Initializes model
        //Deep Copy
        this.vert_vel = copyCoin.vert_vel;
        this.moveCoinX = copyCoin.moveCoinX;
    }

    //Method to clone coin to deep copy
    Coin cloneMe(Model m)
    {
        return new Coin(this, m);
    }

    //Copy Constructor for Unmarshalling
    Coin(Json ob, Model m)
    {
        model = m;                       //Initializes model
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
        ob.add("Category", "Coin");
        return ob;
    }

    boolean checkBrick()
    {
        return false;
    }
    boolean checkMario()
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
        g.drawImage(coin, x - model.camPos, y, w , h, null);
    }

    //Lazy Loading Method
    void load()
    {
        if (coin == null)
        {
            try
            {
                coin = ImageIO.read(new File("coin.png"));
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
        if (y<500)          //If the coin is above the ground makes it to were it goes up then down
        {
            //Adding to this makes it go up
            this.vert_vel +=1.2;
            y+=this.vert_vel;

            //This makes it goe down
            this.vert_vel+= 2.1;
            y+=this.vert_vel;

            x+= moveCoinX;  //This makes the coin go left or right depending on its random number between -10 to 10
        }

        //If the coin hits the ground then remove the coin
        if (this.y >= 500) {
            this.y = 500;
            model.removeCoin(this);
        }
    }
}