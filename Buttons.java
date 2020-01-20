import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * This class is a widget that creats buttons
 * for users to input their own orders into
 * the simulation
 * 
 * @author Zhongchi Li
 * @version October 2018
 */
public class Buttons extends Actor
{
    private GreenfootImage myImage; // original pic
    private GreenfootImage myAltImage; // pic after the button is pressed down
    // booleans to describe the state of the buttons
    private boolean clicked;
    private boolean disable;
    private boolean stay;
    
    /**
     * The main constructor, accepts the length, width, 
     * name of the image files, and whether it will stay
     * as clicked down or not
     */
    public Buttons(int length, int width, String image, String altImage, boolean stay)
    {
        //set new images
        myImage = new GreenfootImage(image);
        myAltImage = new GreenfootImage(altImage);
        //scale the images
        myImage.scale(length,width);
        myAltImage.scale(length,width);
        //initialize the states
        clicked = false;
        disable = false; 
        this.stay = stay;
    }
    /**
     * Act, sets paint order and check for clicks
     */
    public void act() 
    {
        getWorld().setPaintOrder(Buttons.class);
        if(disable==false)
        {
            click();
        }
    }
    
    /**
     * this method checks if button is clicked and
     * sets the images accordingly
     */
    public void click()
    {
        //if the buttons stays clicked down, switch its state after each click
        if(stay)
        {
            if (Greenfoot.mouseClicked(this) && clicked==false)
            {
                clicked = true;
            }
            else if (Greenfoot.mouseClicked(this) && clicked == true)
            {
                clicked = false;
            }
            if(clicked == false)
            {
                setImage (myImage);
            }
            else if(clicked == true)
            {
                setImage (myAltImage);
            }
        }
        //if the button does not stay, switch the state only when mouse is clicking
        else
        {
            if (Greenfoot.mousePressed(this))
            {
                setImage (myAltImage);
            }
            else
            {
                setImage (myImage);
            }
        }
    }
    
    /**
     * returns if it has been clicked or not
     */
    public boolean getClicked()
    {
        return clicked;
    }
    
    /**
     * returns if it has been disabled or not
     */
    public void disable()
    {
        disable = true;
    }
    
    /**
     * returns whether it is enabled
     */
    public void enable()
    {
        disable = false;
    }
}
