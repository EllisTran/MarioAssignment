import java.util.ArrayList;

class Model
{
    ArrayList<Sprite> sprites;  //Makes an array list of type Sprite

    //Member Variables
    int camPos;                     //Gives the camera position of the game (should be fixed based on Mario's position)
    int coinNum;                    //Counts the number of coins there are. coinNum++ whenever a new coin is added
    int marioisJumpingCounter;      //Counts the number of times mario has jumped
    boolean marioRight = false;     //If true, the Mario images will change making him look like he's moving to the right
    boolean marioLeft = false;      //If true, the Mario images will change making him look like he's moving to the left 
    
    //Constructor
    Model()
    {
        sprites = new ArrayList<>();    //Holds an arraylist of Sprites
    }

    //Copy Constructor to create a deep copy of the model
    Model(Model modelCopy)
    {
        sprites = new ArrayList<Sprite>();                              //Creates a new arraylist of Sprites so that it could be copied

        //Deep Copy
        this.coinNum = modelCopy.coinNum;                               
        this.marioisJumpingCounter = modelCopy.marioisJumpingCounter;   
        this.camPos = modelCopy.camPos;                                

        for (int i = 0; i < modelCopy.sprites.size(); i++)
        {
            Sprite modelC = modelCopy.sprites.get(i);                   //Copys the sprite from the arraylist of sprites to copy a new sprite arraylist
            Sprite clone = modelC.cloneMe(this);                        //Allows the deep copy of the object at whatever index and deep copies it to clone
            sprites.add(clone);                                         //Adds the clone to the arraylist that was created at the start of the copy constructor
        }
    }

    //Unmarshall method to load the map.json file
    void unmarshall(Json ob)
    {
        sprites.clear();                                    //Clears whatever is in the Json object

        Json json_sprites = ob.get("Sprites");              //Takes all of the data from the json file

        for (int i = 0; i < json_sprites.size(); i++)    
        {
            Json j = json_sprites.get(i);                   //Sets object j to whatever the index is

            String category = j.getString("Category");      //Gets what ever sprite that matches category
            if(category.equals("Mario"))                    //Checks to see if the Sprite in the json file is a Mario
            {
                Mario m = new Mario(j, this);
                sprites.add(m);
            }
            else if(category.equals("CoinBlock"))
            {
                sprites.add(new CoinBlock(j,this));
            }
            else if(category.equals("Brick"))
            {
                sprites.add(new Brick(j, this));
            }

        }
    }

    //Marshall method to allow save file to turn everything into a json object
    Json marshall()
    {
        Json ob = Json.newObject();             //Makes new Json Object
        Json json_sprites = Json.newList();     //Makes new Json List
        ob.add("Sprites", json_sprites);

        for (int i = 0; i < sprites.size(); i++)
        {

            Sprite s = sprites.get(i);          //Get Sprite
            Json j = s.marshall();              //Turn into Json object
            json_sprites.add(j);                //Turns it into Json List
        }
        return ob;
    } 

    enum Action
    {
        run,        //Equivalent to "0"
        jump,       //Equivalent to "1"
    }

    //Do action method so that the AI will do whatever the "enum action" is
    void do_action(Action action)
    {
        Mario mario = (Mario)sprites.get(0);        //Converts the Mario sprite to a mario
        if (action == Action.run)                   //Cycle through the "moving right" images and lets Mario move
        {
            marioRight = true;
            mario.x +=10;
        }
        else if (action == Action.jump)             //Sets Mario's jumping
        {
            if(mario.jumpFrame <5){
                mario.vert_vel -= 8.0;
                marioisJumpingCounter++;            //Keeps track of the number of times Mario's AI jumps
                mario.y += mario.vert_vel;
            }
        }
    }

    //AI implementation method
    double evaluateAction(Action action, int depth)
    {
        int d = 33;
        int k = 4;
        // Evaluate the state
        if(depth >= d)
            return sprites.get(0).x + 50000 * coinNum - 2 * marioisJumpingCounter;
    
        // Simulate the action
        Model copy = new Model(this);        // uses the copy constructor
        copy.do_action(action);              // like what Controller.update did before
        copy.update();                       // advance simulated time
    
        // Recurse
        if(depth % k != 0)                
           return copy.evaluateAction(action, depth + 1);
        else
        {
           double best = copy.evaluateAction(Action.run, depth + 1);
           best = Math.max(best,
               copy.evaluateAction(Action.jump, depth + 1));
           return best;
        }
    }

    //Allows saving to occur to a specific json file
    void save(String filename)
    {
        Json ob = marshall();              //Turns everything to Json by calling marshall method
        ob.save(filename);                //Saves the object into a the json filename         
        System.out.println("Save successful");
    }

    //Loads the data
    void load(String filename)
    {
        Json j = Json.load(filename);      
        unmarshall(j);
        System.out.println("Load successful");
    } 

    //Method to add bricks
    void addBrick(int x, int y, int w, int h)
    {
        Brick b = new Brick(x, y, w, h, this);      //Creating a new Brick object
        sprites.add(b);                             //Adds the newly created Brick object into the sprites arraylist
    }

    //Method to addCoinBlocks
    void addCoinBlock()
    {
        for (int i = 1; i < 4; i++)
        {
            CoinBlock cb = new CoinBlock(i*700, 300, this);     //Creates a new CoinBlock object
            sprites.add(cb);                                    //Adds the newly created CoinBlock object into the sprites Arraylist
        }
    }

    //Method to remove Coins
    void removeCoin(Coin c)
    {
        sprites.remove(c);      //This method is called whenever the coin hits the ground
    }
    
    //Update Method
    public void update()
    {
        Mario m = (Mario)sprites.get(0);            //Lets mario object always be index 0
        for (int i = 0; i <sprites.size(); i++)
        {
            Sprite s = sprites.get(i);              //Gets the sprite in the arraylist at whatever value "i" is at
            if(s.checkBrick())                      //Checks to see if the Sprite in the arraylist is a Brick
            {
                s.checkCollision(this,m,s);         //Checks the collision with the brick
            }
            if(s.checkCoinBlock())                  //Checks to see if the Sprite in the arralist is a CoinBlock
            {
                s.checkCollision(this, m, s);       //Checks the collision with the CoinBlock
            }
            s.update();
        }
    }
}