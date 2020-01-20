import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Daniel
 * @version October 2018
 */
public class CuttingBoard extends Targets
{
    public CuttingBoard()
    {
        setImage("beeperBlue.png");
        getImage().setTransparency(0);
    }
    /**
     * Act - do whatever the target3 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        moveAround();
        move();
      
    }    
}