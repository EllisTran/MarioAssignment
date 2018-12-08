import javax.swing.JFrame;

import java.awt.Toolkit;


public class Game extends JFrame
{
    //Declare Member Variables
    Model model;
    Mario mario;
    Controller controller;
    View view;

    public Game()
    {
        //Instantiates the objects
        model = new Model();
        mario = new Mario(model);
        controller = new Controller(model);
        view = new View(controller, model);

        //Adds Mario and CoinBlocks at the very beginning of the game
        model.sprites.add(mario);
        model.addCoinBlock();

        model.load("map.json");                                 //Loads the map

        this.setTitle("Super Plumber Bros.");                   //Sets the title of the window
        this.setSize(1000, 800);                                //Sets size of window
        this.setFocusable(true);
        this.getContentPane().add(view);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        view.addMouseListener(controller);                      //Makes the input of the mouse
        this.addKeyListener(controller);                        //Makes the input of the Keys work
    }



    //Run's the movement of Mario
    public void run()
    {
        while(true)
        {
            model.update();
            controller.update();

            view.repaint();                         // Indirectly calls View.paintComponent
            Toolkit.getDefaultToolkit().sync();     // Updates screen

            // Go to sleep for 50 miliseconds
            try
            {
                Thread.sleep(40);
            } 
            catch(Exception e) 
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    //Main Method
    public static void main(String[] args)
    {
        Game g = new Game();
        g.run();
    }

}
