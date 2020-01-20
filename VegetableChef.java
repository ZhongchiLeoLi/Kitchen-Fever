import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Alexander, Daniel, Deston and Justin
 * @version October 2018
 */
public class VegetableChef extends Chefs
{
    /**
     * Constructor
     */
    public VegetableChef()
    {
        GreenfootImage image = new GreenfootImage("walkDown0.png");
        image.scale(image.getWidth()+32, image.getHeight()+32);
        setImage(image);
        type = 1;
        idle = false;
    }

    protected void FollowChef(){
        //designates BroccoliOrBokChoy to be an instance of Vegetables class
        BroccoliOrBokChoy = (Vegetables)getOneObjectAtOffset(35, 35, Vegetables.class);
        //if something from Vegetables class (BroccoliOrBokChoy) is touching the chef and has been cooked
        if(BroccoliOrBokChoy != null && !BroccoliOrBokChoy.getCooking()){
            //relocate instance of food depending on speed of chef in respective direction
            if(targetY < this.getY()) //up
            {
                BroccoliOrBokChoy.setLocation(this.getX(), this.getY() - speedY);
            }
            if(targetY > this.getY()) //down
            {
                BroccoliOrBokChoy.setLocation(this.getX(), this.getY() + speedY);
            }
            if(targetX < this.getX()) //left
            {
                BroccoliOrBokChoy.setLocation(this.getX() - speedX, this.getY());
            }
            if(targetX > this.getX()) //right
            {
                BroccoliOrBokChoy.setLocation(this.getX() + speedX, this.getY());
            }
        }
    }

    /**
     * Act - do whatever the VegetableChef wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        checkOrder(1);
        if (ingredientSelect == "Broccoli" && !panic){
            prepare("vegetable", "broccoli");
        }
        else if (!panic)
        {
            prepare("vegetable", "bok choy");
        }
        else 
        {
            idleWalk();
        }
       
        cut();
        if(!isCutting)FollowChef(); 
    }
}