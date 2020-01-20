import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * @author Alexander, Daniel, Deston and Justin
 * @version October 2018
 */
public class MeatChef extends Chefs
{
    /**
     * Constructor for MeatChef class
     */
    public MeatChef()
    {
        GreenfootImage image = new GreenfootImage("walkDown0.png");
        image.scale(image.getWidth()+32, image.getHeight()+32);
        setImage(image);
        type = 0;
        idle = false;
    }

    protected void FollowChef(){
        //designates ChickenOrBeef to be an instance of Meat class
        ChickenOrBeef = (Meat)getOneObjectAtOffset(35, 35, Meat.class);
        //if something from meat class (chicken or beef) is touching the chef 
        if(ChickenOrBeef != null && !ChickenOrBeef.getCooking()){
            //relocate instance of food depending on speed of chef in respective direction
            if(targetY < this.getY()) //up
            {
                ChickenOrBeef.setLocation(this.getX(), this.getY() - speedY);
            }
            if(targetY > this.getY()) //down
            {
                ChickenOrBeef.setLocation(this.getX(), this.getY() + speedY);
            }
            if(targetX < this.getX()) //left
            {
                ChickenOrBeef.setLocation(this.getX() - speedX, this.getY());
            }
            if(targetX > this.getX()) //right
            {
                ChickenOrBeef.setLocation(this.getX() + speedX, this.getY());
            }
        }
    }

    public void act() 
    {
        checkOrder(0);
        //operates through "prepare" method depending on what is required by the chef in the order
        //if meat within the order is chicken
        if (ingredientSelect == "Chicken" && !panic){
            prepare("meat", "chicken");
        }
        //if meat within the order is beef
        else if (!panic)
        {
            prepare("meat", "beef");
        }
        else
        {
            idleWalk();
        }
        cut();
        if(!isCutting) FollowChef();
    }
}