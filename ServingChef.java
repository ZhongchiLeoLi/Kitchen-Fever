import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Daniel
 * @version October 2018
 */
public class ServingChef extends Chefs
{
    /**
     * Constructor
     */
    public ServingChef()
    {
        GreenfootImage image = new GreenfootImage("walkDown0.png");
        image.scale(image.getWidth()+32, image.getHeight()+32);
        setImage(image);
        getImage().setTransparency(0);
        idle = false;
    }
    /**
     * Act - do whatever the ServingChef wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {

        prepare("server", "");
    }    
}