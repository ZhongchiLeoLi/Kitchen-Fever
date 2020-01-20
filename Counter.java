import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Updates the counter class image whenever the Grain Chef obtains noodles or rice.
 * 
 * @author Justin Huynh
 * @version October 2018
 */
public class Counter extends Actor
{
    MyWorld myWorld = (MyWorld) getWorld();
    
    public Counter()
    {
        returnImage();
    }

    /**
     * Updates counter image with noodles
     * 
     */
    public void getNoodles()
    {
        GreenfootImage image = new GreenfootImage("counterNoodles.png");
        image.scale(960, 640);
        setImage(image);
    }
    
    /**
     * Updates counter image with an open rice pot
     * 
     */
    public void getRice()
    {
        GreenfootImage image = new GreenfootImage("counterRice.png");
        image.scale(960, 640);
        setImage(image);
    }

    /**
     * Returns image to original countertop (no noodles or rice)
     */
    public void returnImage()
    {
        GreenfootImage image = new GreenfootImage("counterFront.png");
        image.scale(960, 640);
        setImage(image);
    }

    /**
     * After 50 frames have passed, image will return to the empty countertop
     */
    public void act()
    {
        if(((MyWorld)getWorld()).getFrames() % 50 == 0)
        {
            returnImage();
        }
    }
}