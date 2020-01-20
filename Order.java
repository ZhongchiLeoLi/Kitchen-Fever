import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Creates and stores data about the orders
 * 
 */
public class Order extends Actor
{
    /**
     * Act - do whatever the Order wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private MyWorld world;
    private Chefs chef;
    private String [] ingredients = new String [3];
    private GreenfootImage image;
    private ProgressBar p;
    public Order (String[] ingredients )
    {
        //SetImage based off of given ingredients
        this.ingredients = ingredients;
        image = new GreenfootImage("Order" + ingredients[0] + ingredients[1] + ingredients[2] + ".png");
        image.scale(100, 70);
        setImage(image);
    }
    
    public void addedToWorld (World w)
    {
        //Creates a progressbar
        world = (MyWorld) getWorld();
        ProgressBar p = new ProgressBar(102,12);
        world.addObject(p,getX(),getY()-31);
    }
    
    public String[] getIngredients() 
    {
        //returns all ingredients associates with the class
        return ingredients;
       
    } 
  

    public void changeLocation()
    {   
        setLocation (getX() - 120, getY());
    }
}
