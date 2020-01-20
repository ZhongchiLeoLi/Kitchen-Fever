import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Alexander, Daniel, Deston and Justin
 * @version October 2018
 */
public class PlatingChef extends Chefs
{
    /**
     * Constructor
     */
    public PlatingChef()
    {
        GreenfootImage image = new GreenfootImage("walkDown0.png");
        image.scale(image.getWidth()+32, image.getHeight()+32);
        setImage(image);
        type = 3;
    }
    /**
     * Act - do whatever the FoodPlatingChef wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (!panic)
        {
            prepare("plater", "");
        }
        else 
        {
            idleWalk();
        }
        FollowChef();
        
    }    
    protected void FollowChef(){
        //designates food to be an instance of Ingredients class
        Ingredients food = (Ingredients)getOneObjectAtOffset(0, 0, Ingredients.class);
        //if something from Ingredients class (Meat/Vegetables/Grain) is touching the chef and has been cooked 
        if(food != null && food.getCooked() && follow){
            //relocate instance of food depending on speed of chef in respective direction
            if(targetY < this.getY()) //up
            {
                food.setLocation(this.getX(), this.getY() - speedY);
            }
            if(targetY > this.getY()) //down
            {
                food.setLocation(this.getX(), this.getY() + speedY);
            }
            if(targetX < this.getX()) //left
            {
                food.setLocation(this.getX() - speedX, this.getY());
            }
            if(targetX > this.getX()) //right
            {
                food.setLocation(this.getX() + speedX, this.getY());
            }
        }
    }
}
