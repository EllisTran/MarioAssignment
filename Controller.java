import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements ActionListener, MouseListener, KeyListener
{
    //Declaring Member Variables
    View view;
    Model model;

    boolean keyLeft;
    boolean	keyRight;
    boolean keyUp;
    boolean keyDown;
    boolean keySpace;
    int mouseDownX;
    int mouseDownY;

    //Constructor
    Controller(Model m)
    {
        model = m; //Initializes model object
    }

    void setView(View v) 
	{
        view = v;
    }

    //Update Method
    void update()
    {
        Mario mario = (Mario)model.sprites.get(0);      //References to Mario so that sprites.clear does not clear the Mario sprite object

        mario.prevdestination();                        //Gets the previous position of mario

        //Determines what happens with mario when such keys are pressed
        if(keyRight)
        {
            mario.x += 10;                              //Moves the mario to the right if the right key is pressed
        }
        if(keyLeft)
        {
            mario.x -=10;                               //Moves Mario to the left if the left key is pressed
        }
        if (keySpace)                                   //Lets Mario Jump
        {
            if (mario.jumpFrame < 5)                    //Lets mario Jump up if the framerate count is less than 5
            {
                mario.vert_vel -= 8.0;
                mario.y += mario.vert_vel;
            }
        }

        // Evaluate each possible action
        double score_run = model.evaluateAction(Model.Action.run, 0);
        double score_jump = model.evaluateAction(Model.Action.jump, 0);

        // Do the best one
        if(score_run > score_jump)
            model.do_action(Model.Action.run);
        else
            model.do_action(Model.Action.jump);
    }

    public void mousePressed(MouseEvent e)
    {
        mouseDownX = e.getX();
        mouseDownY = e.getY();
    }

    //Methods for mouse commands
    public void mouseReleased(MouseEvent e)
    {
        int x1 = mouseDownX;
        int x2 = e.getX();
        int y1 = mouseDownY;
        int y2 = e.getY();
        int left = Math.min(x1, x2);
        int right = Math.max(x1, x2);
        int top = Math.min(y1, y2);
        int bot = Math.max(y1, y2);

        model.addBrick(left + model.camPos, top, right - left, bot - top);
    }

    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }
    public void mouseClicked(MouseEvent e)
    {
        if(e.getY() < 100)
        {
            System.out.println("Break here");
        }
    }

    public void keyPressed(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT: keyRight = true;
            {
                model.marioRight = true;
                break;
            }

            case KeyEvent.VK_LEFT: keyLeft = true;
            {
                model.marioLeft = true;
                break;
             }

            case KeyEvent.VK_UP: keyUp = true; break;
            case KeyEvent.VK_DOWN: keyDown = true; break;
            case KeyEvent.VK_SPACE: keySpace = true; break;
            case KeyEvent.VK_S: model.save("map.json"); break;
            case KeyEvent.VK_L: model.load("map.json"); break;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT: keyRight = false; 
            {
                model.marioRight = false;
                break;
            }
            case KeyEvent.VK_LEFT: keyLeft = false;
            {
                model.marioLeft = false;
                break;
            }
            case KeyEvent.VK_UP: keyUp = false; break;
            case KeyEvent.VK_DOWN: keyDown = false; break;
            case KeyEvent.VK_SPACE: keySpace = false; break;
        }
    }

    //Deletes the Button if the button is pressed
    public void actionPerformed(ActionEvent e)
    {
        view.removeButton();
    }

    public void keyTyped(KeyEvent e)
    {}
}
