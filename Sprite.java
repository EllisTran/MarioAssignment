import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.ArrayList;

abstract class Sprite
{
    //Declaring Member Variables
    int x;              //X position of the Sprite
    int y;              //Y position of the Sprite
    int w;              //Width of the Sprite
    int h;              //Height of the Sprite
    double vert_vel;    //The Vertical velocity of the sprite
    String category;    //Allows for the json implementation of objects
    Model model;        //Allows for the use of the model class

    //Abstract Methods to be called in every single class that extends Sprite
    abstract boolean checkBrick();      //In each class that extends Sprite checks to see if that object is a brick
    abstract boolean checkMario();      //In each class that extends Sprite checks to see if that object is a Mario
    abstract boolean checkCoinBlock();  //In each class that extends Sprite checks to see if that object is a CoinBlock
    abstract void draw(Graphics g);     //In each class that extends Sprite draws the images for each Sprite
    abstract void load();               //In each class that extends Sprite Lazy loads the images
    abstract void update();             //In each class that extends Sprite updates each object
    abstract Sprite cloneMe(Model m);   //In each class that extends Sprite allows the sprite to be cloned

    //Constructor
    Sprite() {}

    //Copy Constructor
    Sprite(Sprite copy, Model m)
    {
        //Initializes the Model that was passed in
        model = m;

        //Deep Copy
        this.x = copy.x;
        this.y = copy.y;
        this.w = copy.w;
        this.h = copy.h;
    }

     //Creates JSON object that adds the member variables to the JSON object
    Json marshall()
    {
        Json ob = Json.newObject();             //Creates new Object
        ob.add("x", x);             
        ob.add("y", y);
        ob.add("w", w);
        ob.add("h", h);
        ob.add("Category", "Sprites");
        return ob;
    }

    //Method to check collision
    boolean checkCollision(Model m, Mario mario, Sprite sprite)
	{
        if (mario.x + mario.w < sprite.x)       //Checks to see if Mario is colliding with the left side of the sprite
            return false;
        if (mario.x > sprite.x + sprite.w)      //Checks to see if Mario is colliding with the right side of the sprite
            return false;
        if (mario.y + mario.h < sprite.y)       //Checks to see if Mario is colliding with the top of the sprite
            return false;
        if (mario.y > sprite.y + sprite.h)      //Checks to see if Mario is colliding with the bottom of the sprite
            return false;
        collisionHandler(m, mario, sprite);     //Calls Collision method if collision occurs
        return true;
        
    }

    //Method to handle the collision
	void collisionHandler(Model m, Mario mario, Sprite sprite)
    {
        if (mario.x + mario.w >= sprite.x && mario.prev_X +mario.w <= sprite.x)                  //Left Side Collision
        {
            mario.x = sprite.x - mario.w - 3;
        }
        else if (mario.x <= sprite.x + sprite.w && mario.prev_X >= sprite.x + sprite.w)          //Right Side Collision
        {
            mario.x = sprite.x + sprite.w + 3;
        }
        else if (mario.y + mario.h > sprite.y && mario.prev_Y + mario.h <= sprite.y + sprite.h)  //Top Collision
        {
            mario.y = sprite.y - mario.h;
            mario.jumpFrame = 0;
            mario.vert_vel = 2.1;
            mario.coinPop = 0;
        }
        else if (mario.y < sprite.y + sprite.h && mario.prev_Y >= sprite.h)                      //Under Brick
        {
            mario.y = sprite.y + sprite.h;
            mario.vert_vel = 0.0;
            if (sprite.checkCoinBlock() && mario.coinPop == 0)                                   //If it hits a coinBlock do certain things
            {         
                mario.coinPop++;                                                                 //Allows for only 1 coin to pop out
                CoinBlock cb = (CoinBlock)sprite;                                                //Casts the sprite to be a CoinBlock
                if(cb.coinLimit< 5)                                                              //If the coins are greater than 5 in that block do not add more coins
                {
                    cb.addCoins(m);
                }
                 
            } 
        }

    }

}