import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * The Ingredients class works along with the Chef class to create
 * instances of different types of food such as meat, vegetables, and 
 * grains which are used by the chefs. Each instance of meat, vegetables,
 * or grains has its own unique properties to determine how it is being
 * processed (chopped or cooked), and acts appropriately based on which part
 * of the process it is going through.
 *
 * Within the Ingredients class, an interesting aspect to take note of is that many of the 
 * methods within the class are called on through the Chef class, thus showing how the two
 * major superclasses within the program interact closely to make the program function smoothly. 
 * For example, many methods deal with Greenfoot Images to effectively animate and process food or calculate
 * the time for which food is cooked before it is burnt are stored within the Food class, and are further 
 * implemented to work alongside the Chefs after such objects are created in the world. These methods are 
 * specifically optimized in order to reduce redundant code and bring out the benefits of 
 * Object Oriented Programming due to their interactivity with the Food class. 
 * 
 * In addition, our class fully utilizes the benefits of Object Oriented Programming because many instances of 
 * each object can be created, and they will all work the same in our simulation because of how our methods 
 * were generalized. As a result, no matter what type of object or how many of them there are, it will all work 
 * seamlessly with our simulation.
 * 
 * @author Deston Wong
 * @author Justin Huynh
 * @version October 2018
 */
public abstract class Ingredients extends Actor
{
    //Ingredient Properties:
    protected String foodName;//Sets the name of the food inside the constructor
    protected boolean isPreparing = false;//True if the ingredient is currently being cut/cooked
    protected boolean isChopped = false;//True if the ingredient has been chopped
    protected boolean isCooked = false;//True if the ingredient has been cooked
    protected boolean isCooking = false;
    protected boolean isBurning = false;//True if the ingredient is currently burning
    private static boolean isPanic = false;//True if there is a panic
    protected boolean locationSet = false;//True if the ingredient's location has been set after being placed
    private int position; //Position of food for when it is cooking

    //Images
    protected GreenfootImage image = this.getImage();//Original image of food
    protected GreenfootImage altImage = this.getImage();//Alternate image of food that will be altered
    protected GreenfootImage cloud;//Cloud image covering cutting animation
    protected GreenfootImage smoking;//Smoke image covering burning animation

    //Miscellaneous
    private int extension1 = 0;//File extension for the image when cooking
    private int extension2 = 0;//File extension for the image when burning
    private int burnExt = 0;//File used to check extension2 when burning
    private int frameDelay = 5;//Frame delay until the animation will update
    private int yOffset = 0;//How far up/down ingredient will be placed when at a stove or cutting board
    private int sec = 0;//Value for how many seconds have passed after food is cooked, is past stage 1 of burning, past stage 2 of burning, etc.
    private int burningCheck = 0;//Value updated when panicTimer is created (updates once every 60 frames in world)
    private int cookingTime = 10;//how long it takes to cook the food
    Timer timer;//Timer created when cooking
    private GreenfootSound meatCooking = new GreenfootSound("meatCooking.wav");//Sound for when meat is cooking
    private GreenfootSound vegCooking = new GreenfootSound("vegCooking.wav");//Sound for when vegetable is cooking

    /**
     * A method which runs for each instance of Food in the world. Repeatedly checks if ingredients are cooked 
     * or chopped so they can act according to actions of the Chef class.
     */
    public void act()
    {
        //If ingredient is at a cutting board or a stove:
        if(isPreparing)
        {    
            //Sets food offset when chef is at cutting board or stove
            placeFoodOffset();
            //Checks if a certain number of frames has passed
            if(((MyWorld)getWorld()).getFrames() % frameDelay == 0)
            {
                //Sets image for food depending on cooking or chopping (if in preparation)
                setCookingOrChoppingImage();
                //Animates food if burning on frying pan
                foodBurningAnimate();
            }
            //If the food is from the Meat class, display below the chef
            if(foodName == "chicken" || foodName == "beef")
            {
                getWorld().setPaintOrder(Buttons.class, Sliders.class, Meat.class);
            }
            //If the food is from the Vegetables class, display below the chef
            else
            {
                getWorld().setPaintOrder(Buttons.class, Sliders.class, Vegetables.class);
            }
        }
    }

    private void placeFoodOffset(){
        //If ingredient's starting location has not been set:
        if(!locationSet)
        {
            //If is chopped (AKA at stove), then move image so that its on top of a frying pan
            if(isChopped)
            {
                setLocation (334 + 106 * position, 130);
            }
            //If not chopped (AKA at cutting board), then move image down 50 pixels
            else
            {
                yOffset = 50;
                setLocation(getX(), getY() + yOffset);
            }
           
            locationSet = true;
        }
    }

    private void foodBurningAnimate(){
        //If the food is currently burning then:
        if(isBurning)
        {
            //Repeatedly swaps the value of extension 2 to the value of burnExt and burnExt + 1
            if(extension2 == burnExt)extension2 = burnExt + 1;
            else extension2 = burnExt;
            //Creates and sets image of smoking animation over the current image
            smoking = new GreenfootImage("smoking" + extension2 + ".png");
            smoking.scale(65,95);
            altImage.drawImage(smoking, 0, -5);
        }
        setImage(altImage);

        //If the cooking timer is completed, then it will be removed
        if(timer != null && timer.isDone() && isChopped)
        {
            ((MyWorld)getWorld()).removeObject(timer);
            //Means that ingredient is now cooked and it will start burning
            isCooked = true;
            isBurning = true;
        }

        //Sec will store the amount of seconds passed after it started burning
        if(isBurning && ((MyWorld)getWorld()).getFrames() % 60 == 0)
        {
            sec++;
        }
        //If five seconds has passed, then the burn extension will update so the next two images will be used
        if(sec>=3)
        {
            burnExt = burnExt + 2;
            extension2 = burnExt;
            sec = 0;
        }
        //If last burning animation has been played:
        if(burnExt == 8)
        {
            burnExt = 6;
            extension2 = 6;
            Greenfoot.playSound("firealarm.wav");
            if(!isPanic)
            {
                //Iterate through each chef in the world and set them into a panic
                List<Chefs> chefList = getWorld().getObjects(Chefs.class);
                for(Chefs c : chefList)
                {
                    c.setPanic();
                }
                isPanic = true;
            }
            //Checks the world for 60 frames (every second)
            if (((MyWorld)getWorld()).getFrames() % 60 == 0){
                //Increase burningCheck by 1
                burningCheck ++;
                //When burningCheck is equal to 5 (after 5 seconds have passed)
                if (burningCheck == 5){
                    //Delay the current world and change worlds to an EndScreen
                    Greenfoot.delay(250);
                    EndScreen KitchenFever = new EndScreen();
                    Greenfoot.setWorld(KitchenFever);
                }
            }
        }
    }

    private void setCookingOrChoppingImage(){
        //If ingredient is cooking:
        if(isChopped)
        {
            //Repeatedly swaps the cooking image every interval
            if(extension1 == 1)extension1 = 0;
            else extension1 = 1;
            altImage = new GreenfootImage(foodName + "_cooking" + extension1 + ".png");
        }
        else
        {
            //Same as above, but repeatedly swaps the cloud image above the ingredient
            if(extension1 == 1)extension1 = 0;
            else extension1 = 1;
            cloud = new GreenfootImage("Cloud" + extension1 + ".png");
            cloud.scale(85,65);

            //Draws cloud over the current image at a random offset
            altImage = new GreenfootImage(image);
            altImage.drawImage(cloud, Greenfoot.getRandomNumber(10),5+Greenfoot.getRandomNumber(10));
        }
    }

    /**
     * Determines the actions of the food when it is picked up, after it has been either 
     * chopped or cooked. Is called upon throughout methods in the Chef class.
     * 
     */        
    public void PickUp()
    {
        //If food has been both chopped and cooked
        if(isChopped && isCooked)
        {
            //Updates current image to cooked ingredient
            image = new GreenfootImage(foodName + "_cooked.png");

            //Stops playing cooking sounds
            if(foodName == "beef" || foodName == "chicken")
                meatCooking.stop();
            else
                vegCooking.stop();

            //If the food is from the meat class, display above the chef
            if(foodName == "chicken" || foodName == "beef")
            {
                getWorld().setPaintOrder(Washing.class, Buttons.class, Meat.class);
            }
            //If the food is from the vegetable class, display above the chef
            else
            {
                getWorld().setPaintOrder(Washing.class,Buttons.class, Vegetables.class);
            }
        }
        //If food is uncut and/or uncooked
        else
        {
            //Change food to cut version, display the cut version of the food
            isChopped = true;
            image = new GreenfootImage(foodName + "_cut.png");
            frameDelay = 20;
            //If the food is from the meat class, display meat chef in front
            if(foodName == "chicken" || foodName == "beef")
            {
                getWorld().setPaintOrder(Washing.class, MeatChef.class);
            }
            //If the food is from the vegetable class, display vegetable chef in front
            else
            {
                getWorld().setPaintOrder(Washing.class, VegetableChef.class);
            }
        }
        //Update the image of the food based on the actions it has gone through //above (been cooked or cut)
        this.setImage(image);
        isPreparing = false;
        locationSet = false;
    }

    /**
     * Initializes boolean allowing for animations to occur. Is called upon throughout methods in the Chef class. 
     * 
     * @param position Position of the ingredient relative to which pan it is going on
     */
    public void Process(int position)
    {
        this.position = position; //gets position of frying pan
        //If ingredient is not currently being cooked/chopped:
        if(!isPreparing)
        {
            //If ingredient has been cut (AKA is about to be cooked)
            if(isChopped && getY() < 300)
            {
                getWorld().setPaintOrder(Washing.class, Buttons.class, Sliders.class , Counter.class);
                //Adds a timer that will show how long it will take to cook
                timer = new Timer(cookingTime,0,255,77,10,30);
                getWorld().addObject(timer, getX()-30, getY()-5);
                isCooking = true;
                //Starts playing cooking sounds
                if(foodName == "beef" || foodName == "chicken")
                    meatCooking.play();
                else
                    vegCooking.play();
            }
            //If ingredient has not been cut (AKA is about to be cut)
            else
            {
                //Sets counter to the front of the world, with the corresponding ingredient class in front
                getWorld().setPaintOrder(Washing.class, Buttons.class, Sliders.class , Counter.class);
                if(foodName == "chicken" || foodName == "beef")
                {
                    getWorld().setPaintOrder(Washing.class,Meat.class);
                }
                else if(foodName == "bok choy" || foodName == "broccoli")
                {
                    getWorld().setPaintOrder(Washing.class, Vegetables.class);
                }
            }
            isPreparing = true;
        }

    }

    /**
     * Getter method to determine whether the ingredient is cooking.
     * 
     * @return boolean True if ingredient is being cooked 
     */
    public boolean getCooking()
    {
        return isCooking;
    }
    
    /**
     * Getter method to determine whether the ingredient is fully cooked.
     * 
     * @return boolean True if ingredient is fully cooked 
     */
    public boolean getCooked()
    {
        return isCooked;
    }
    
    /**
     * Getter method to determine whether the ingredient is chopped.
     * 
     * @return boolean True if ingredient is chopped
     */
    public boolean getChopped()
    {
        return isChopped;
    }
    /**
     * Getter method to determine the name of the ingredient
     * 
     * @return String The name of the current ingredient
     */
    public String getName()
    {
        return foodName;
    }
    
    /**
     * Set cooking time 
     * 
     * @param int How long the ingredient takes to cook
     */
    public void setCookingTime(int time)
    {
        cookingTime = time;
    }
    
    /**
     * resets cutting time 
     */
    public int getCookingTime()
    {
        return cookingTime;
    }
}

