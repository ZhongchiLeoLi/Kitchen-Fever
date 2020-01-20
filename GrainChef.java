import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * @author Alexander, Daniel, Deston and Justin
 * @version October 2018
 */
public class GrainChef extends Chefs
{
    /**
     * Constructor
     */
    public GrainChef()
    {
        GreenfootImage image = new GreenfootImage("walkDown0.png");
        image.scale(image.getWidth()+32, image.getHeight()+32);
        setImage(image);
        type = 2;
        idle = false;
    }

    /**
     * Act - do whatever the PlaterChef wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        checkOrder (2);
        if (ingredientSelect == "Rice" && !panic){
            prepare("grain", "rice");
        }
        //if meat within the order is beef
        else if (!panic)
        {
            prepare("grain", "noodles");
        }
        else
        {
            idleWalk();
        }
        
   
        if(!placedPlate)
            FollowChef();
    }    

    protected void FollowChef(){
        //designates plate to be an instance of Plate class
        Plate plate = (Plate)getOneObjectAtOffset(20, 20, Plate.class);
        //If plates are touching the chef, move with it 
        if(plate != null){
            if(targetY < this.getY())//up
            {
                plate.setLocation(this.getX(), this.getY() - speedY);
            }
            if(targetY > this.getY())//down
            {
                plate.setLocation(this.getX(), this.getY() + speedY);
            }
            if(targetX < this.getX())//left
            {
                plate.setLocation(this.getX() - speedX, this.getY());
            }
            if(targetX > this.getX())//right
            {
                plate.setLocation(this.getX() + speedX, this.getY());
            }
        }
    }
   
    /**
     * Updates whether the plate has been placed
     */
    public void updatePlate()
    {
        placedPlate = !placedPlate;
    }
}