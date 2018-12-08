import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.Random;
import java.util.ArrayList;

class CoinBlock extends Sprite
{
    //Declaring Member Variables
    int coinLimit;                              //Does the limit for how many coins in each block
    static Image emptyCoinBlock = null;         //Sets up the lazy Loading for the empty coinblock
    static Image coinBlock = null;              //Sets up the lazy loading for the coinBlock
    Model model;

    //Constructor
    CoinBlock(int x, int y, Model m)
    {
        //Initialize Member Variables
        model = m;
        this.x = x;
        this.y = y;
        this.w = 89;
        this.h = 83;
    }

    //Copy Constructor
    CoinBlock(CoinBlock cbCopy, Model modelCopy)
    {
        //Super constructor and others for deep copy
        super(cbCopy,modelCopy);
        model = modelCopy;
        this.coinLimit = cbCopy.coinLimit;
    }   

    //Method to clone CoinBlock for deep copy
    CoinBlock cloneMe(Model m)
    {  
        return new CoinBlock(this, m);
    }

    //Constructor for Unmarshalling
    CoinBlock(Json ob, Model m)
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
        ob.add("Category", "CoinBlock");
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
        return true;
    }

    //Load Method
    void load()
    {
        if (coinBlock == null && emptyCoinBlock == null)
        {
            try
            {
                coinBlock = ImageIO.read(new File("coinblock1.png"));
                emptyCoinBlock = ImageIO.read(new File("emptycoinblock.png"));
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
        if (this.coinLimit < 5)
            g.drawImage(coinBlock, x - model.camPos, y, w , h, null);
        else
            g.drawImage(emptyCoinBlock, x - model.camPos, y, w , h ,null);

    }

    //addCoins Mathod
    void addCoins(Model m)
    {
        if(coinLimit < 5)
        {
            coinLimit++;                                    //Adds the coinLimit
            m.coinNum++;                                    // Adds coin to number of coins to keep track of it in the AI part
            Coin coin = new Coin(m, this.x, this.y-50);     // Add coin here to instantiate a new coin
            m.sprites.add(coin);                            // Add coin to sprites
        }
    }
    
    //Update Method
    void update()
    {

    }
}