import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The sliders class is a widget that
 * allows the user to change certain
 * time lengths up to a range in this 
 * simulation.
 * 
 * @author Zhongchi Li with the help of Gevater_Tod4711
 * @version Oct 2018
 */
public class Sliders extends Actor
{
    //the limit for the size of the image
    private int MIN_X = -82;  
    private int MAX_X = 81;
    private double RANGE_X = MAX_X - MIN_X;
    //the max and min x coordinates
    private int maxX;
    private int minX;
    //assistant object
    private SliderHand hand;

    //initializing more private variables
    private boolean enabled;
    private int val;
    private int min;
    private int max;
    private int range;
    private int ratio;
    private int ini;

    /**
     * The main constructor, accepts a minimum, maximum and inital vaue,
     * and an int to indicate which slider it is
     */
    public Sliders(int min, int max, int initial, int which)
    {
        //set the image based on which slider it is
        if (which==1)
        {
            setImage(new GreenfootImage("slider-base1.png"));
        }
        else
        {
            setImage(new GreenfootImage("slider-base.png"));
        }

        //determine range and ratio
        range = 60 * max - 60 * min;
        ratio = (int) ((double) range/RANGE_X);
        ini = initial;
    }

    public void act()
    {
        //set the order or the objects in the world
        getWorld().setPaintOrder(Washing.class, SliderHand.class/*,Sliders.class */);
    }

    /**
     * add the hand to the world
     */
    public void addedToWorld(World world)
    {
        hand = new SliderHand();
        getWorld().addObject(hand, getX(), getY()-10);
    }

    /**
     * returns initial value
     */
    public int getInitial()
    {
        return ini;
    }

    /**
     * returns the ratio between the length of 
     * the image and the range of the value
     */
    public int getRatio()
    {
        return ratio;
    }

    /**
     * returns minimum x value
     */
    public int getMinX()
    {
        return getX() + MIN_X;
    }

    /**
     * returns maximum x value
     */
    public int getMaxX()
    {
        return getX() + MAX_X;
    }

    /**
     * returns current x value
     */
    public int getCurX()
    {
        return getX();
    }

    /**
     * returns the assisting hand object
     */
    public SliderHand getHand()
    {
        return hand;
    }
    /**
     * The class that acts as a pointer on the sliding bar
     */
    class SliderHand extends Actor
    {    
        private int oldX; //the original spot
        private int curValue; //current time length

        /**
         * The main constructor, sets the image
         */
        public SliderHand()
        {
            setImage("slider-hand.png");
        }

        /**
         * The act method, places the object
         * allows mouse to drag it while limited
         * and calculate the new time length to
         * pass down
         */
        public void act()
        {
            if (Greenfoot.mouseDragged(this)) {
                int oldX = getX();
                MouseInfo mouse = Greenfoot.getMouseInfo();
                int x = mouse.getX();

                //check if it's outside to the left of the bar
                if (x < getMinX())
                    x = getMinX();
                //check if it's outside to the right of the bar  
                if (x > getMaxX())
                    x = getMaxX();
                //if position has changed, calculate current time length  
                if (x != oldX) {
                    setLocation(x, getY());
                    curValue= (x - getCurX())*getRatio() +60*getInitial(); //times 60 because 60 frames per second
                }
            }
        }

        /**
         * returns current time length
         */
        public int getCurValue()
        {
            return curValue;
        }

    }
}
