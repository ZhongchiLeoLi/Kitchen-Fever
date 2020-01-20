import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Alexander
 * @version October 2018
 */
public class Washing extends Actor
{
    private GreenfootImage[] anime = new GreenfootImage[2]; //Stores animation pictures
    private int animation = 0; //What animation image it is on
    private MyWorld world;
    public Washing()
    {
        for (int i = 0;  i < 2; i++)//Stores images in the array
        {
            anime [i] = new GreenfootImage("bubbles"+ (i) + ".png");
            anime [i].scale(35,35);//Scales the image down
        }
    }
    public void addedToWorld(World w)
    {
        world = (MyWorld) getWorld();
        getImage().scale(35,35);//Scales the image down
    }
    public void act()
    {
        animation();
    }
    private void animation()
    {
        if (world.getFrames() % 20 == 0) //Animation occurs every 20 frames
        {
            setImage (anime[animation]);//Go to next animation image
            animation ++;
        }
        if (animation == 2)
        {
            animation = 0;//Resets animation
        }
    }
}
