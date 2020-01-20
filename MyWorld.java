import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;
/**
 * Kitchen fever is a simulation set in the kitchen of a busy restaurant. 
 * Inspired by the game "Overcooked", the simulation shows the busy work 
 * lives of chefs as they cut, cook and plate orders. The two main parent 
 * classes are the Ingredients and Chefs classes. However, there are many 
 * other classes used in the simulation. These include, the Order, Timer,
 * ProgressBar and Targets classes. 
 * <p>
 * Having two different types of meat, vegetable and grain ingredients, 
 * there are a total of eight dishes the the chefs can make.
 * <p>
 * The World class is primarily responsible for creating objects,
 * generating orders and removing the order displays when a dish 
 * has been complete. The World also helps keep track of time as it
 * runs at 60 frames per second
 * <p>
 * In the parent class Chefs, the child classes are broken up into meat,
 * vegetable, grain, plating and serving chefs. The meat and vegetable 
 * chefs are responsible for picking up, cutting and cooking ingredients.
 * The grain chef is responsible for picking up grain ingredients and placing
 * plates on the serving table. The Plating chef checks when the food is
 * cooked and places the food on the plates. Finally, the serving chef 
 * takes the plates to serve to the customers. 
 * <p>
 * The Target class is a parent class that helps with the movement of the chefs. 
 * Each subclass represents a station. Each subclass of chef will only go to 
 * their designated stations. For instance, the meat chef will only move towards
 * the FoodBox, CuttingBoard, FryingPan and Home Targets.
 * <p>
 * The Order class is primarily used to display the orders visually for 
 * the viewer. However it also creates the ProgressBar and tells the 
 * chefs what ingredients they need to cook.
 * <p>
 * The Timer and Progressbar act as an visual indication for when a 
 * process has been completed.
 * <p>
 * An interesting thing to look out for would be how the animations are done
 * For the animations, a 2D array is used. The array stores the type of 
 * animation needed (i.e. move down) and the images associated with that 
 * animation. This cuts back on a lot of code because everything is 
 * stored in one easily accessible array.
 * <p>
 * Another interesting thing to look out for would be the images used.
 * Almost all of background was hand drawn to fit the classic look
 * of the overcooked game. In addition, all of the food images were 
 * created using editing software. The most time consuming task was
 * colouring the images, especially the bricks and tiles as each individual
 * object has its unique look. 
 * <p>
 * If you don't want to wait for food to burn, press q and watch the chefs panic!
 * <p>
 * One of the hardest tasks for this simulation was figuring out how the 
 * chefs were going to move. Typically, one would just rotate the character to
 * move towards the target. However, since the simulation was not birds eye view, 
 * we had to go about it a completely different way through targets. However, an added
 * benefit of doing it this way is that we actually have walking animations for the chef
 * for all directions. In addition, I think this three dimensional view is one of the 
 * main things that help our simulation stand out from the others.
 * <p>
 * Sound effects were taken from the youtube channel "SOUND and IMAGE FX"
 * under the playlist "Cooking Sound Effect". Some food images were taken
 * from the game "Minecraft". Background music is from the game "Overcooked".
 * Animation sprites were created using the character sprite generator at
 * "http://gaurav.munjal.us/Universal-LPC-Spritesheet-Character-Generator/".
 * Object list and nanotime code given by Mr.Cohen.
 *
 * 
 * @author Daniel Tan, Alexander Yee and Zhongchi Li
 * @version October 2018
 */
public class MyWorld extends greenfoot.World
{
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    //Initializing variables for the orders
    private String [] ingredients;
    private String [] ingredients2 =  new String [3];
    private String [] ingredients3;
    private int orderTotal = 0;
    private int displayPlacement;
    private ArrayList<Order> orderList;
    private Order order;

    //Initializing variables to run simulation using nanotime
    private long lastFrame;
    private double framesPerSecond;
    private double secondsPerFrame;
    private int frames = 1;
    private int noneSelected = 0;

    //widgets
    private Buttons addOrder;
    private Buttons rice;
    private Buttons noodles;
    private Buttons chicken;
    private Buttons beef;
    private Buttons broccoli;
    private Buttons bokchoy;
    private Buttons add;
    private Buttons random;
    private Buttons bad;
    private Sliders cutting;
    private Sliders cooking;
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(960, 640, 1); 
        setPaintOrder (Washing.class,Buttons.class, Sliders.class);
        
        //initialize the widgets
        addOrder = new Buttons(180, 50, "addorderbuttonUP.png", "addorderbuttonDOWN.png", true);
        addObject(addOrder, 100, 580);
        rice = new Buttons(180, 50, "rice1.PNG", "rice2.png", true);
        noodles = new Buttons(180, 50, "noodles1.PNG", "noodles2.png", true);
        chicken = new Buttons(180, 50,"chicken1.PNG", "chicken2.png", true);
        beef = new Buttons(180, 50,"beef1.PNG", "beef2.png", true);
        broccoli = new Buttons(180, 50,"broccoli1.PNG", "broccoli2.png", true);
        bokchoy = new Buttons(180, 50,"bokchoy1.PNG", "bokchoy2.png", true);
        add = new Buttons(180, 50,"addButton1.PNG", "addButton2.png", false);
        random = new Buttons(180, 50,"random1.PNG", "random2.png", false);
        bad = new Buttons (600, 100, "bad1.PNG", "bad2.png",false);
        cutting = new Sliders (5, 15, 5, 0);
        addObject(cutting, 400, 600);
        cooking = new Sliders (3, 20, 10, 1);
        addObject(cooking, 700, 600);
        
        //Plays background music
        GreenfootSound backgroundMusic = new GreenfootSound("backgroundmusic.wav");
        backgroundMusic.playLoop();

        //Creating a list to store order objects
        orderList = new ArrayList<Order>();

        //Setting values so that simulation runs at 60 fps
        framesPerSecond = 60;
        secondsPerFrame = 1.0/framesPerSecond;
        lastFrame = System.nanoTime();
        frames = 1;

        //Value is for displacing the order displays 
        displayPlacement = 120;
        
        //Add kitchen wall
        GreenfootImage image = new GreenfootImage("KitchenWall.png");
        image.scale(getWidth(), getHeight());
        setBackground(image);
        
        //Create chefs
        generateChefs();
        
        //Creates targets
        generateTargets();
        
        //Add counter
        Actor counter = new Counter();
        addObject(counter, 960/2, 640/2);

        
    }

    public void act()
    {
        //Checks for current time of the system
        long current  = System.nanoTime();
        //Checks for how long the last frame was from the current frame in milliseconds
        long elapsed = (current = lastFrame) / 1000000;

         //Checks for when a set amount of time has passed in milliseconds
        if (elapsed > secondsPerFrame * 1000)
        {
            //resets the last frame time value
            lastFrame = current;
            frames ++;
        }

        //After 60 frames, a second has past
        if (frames > 60)
        {
            //reset frames
            frames = 1;
        }

        generateOrder(false, 400);
        
        if (Greenfoot.isKeyDown("q")) //Set Chefs to panic mode
        {
            List<Chefs> chefList = getObjects(Chefs.class);
            for(Chefs c : chefList)
            {
                c.setPanic();
            }
        }

        //if addOrder is clicked, display the option buttons for making your own order
        if (Greenfoot.mouseClicked(addOrder) && !addOrder.getClicked())
        {
            addObject (rice, 290, 555);
            addObject (noodles, 290, 610);
            addObject (chicken, 480, 555);
            addObject (beef, 480, 610);
            addObject (broccoli, 670, 555);
            addObject (bokchoy, 670, 610);
            addObject (add, 860, 555);
            addObject (random,860, 610);
            removeObject(cutting); removeObject(cutting.getHand());
            removeObject(cooking); removeObject(cooking.getHand());
        }
        //unclicking addOrder button removes the list of buttons
        else if(Greenfoot.mouseClicked(addOrder) && addOrder.getClicked())
        {
            removeObject(rice);
            removeObject(noodles);
            removeObject(chicken);
            removeObject(beef);
            removeObject(broccoli);
            removeObject(bokchoy);
            removeObject(add);
            removeObject(random);
            addObject(cutting, 400, 600); addObject(cutting.getHand(), 400, 590);
            addObject(cooking, 700, 600); addObject(cooking.getHand(), 700, 590);
        }

        checkButtons(); //detect which buttons are clicked
        //check if the user has selected 1 of each components
        if (Greenfoot.mouseClicked(add))
        {
            if(noneSelected ==0)
            {
                generateOrder(true, 1);
            }
            //if not display the bad button
            else
            {
                addObject(bad, 480, 580);
            }
        }
        //generate random order if random button is clicked
        if (Greenfoot.mouseClicked(random))
        {
            generateOrder(false, 1);
        }
        
        //removes the bad button
        if (Greenfoot.mouseClicked(bad))
        {
            removeObject(bad);
        }
        
        
        //if sliding bars are adjusted, change the value of the time length accordingly
        if(Greenfoot.mouseDragged(cutting.getHand()))
        {
            List<Chefs> orderList = getObjects(Chefs.class); // array list of chefs
            for(Chefs c : orderList)
            {
                c.setCuttingTime((cutting.getHand().getCurValue())/60);
            }
        }   
        if(Greenfoot.mouseDragged(cooking.getHand()))
        {
            List<Ingredients> orderList = getObjects(Ingredients.class); // array lists of ingredients
            for(Ingredients i : orderList)
            {
                i.setCookingTime((cooking.getHand().getCurValue())/60);

            }
        }
        setPaintOrder(Washing.class, Buttons.class, Sliders.class);
    }

    /**
     * Check which of the 2 buttons of each column is clicked 
     */
    public void checkButtons()
    {
        noneSelected = 0;
        checkButton ("grain", rice, noodles);
        checkButton ("meat", chicken, beef);
        checkButton ("vegetable", broccoli, bokchoy);
    }

    /**
     * Assign the buttons clicked their corresponding ingredients to their order
     */
    public void checkButton(String ingredientType, Buttons button1, Buttons button2)
    {
        String first= "";
        String second = "";
        int ingredient=0;
        //assign them their string names
        if(ingredientType.equals("grain"))
        {
            first = "Rice";
            second = "Noodle";
            ingredient = 2;
        }
        else if(ingredientType.equals("meat"))
        {
            first = "Chicken";
            second = "Beef";
            ingredient = 0;
        }
        else if (ingredientType.equals("vegetable"))
        {
            first = "Broccoli";
            second = "Bok Choy";
            ingredient = 1;
        }
        //disable the other button that is not pressed
        if (button1.getClicked())
        {
            button2.disable();
            ingredients2[ingredient] = first;
        }
        else if (button2.getClicked())
        {
            button1.disable();
            ingredients2[ingredient] = second;
        }
        //both buttons are enabled
        else
        {
            button1.enable();
            button2.enable();
            noneSelected ++; //indicates that there are buttons unselected
        }
    }

    private void generateOrder(boolean instant, int probability)
    {
        //Randomly creates an order
        boolean now = instant;
        int p = probability;
        
        //Randomly adds an order if there is less than 7 orders present
        if (orderTotal < 7 && Greenfoot.getRandomNumber(p) == 0)
        {
            if (now ==false)
            {
                ingredients = new String [3];
                //Chooses randomly which type of ingredient the order will be
                int meatSelection = Greenfoot.getRandomNumber (2);
                int vegetableSelection = Greenfoot.getRandomNumber (2);
                int grainSelection = Greenfoot.getRandomNumber (2);
                if (meatSelection == 0)
                {
                    ingredients [0] = "Beef";
                }
                else if (meatSelection == 1)
                {
                    ingredients [0] = "Chicken";
                }
                if (vegetableSelection == 0)
                {
                    ingredients [1] = "Broccoli";
                }
                else if (vegetableSelection == 1)
                {
                    ingredients [1] = "Bok Choy";
                }
                if (grainSelection == 0)
                {
                    ingredients [2] = "Rice";
                }
                else if (grainSelection == 1)
                {
                    ingredients [2] = "Noodle";
                }
                //Create a new object order and add it to the array list
                orderList.add(new Order(ingredients));
                //Adding the order to the world
                addObject (new Order(ingredients), displayPlacement, 50);
    
                //Increment total orders present in screen by 1
                orderTotal ++;
    
                //Increases order displacement for the next time an order comes in
                displayPlacement += 120;
            }
            else
            {
                ingredients3 = new String [3];
                for(int i = 0; i<3; i++)
                {
                    ingredients3[i] = ingredients2[i];
                }
                //Create a new object order and add it to the array list
                orderList.add(new Order(ingredients3));
                
                //Adding the order to the world
                addObject (new Order(ingredients3), displayPlacement, 50);
    
                //Increment total orders present in screen by 1
                orderTotal ++;
    
                //Increases order displacement for the next time an order comes in
                displayPlacement += 120;
            }
        }
    }
    
    /**
     * Updates the order display by removing the first order and progressbar.
     * Shifts all displays one position to the left
     */
    public void finishedOrder()
    {
        //Shifts all orders back one spot
        displayPlacement -= 120;
        //Reduces number of orders in the screen
        orderTotal --;
        List<Order> oList = getObjects(Order.class);
        for(Order o : oList)
        {
            o.changeLocation();
        }
        //Shifts progress bar back one spot
        List<ProgressBar> barList = getObjects(ProgressBar.class);
        for(ProgressBar b : barList)
        {
            b.changeLocation();
        }

        //Remove the first order and progressbar from the screen
        removeObject(oList.get(0));
        removeObject(orderList.get(0));
        removeObject(barList.get(0));
        orderList.remove(0);
        oList.remove(0);
        barList.remove(0);

    }
    
    /**
     * Gets the Order objetc 
     * 
     * @param num position of the order relative to the display
     * @return Order returns the Order object
     */
    public Order getOrders(int num)
    {
        //returns the order object for the chefs
        return orderList.get(num);
    }

    private void generateTargets()
    {
        //Initial starting points for each Target Class
        int homeOffsetX = 550;
        int homeOffsetY = 462;
        int foodBoxOffsetX = 204;
        int foodBoxOffsetY = 166;
        int cuttingBoardOffsetX = 224;
        int cuttingBoardOffsetY = 462;
        int fryingPanOffsetX = 334;
        int fryingPanOffsetY = 150;
        int serveryWindowOffsetX = 782;
        int serveryWindowOffsetY = 218;
        int riceAndNoodlesOffsetX = 752;
        int riceAndNoodlesOffsetY = 462;
        int foodPickupOffsetX = 860;
        int foodPickupOffsetY = 219;
        //Creates certain number of Instances for each Target Class
        for (int i = 0; i < 2; i++)
        {
            Actor Home = new Home();
            addObject(Home, homeOffsetX, homeOffsetY);
            homeOffsetX += 395;
            homeOffsetY -= 276;
        }
        for (int i = 0; i < 4; i++)
        {
            FoodBox foodBox = new FoodBox();
            addObject(foodBox, foodBoxOffsetX, foodBoxOffsetY);
            foodBoxOffsetX -= 13;
            foodBoxOffsetY += 46;
        }
        for (int i = 0; i < 2; i++)
        {
            CuttingBoard cuttingBoard = new CuttingBoard();
            addObject(cuttingBoard, cuttingBoardOffsetX, cuttingBoardOffsetY);
            cuttingBoardOffsetX += 130;
            cuttingBoardOffsetY += 0;
        }
        for (int i = 0; i < 4; i++)
        {
            FryingPan fryingPan = new FryingPan();
            addObject(fryingPan, fryingPanOffsetX, fryingPanOffsetY);
            fryingPanOffsetX += 106;
            fryingPanOffsetY += 0;
        }
        for (int i = 0; i < 4; i++)
        {
            ServeryWindow serveryWindow = new ServeryWindow();
            addObject(serveryWindow, serveryWindowOffsetX , serveryWindowOffsetY);
            serveryWindowOffsetX += 14;
            serveryWindowOffsetY += 56;
        }
        for (int i = 0; i < 2; i++)
        {
            RiceAndNoodles riceAndNoodles = new RiceAndNoodles();
            addObject(riceAndNoodles, riceAndNoodlesOffsetX, riceAndNoodlesOffsetY);
            riceAndNoodlesOffsetX += 60;
            riceAndNoodlesOffsetY += 0;
        }
        for (int i = 0; i < 4; i++)
        {
            FoodPickup foodPickup = new FoodPickup();
            addObject(foodPickup, foodPickupOffsetX, foodPickupOffsetY);
            foodPickupOffsetX += 16;
            foodPickupOffsetY += 54;
        }
        Dishrack dishrack = new Dishrack();
        addObject(dishrack, 616, 462);
    }

    private void generateChefs()
    {
        Actor mChef = new MeatChef();
        addObject(mChef, 550, 458);

        Actor vChef = new VegetableChef();
        addObject(vChef, 550, 458);

        Actor gChef = new GrainChef();
        addObject(gChef, 550, 458);

        Actor pChef = new PlatingChef();
        addObject(pChef, 550, 458);

        Actor sChef = new ServingChef();
        addObject(sChef, 945, 190);
    }

    /**
     * Get the frame that the world is currently at
     * 
     * @return int the frame the world is at
     */
    public int getFrames ()
    {
        return frames;
    }

}