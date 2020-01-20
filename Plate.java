import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * When a chef interacts with a plate, their cooked ingredient will be drawn onto the plate. When the plate is 
 * complete, it can be sent out to the customer.
 * 
 * @author Deston Wong 
 * @author Justin Huynh
 * @version October 2018
 */
public class Plate extends Actor
{
    //Plate Content Variables
    private boolean hasSide = false;
    private boolean hasVeggie = false;
    private boolean hasMeat = false;
    private boolean locationSet = false;
    private int position;
    
    //Image variables
    private GreenfootImage plate = getImage();
    private GreenfootImage food;

    //Sets the image to a scaled version
    public Plate()
    {
        plate.scale(55, 55);
        this.setImage(plate);
    }

    /**
     * Adds the image of the given food onto the plate
     * 
     * @ param foodName The name of the cooked ingredient being added to the plate (all in lowercase, with spaces if applicable)
     */
    public void setFood(String foodName)
    {
        //Checks which food type is being added to the plate, and changes the according boolean
        if (foodName.equals("beef") || foodName.equals("chicken")){
            hasMeat = true;
        }
        else if (foodName.equals("bok choy") || foodName.equals("broccoli")){
            hasVeggie = true;
        }
        else if (foodName.equals("rice") || foodName.equals("noodle")){
            hasSide = true;
        }

        //Draws a scaled image of the food onto the plate
        food = new GreenfootImage(foodName + "OnPlate.png");
        food.scale(55, 55);
        plate.drawImage(food, 0, 0);
    }

    /**
     * Place plate at a location 40 pixels to the right
     * 
     */
    public void placePlate()
    {
        //If location has not already been set:
        if(!locationSet)
        {
            GrainChef chef = (GrainChef)getOneIntersectingObject(GrainChef.class);
            chef.updatePlate();
            
            setLocation(822 + position * 14, getY());
            
            
            locationSet = true;
            Greenfoot.playSound("putPlateDown.wav");
        }
    }

    /**
     * Method to check if the plate has been completed
     * 
     * @return boolean True if plate has all components
     */
    public boolean checkIfComplete (){
        return hasMeat && hasVeggie && hasSide;
    }
    
    /**
     * Method to set position of the plate based off of the servery window
     * 
     * @param position Position of the plate relative to which ServeryTarget it is going on
     */
    public void setPosition(int position)
    {
        this.position = position;
    }
    
    /**
     * Method to get position of the plate based off of the servery window
     * 
     * @return int Position of the plate relative to which ServeryTarget it is going on
     */
    public int getPosition ()
    {
        return position;
    }
}