import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.util.List;
import java.util.ArrayList;
/**
 * 
 * The Chefs class is a Superclass for all the chefs present in the simulation, that controls all the movement, animations and decision making.
 * The Chefs class recieves information from the majority of all the other classes to be relayed to all the other chefs.
 * It also controls the Orders that are created from the World, helping the chefs 'see' the next orders that need to be prepare
 * <p>
 * Contributions by Alexander include:
 * Animations of the chefs
 * Sound effects being played
 * How the chef gets orders
 * Helped fix bugs with movement
 * Created methods checkOrder(),cut(), finishedIngredient(), finishedOrder(), animate(),
 * <p>
 * Contributions by Daniel include:
 * Chef movement
 * Chef interactions with ingredients
 * <p>
 * Contribution by Deston and Justin:
 * Creating ingredients within the class
 * How ingredients interacted with chefs
 * 
 * @author Daniel, Alexander, Justin and Deston
 * @version October 2018
 */
public abstract class Chefs extends Actor
{
    //Target variables for movement
    protected int targetX;
    protected int targetY;
    private String targPoint;
    private String nextPoint;
    private Actor currPoint;
    private int targFoodBx;
    private int targCutBrd;
    private int targFryPan;
    private int targServWin;
    private int targDishrack;
    private int targRiceNoods;
    private int targFoodPickup;
    private int cuttingTime = 5;//how long it takes to cut an ingredient
    private int targetPan = 0;
    private int targetServery = 0;
    private int targetPlate = 0;
    private boolean test = false;
    protected boolean follow = false;
    private int position;

    protected String ingredientSelect;
    protected int type;
    private ProgressBar p;

    //Cut  variables
    private Timer t;
    private boolean timer;
    protected boolean isCutting;

    //Order variables
    private int orderNumber = 0;
    private int pic = 0;
    private Order order;
    private MyWorld world;
    private boolean receivedOrder = false;
    private String [] ingredients = new String [3];

    //Animation array
    private GreenfootImage[][] multi = new GreenfootImage[9][9];

    
    private GreenfootSound meatCutting = new GreenfootSound("meatCutting.wav");
    private GreenfootSound vegCutting = new GreenfootSound("vegCutting.wav");

    //Food variables
    protected Meat ChickenOrBeef;
    protected Vegetables BroccoliOrBokChoy;
    protected boolean placedPlate = true;
    private ArrayList<Order> orderList;

    private Meat chicken = new Meat("chicken");
    private Meat beef = new Meat("beef");
    private Vegetables broccoli = new Vegetables("broccoli");
    private Vegetables bokchoy = new Vegetables("bok choy");

    //Movement variables
    protected int speedX = 2;
    protected int speedY = 2;

    //Idle variables
    protected boolean idle = true;
    private int walker = 0;
    private int direction = 0;
    private int directionLast = 0;

    //Washer variables
    private Washing wash;
    private boolean washCreated = false;
    protected boolean panic = false;

    public Chefs()
    {
        //Set animation images to the 2D array multi
        for (int i = 0; i < 4; i++)
        {
            for (int n = 0;  n < 9; n++) //Moving animations
            {
                multi [i][n] = new GreenfootImage("animation"+ (i) +"" + n + ".png");
                multi[i][n].scale(multi[i][n].getWidth()+32, multi[i][n].getHeight()+25);
            }
        }
        for (int i = 4; i < 8; i++)
        {
            for (int n = 0;  n < 6; n++)//Cutting animations
            {
                multi [i][n] = new GreenfootImage("animation"+ (i) +"" + n + ".png");
                multi[i][n].scale(multi[i][n].getWidth()+32, multi[i][n].getHeight()+25);
            }
        }
    }

    public void addedToWorld (World w)
    {
        //World can now be called using world variable
        world = (MyWorld) getWorld();

    }

    public void checkOrder(int food)
    {
        if (world.getObjects(Order.class).size() > 0 && !receivedOrder && world.getObjects(Order.class).size() > orderNumber)
        {
            //Gets the next Order object that the chef has no completed
            order = world.getOrders(orderNumber);

            //Gets the ingredients for that order
            ingredients = order.getIngredients();
            receivedOrder = true;
            orderNumber ++;

            //Assigns the ingredient that the chef needs to get
            ingredientSelect = ingredients[food];
        }
    }

    public void finishedIngredient()
    {
        //Increments the progressbar when their part of the order is finished
        List <ProgressBar> barList = world.getObjects(ProgressBar.class);
        barList.get(orderNumber-1).resize();
        //Chef can get new order
        receivedOrder = false;

    }

    public void finishedOrder()
    {
        //Removes the order from the world
        world.finishedOrder();
        //Decreases the orderNumber for all chefs by one
        List<Chefs> chefList = world.getObjects(Chefs.class);
        for(Chefs c : chefList)
        {
            c.lowerOrder();
        }
    }

    protected void lowerOrder()
    {
        //Chefs orderNumber lowers by one when an entire order is finished
        orderNumber --;
    }

    /**
     * returns cutting time 
     */
    public int getCuttingTime()
    {
        return cuttingTime;
    }

    /**
     * resets cutting time 
     */
    public void setCuttingTime(int time)
    {
        cuttingTime = time;
    }

    public void cut()
    {
        //Every two frames, method is played
        if (isCutting && world.getFrames() % 2 == 0)
        {
            if (!timer)
            {
                //Creates timer for cutting duration
                t = new Timer (cuttingTime,0,255,77,10,30);
                world.addObject(t,getX()-30,450);
                timer = true;
                if (type == 0)//Play meat cutting sound
                {
                    meatCutting.play();
                }
                else//Play vegetable cutting sound
                {
                    vegCutting.play();
                }
            }
            //Play cutting down animation
            animate(6);
            if (t.isDone())//When the chef finishes cutting
            {
                //Removes timer
                isCutting = false;
                world.removeObject(t);
                world.getObjects(CuttingBoard.class).get(targCutBrd).setFreeTrue();
                timer = false;

                //Stop cutting sounds
                meatCutting.stop();
                vegCutting.stop();

                //Pick up food
                Ingredients food = (Ingredients)getOneObjectAtOffset(30, 30, Ingredients.class);
                if(food!= null)
                {
                    food.PickUp();
                }
            }
        }
    }

    private void animate (int direction)
    {
        //If animation is moving, reset it to second animation image when it reaches the last picture of the animation
        if (direction < 5 && pic > 7)
        {
            pic = 1;
        }
        //if animation is cutting, reset it to first animation image when it reaches the last picture of the animation
        else if (pic > 4 && direction > 4)
        {
            pic = 0;
        }
        if (direction < 5)//For moving
        {
            //Every 10 frames, increment animation picture by 1
            if (world.getFrames() % 10 == 0)
            {
                setImage (multi[direction][pic]);
                pic ++;
            }
        }    
        //For cutting
        else if (world.getFrames() % 6  == 0)
        {
            //Every 6 frames, increment animation picture by one
            setImage(multi[direction][pic]);
            pic++;
        }
    }

    private void moveTowardTargetLocation()
    {
        //Move towards the target
        if(world.getObjects(Order.class).size() != 0 && idle == false) //&& world.getFrames() % 2 == 0 && !panic)
        {
            if(targetX < this.getX()) //Move left
            {
                setLocation(this.getX() - speedX, this.getY());  
                animate(2);
            }
            if(targetX > this.getX()) //Move right
            {
                setLocation(this.getX() + speedX, this.getY());
                animate(3);
            }
            if(targetY < this.getY()) //Move Up
            {
                setLocation(this.getX(), this.getY() - speedY);
                animate(0);
            }
            if(targetY > this.getY()) //Move Down
            {
                setLocation(this.getX(), this.getY() + speedY);
                animate(1);
            }
        }
    }

    private void setTargets(String chefType)
    {
        if(chefType == "meat") //Checks the chef type
        {
            if(ingredientSelect  == "Chicken")
            {
                targFoodBx = 0; //Sets  an int corresponding to the Foodbox Array Index
            }
            else if(ingredientSelect  == "Beef")
            {
                targFoodBx = 1; //Sets  an int corresponding to the Foodbox Array Index
            }
        }
        if(chefType == "vegetable") //Checks the chef type
        {
            if(ingredientSelect  == "Broccoli")
            {
                targFoodBx = 2; //Sets  an int corresponding to the Foodbox Array Index
            }
            else if(ingredientSelect  == "Bok Choy")
            {
                targFoodBx = 3; //Sets  an int corresponding to the Foodbox Array Index
            }
        }
        if(chefType == "grain") //Checks the chef type
        {
            if(ingredientSelect  == "Rice")
            {
                targRiceNoods = 0; //Sets  an int corresponding to the Rice Cooker Array Index
            }
            else if(ingredientSelect  == "Noodle")
            {
                targRiceNoods = 1; //Sets  an int corresponding to the Noodle Cooker Array Index
            }
        }
    }

    private void getTargetLocation(String desPoint, String chefType)
    {
        //targPoint is what chef is moving towards
        targPoint = desPoint;
        //List of objects to refer later
        List <FryingPan> panList = world.getObjects(FryingPan.class);
        List <Plate> plateList = world.getObjects(Plate.class);
        List <ServeryWindow> serveList = world.getObjects(ServeryWindow.class);
        if(targPoint == "Home1") //Checks the desired location
        {
            currPoint = world.getObjects(Home.class).get(0);
        }
        if(targPoint == "Home2") //Checks the desired location
        {
            currPoint = world.getObjects(Home.class).get(1);
        }
        if(targPoint == "FoodBox") //Checks the desired location
        {
            currPoint = world.getObjects(FoodBox.class).get(targFoodBx);
        }
        if(targPoint == "CuttingBoard1") //Checks the desired location
        {
            currPoint = world.getObjects(CuttingBoard.class).get(0);
        }
        if(targPoint == "CuttingBoard2") //Checks the desired location
        {
            currPoint = world.getObjects(CuttingBoard.class).get(1);
        }
        if(targPoint == "FryingPan") //Checks the desired location
        {
            test = true;
            position = 0;
            //Checks for first frying pan that is free
            for(FryingPan p: panList)
            {
                if (test)
                {
                    position ++;
                }
                if(p.getFree() == true && test)
                {
                    test = false;
                    targetPan = position - 1;
                }
            }
            if (test)
            {
                //If no pans are available, chef will return to home 
                currPoint = world.getObjects(Home.class).get(0);
            }
            else 
            {
                //Chef will now target the first free frying pan
                currPoint = world.getObjects(FryingPan.class).get(targetPan);
            }
        }   
        if (targPoint == "FryingPan1" || targPoint == "FryingPan2" || targPoint == "FryingPan3" || targPoint == "FryingPan4" ) //Checks the desired location
        {
            currPoint = world.getObjects(FryingPan.class).get(targetPan);
        }
        if (targPoint == "ServeryWindow") //Checks the desired location
        {
            if (chefType == "grain")//only if it is the grain chef
            {
                //Chef will now target the ServeryWindow in order
                currPoint = world.getObjects(ServeryWindow.class).get(targetServery);
            }
            if(chefType == "plater") //only if it is the plater chef
            {
                //checks for the first plate that is complete
                test = true;
                for(Plate p: plateList)
                {
                    if(p.checkIfComplete() == false && test)
                    {
                        test = false;
                        targetServery = p.getPosition();
                    }
                }
                //Chef will now target the ServeryWindow in order of incomplete dishes
                currPoint = world.getObjects(ServeryWindow.class).get(targetServery);
            }
        }
        if (targPoint == "Dishrack") //Checks the desired location
        {
            currPoint = world.getObjects(Dishrack.class).get(targDishrack);
        }
        if (targPoint == "RiceAndNoodles") //Checks the desired location
        {
            currPoint = world.getObjects(RiceAndNoodles.class).get(targRiceNoods);
        }
        if (targPoint == "FoodPickup") //Checks the desired location
        {
            currPoint = world.getObjects(FoodPickup.class).get(targetServery);
        }
        if (chefType != "plater" || idle == false) //Checks the desired location
        {
            targetX = currPoint.getX(); // set x and y coordinates of the desired location
            targetY = currPoint.getY();
        }
        if (!isCutting && !panic) // if the chef isn't doing anything
        {
            moveTowardTargetLocation();
        }
    }

    protected void prepare(String chefType, String meatVegGrain)
    {
        setTargets(chefType);
        //Initialize variables for the closest targets 
        Actor Home = getOneObjectAtOffset(0, 0, Home.class);
        Actor FoodBox = getOneObjectAtOffset(0, 0, FoodBox.class);
        Actor CuttingBoard = getOneObjectAtOffset(0, 0, CuttingBoard.class);
        Actor FryingPan = getOneObjectAtOffset(0, 0, FryingPan.class);
        Actor ServeryWindow = getOneObjectAtOffset(0, 0, ServeryWindow.class);
        Actor Dishrack = getOneObjectAtOffset(0, 0, Dishrack.class);
        Actor RiceAndNoodles = getOneObjectAtOffset(0, 0, RiceAndNoodles.class);
        Actor FoodPickup = getOneObjectAtOffset(0, 0, FoodPickup.class);
        Ingredients food = (Ingredients)getOneObjectAtOffset(10, 10, Ingredients.class);
        Plate plate = (Plate)getOneObjectAtOffset(0, 0, Plate.class);
        if (chefType == "plater")
        {
            plate = (Plate)getOneObjectAtOffset(40, 0, Plate.class); //Change plate variable for platter so that it has an easier time at placing the food on plates
        }
        //Initialize list of objects to be referred to later
        List <Ingredients> ingredientsList = world.getObjects(Ingredients.class);
        List <Plate> plateList = world.getObjects(Plate.class);
        List <ServeryWindow> serveList = world.getObjects(ServeryWindow.class); 
        if(chefType == "plater" && idle && world.getObjects(Order.class).size() != 0 ) //When an order is present
        {
            idleWalk();
            test = true;
            follow = false;
            if(world.getObjects(Order.class).size() != 0)
            {
                //Checks for the first ingredient that is cooked
                for(Ingredients i : ingredientsList)
                {
                    if(i.getCooked() == true && test) //Checks if food is cooked
                    {
                        idle = false;
                        test = false;
                        if(i.getX() >= 300 && i.getX() <= 358) //Set location to frying pan based on cooked food
                        {
                            nextPoint = "FryingPan1";
                            targetPan = 0;
                        }
                        if(i.getX() >= 415 && i.getX() <= 463) //Set location to frying pan based on cooked food
                        {
                            nextPoint = "FryingPan2";
                            targetPan = 1;
                        }
                        if(i.getX() >= 510 && i.getX() <= 568) //Set location to frying pan based on cooked food
                        {
                            nextPoint = "FryingPan3";
                            targetPan = 2;
                        }
                        if(i.getX() >= 625 && i.getX() <= 673) //Set location to frying pan based on cooked food
                        {
                            nextPoint = "FryingPan4";
                            targetPan = 3;
                        }
                    }
                }
            }
        }
        if (Home != null) //If chef is at home target
        {
            if (food != null && chefType == "meat" || food != null && chefType == "vegetable" ) //If chef is carrying food
            {
                nextPoint = "FryingPan";
            }
            else if(chefType == "meat" || chefType == "vegetable") //If chef is not carrying food
            {
                nextPoint = "FoodBox";
            }

            if(chefType == "grain" && world.getObjects(Order.class).size() != 0  && orderNumber < world.getObjects(Order.class).size()  )
            {
                test = true;
                position = 0;
                setImage (multi[1][0]); //Set image to face down
                //Checks for a spot with no dishes
                for(ServeryWindow s: serveList)
                {
                    if (test)
                    {
                        position ++;
                    }
                    if(s.getPlate() == false && test)// Checks for free spot
                    {
                        test = false;
                        targetServery = position - 1; //Sets the target for the ServeryWindow
                        nextPoint = "Dishrack";
                        washCreated = true;
                        world.removeObject (wash);
                    }
                }

            }
            else if (nextPoint != "Dishrack" && !washCreated && chefType == "grain" && world.getObjects(Order.class).size() != 0) //While grain chef is doing nothing
            {
                world.setPaintOrder(Washing.class, Buttons.class, Sliders.class);
                setImage(multi[1][0]); //Set image to face down
                washCreated = true;
                wash = new Washing(); //Creates the washing animation
                world.addObject (wash,getX()-7, getY()+40);
                nextPoint = "";
            }
            else if (chefType == "grain") //Faces down if nothing happens
            {
                world.setPaintOrder(Washing.class, Buttons.class, Sliders.class);
                setImage(multi[1][0]);
                nextPoint = "";
            }
            if(chefType == "server") //Checks for chef type
            {
                test = true;
                position = 0;
                if(world.getObjects(Order.class).size() == 0)
                {
                    getImage().setTransparency(0); //Chef is invisible until it has to pick up a plate
                    nextPoint = "Home2";
                }
                if(world.getObjects(Order.class).size() != 0)
                {
                    getImage().setTransparency(0);
                    for(Plate p : plateList) // Checks for a plate that is complete
                    {
                        if (test)
                        {
                            position ++;
                        }
                        if(p.checkIfComplete() == true && test) //When it finds a plate that is complete, set target to it
                        {
                            getImage().setTransparency(250);
                            test = false;                         
                            targetPlate = position - 1;
                            targetServery = p.getPosition();
                            nextPoint = "FoodPickup"; 
                        }
                    }
                }
            }
        }
        if (FoodBox != null) //Food boxes to pick up uncooked ingredients
        {
            if(food == null && (chefType == "meat" || chefType == "vegetable"))
            {
                test = true;
                if (meatVegGrain == "chicken") //Creates an uncooked chicken
                {
                    // add chicken to world
                    chicken = new Meat("chicken");
                    getWorld().addObject(chicken, 210, 190);
                }
                else if (meatVegGrain == "beef") //Creates an uncooked beef
                {
                    // add beef to world
                    beef = new Meat("beef");
                    getWorld().addObject(beef, 200, 215);
                }
                else if (meatVegGrain == "broccoli") //Creates an uncooked broccoli
                {
                    // add broccoli to world
                    broccoli = new Vegetables("broccoli");
                    getWorld().addObject(broccoli, 190, 260);
                }
                else if (meatVegGrain == "bok choy") //Creates an uncooked chicken
                {
                    // add bokchoy to world
                    bokchoy = new Vegetables("bok choy");
                    getWorld().addObject(bokchoy, 180, 320);
                }
                //If the food is from the meat class, display below the chef
                if(meatVegGrain == "chicken" || meatVegGrain == "beef")
                {
                    world.setPaintOrder(Washing.class, Buttons.class,  Sliders.class, Meat.class);
                    nextPoint = "CuttingBoard2";
                }
                //If the food is from the vegetable class, display below the chef
                else
                {
                    world.setPaintOrder(Washing.class, Buttons.class,  Sliders.class,  Vegetables.class);
                    nextPoint = "CuttingBoard1";
                }
                Greenfoot.playSound("openChest.wav"); //Plays chest opening sound 
            }
        }
        if (CuttingBoard != null )
        {
            if(chefType == "meat" && !food.getChopped() || chefType == "vegetable" && !food.getChopped()) //Starts Cutting
            {
                setLocation (getX(), getY()-3); //Displaces chef upwards
                isCutting = true;
                nextPoint = "FryingPan";
                if(food!= null) //Plays food animation
                {
                    food.Process(0);
                }
            }
        }
        if (FryingPan != null && FryingPan == world.getObjects(FryingPan.class).get(targetPan))
        {
            if(chefType == "meat" || chefType == "vegetable" )
            {
                if (!test) //Chef finishes their portion of the order
                {
                    finishedIngredient();
                    test = true;
                }
                world.getObjects(FryingPan.class).get(targetPan).setFreeFalse(); //Sets frying pan to in use
                if(world.getObjects(Order.class).size() == 0) //Goes back home 
                {
                    nextPoint = "Home";
                }
                else if(world.getObjects(Order.class).size() != 0) //Picks up more food
                {
                    nextPoint = "FoodBox";
                }
                if(food!= null) //Plays cooking animation for food
                {
                    food.Process(targetPan);
                }
            }
            if(chefType == "plater") //Plater touches the frying pan
            {
                follow = true;
                world.getObjects(FryingPan.class).get(targetPan).setFreeTrue(); //Set Target Pan to true
                test = true;
                nextPoint = "ServeryWindow"; //Will start to move to the ServeryWindow now
                food.PickUp(); //Picks up cooked food
            }
        }
        if (Dishrack != null) //Grain chef picks up a plate
        {
            if(chefType == "grain" && nextPoint != "Home1")
            {
                nextPoint = "RiceAndNoodles"; //Goes to get noodles or rice
                test = true;
                if(plate == null )
                {
                    plate = new Plate();
                    getWorld().addObject(plate, 620, 470);
                    placedPlate = false;
                }
            }
        }
        if (RiceAndNoodles != null && RiceAndNoodles == world.getObjects(RiceAndNoodles.class).get(targRiceNoods) && nextPoint != "Home1")
        {
            if(chefType == "grain")
            {
                if (ingredientSelect == "Rice") //Grain chef gets rice
                {
                    getWorld().getObjects(Counter.class).get(0).getRice();
                    plate.setFood("rice"); //Sets rice on plate
                }
                else 
                {
                    getWorld().getObjects(Counter.class).get(0).getNoodles(); //Grain chef gets noodles
                    plate.setFood("noodle"); //Sets noodles on plate
                }
                nextPoint = "ServeryWindow"; //Goes to ServeryWindow
            }
        }
        if (ServeryWindow != null) //Chef touches ServeryWindow
        {
            if(world.getObjects(Order.class).size() == 0)
            {
                if(chefType == "grain") //Returns to home
                {
                    nextPoint = "Home1";
                }
                if(chefType == "server") //Returns to home
                {
                    nextPoint = "Home2";
                }
            }
            else if(world.getObjects(Order.class).size() != 0)
            {
                if(chefType == "grain" && test && ServeryWindow == world.getObjects(ServeryWindow.class).get(targetServery)) //Touches ServeryWindow with plate
                {
                    test = false;
                    washCreated = false;
                    finishedIngredient(); //Finishes their portion of the order
                    world.getObjects(ServeryWindow.class).get(targetServery).setPlateTrue(); //Set ServeryWindow plate to true
                    plate.setPosition (targetServery);//Places plate down
                    nextPoint = "Home1"; //Returns to home
                    plate.placePlate();
                }
                if(chefType == "plater" && !idle && !test && ServeryWindow == world.getObjects(ServeryWindow.class).get(targetServery)) //Plater chef touches Servery
                {
                    test = true;
                    follow = false;
                    idle = true;//Returns to idle position
                    if(food != null && plate != null) //Places food on plate
                    {
                        plate.setFood(food.getName());
                        getWorld().removeObject(food);
                    }
                }
            }
        }
        if (FoodPickup != null && FoodPickup == world.getObjects(FoodPickup.class).get(targetServery)) //Serving chef touches food
        {
            if(!test)
            {
                test = true;
                world.getObjects(ServeryWindow.class).get(targetServery).setPlateFalse();//Set ServeryWindow so that it does not have a plate
                world.removeObject(world.getObjects(Plate.class).get(targetPlate)); //Removes the dish
                nextPoint = "Home2"; //Goes back home
                finishedOrder();
            }
        }
        if (nextPoint != "" || chefType == "plater" && !idle )
        {
            getTargetLocation(nextPoint, chefType);
        }
    }

    protected void idleWalk()
    {
        walker ++;
        if (walker == 30) //Change walking direction everytime walker equals 30
        {
            directionLast = direction; //Keep track of last direction if chef stands still
            direction = Greenfoot.getRandomNumber(5); //Could be up, down, left, right or nothing
            walker = 0; //Resets walker 
        }
        if (direction == 4) //Not moving
        {
            setImage (multi[directionLast][0]);//Set image so that it remains still in the same direction 
        }
        if( direction == 3 && getX() < 750) //Walk right if the chef isn't too far to the right
        {
            setLocation(getX() + speedX, getY());
            animate(3);
        }
        else if (direction == 3)//Reverse direction if chef is too far to the right
        {
            direction = 2;
        }
        if (direction == 2 && getX() > 204)//Walk left if the chef isn't too far to the left
        {
            setLocation(getX() - speedX, getY());
            animate(2);
        }
        else if (direction == 2)//Reverse direction if chef is too far to the left
        {
            direction = 3;
        }
        if(direction == 1 && getY() < 462)//Walk down if the chef isn't too far downwards
        {
            setLocation(getX(), getY() + speedY);
            animate(1);
        }
        else if (direction == 1)//Reverse direction if chef is too far downwards
        {
            direction = 0;
        }
        if(direction == 0 && getY() > 150)//Walk up if the chef isn't too far upwards
        {
            setLocation(getX(), getY() - speedY);
            animate(0);
        }
        else if (direction == 0)//Reverse direction if chef is too far upwards
        {
            direction = 1;
        }
    }

    /**
     * Makes chef panic, causing the chefs to move randomly at a greater spee
     */
    public void setPanic()
    {
        panic = true; //Chefs will start to panic
        speedX = 5; //Move faster when panicked 
        speedY = 5;
    }
}
