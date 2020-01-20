import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Justin Huynh
 * @version October 2018
 */
public class Vegetables extends Ingredients
{
    

    public Vegetables(String foodName)
    {
        this.foodName = foodName;

        image = new GreenfootImage(foodName + "_uncooked.png");
        this.setImage(image);
    } 
}
