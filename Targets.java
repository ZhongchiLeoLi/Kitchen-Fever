import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Daniel
 * @version October 2018
 */
public class Targets extends Actor
{
    protected int speed = 1;
    protected boolean isFree = true;
    protected boolean hasPlate = false;

    protected void move()
    {
        if(Greenfoot.isKeyDown("up"))
        {
            setLocation(getX(), getY()-speed);

        }
        if(Greenfoot.isKeyDown("down"))
        {
            setLocation(getX(), getY()+speed);

        }
        if(Greenfoot.isKeyDown("left"))
        {
            setLocation(getX()-speed, getY());

        }
        if(Greenfoot.isKeyDown("right"))
        {
            setLocation(getX()+speed, getY());

        }
    }

    protected void moveAround()
    {
        if(Greenfoot.isKeyDown("enter"))
        {
            setLocation(Greenfoot.getRandomNumber(960), Greenfoot.getRandomNumber(640));
        }
    }

    protected void setFreeFalse()
    {
        isFree = false;

    }

    protected void setFreeTrue()
    {
        isFree = true;
    }

    protected boolean getFree()
    {
        return isFree;
    }

    protected void setPlateTrue()
    {
        hasPlate = true;
    }

    protected void setPlateFalse()
    {
        hasPlate = false;
    }

    protected boolean getPlate()
    {
        return hasPlate;
    }
}
