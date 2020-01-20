
import greenfoot.*;  
/**
 * Acts as a countdown from a desired time to zero seconds.
 * Designed to show when something can be used again. 
 * Multiple instances can be created as long as the trigger keys are different. 
 * 
 * @author Alexander Yee with help from Jordan Cohen 
 * @version October 2018
 */
public class Timer extends Actor
{
    private long lastFrame;
    private double timePerIncrement; //The time in milliseconds that passes before the bar decreases by one pixel

    private boolean animation;
    private double cooldown;
    private int resize; // changes the size of the bar during countdown
    private int repitition;
    
    //Variables that require constructors
    private double time; //Time is in seconds
    private double originalTime; 
    private String key;
    private double width;
    private double height;
    private int r;
    private int g;
    private int b;
    
    private GreenfootImage image;
    private GreenfootImage text;
    
    /**
     * Creates an ability bar with full customization, allowing the user to change countdown time, colour and size.
     * 
     * @param time The countdown time of the ability in seconds. Recommended to use less than 40 seconds
     * @param key The keyboard input that will begin the countdown. Must be one character or a single input 
     * @param r The red component of colour. Ranges from 0 - 255
     * @param g The green component of colour. Ranges from 0 - 255
     * @param b The blue component of colour. Ranges from 0 - 255
     * @param width The width of the ability bar in pixels. Recommended to use between 1 and 100 
     * @param height The height of the ability bar in pixels. Recommended to use between 30 and 100 
     */
    public Timer (double time,  int r, int g, int b, double width, double height)
    {
        //Assign values to variables
        lastFrame = System.nanoTime();
        
        animation = false;
        repitition = 0;
        cooldown = 0;
        resize = 0;
        
        this.key = key;
        this.height = height;
        this.width = width;
        this.time = time;
        this.r = r;
        this.g = g;
        this.b = b;
        
        //Displays the ability bar when the object is added to the world
        countdown();
    }
    
    /**
     * Counts in real time and checks when to decrease the ability bar height. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment. 
     */
    public void act() 
    {
        //Determines the real time in milliseconds that passes before the bar decreases by one pixel
        timePerIncrement = (1/(height/time))*1000; 
        
        //Determine how much time has passed since the last act
        long current = System.nanoTime();
        // Find elapsed time - in milliseconds (ms)
        long elapsed = (current - lastFrame) / 1000000;  
     
        if (elapsed + (height/20) > timePerIncrement ) // elapsed is given a small range 
        {
            lastFrame = current;
            
            countdown();
        }
    }    
    private void countdown()
    {
        //Displace the bars by 1 pixel
        resize += 1;
         
        
        //Setup the main bar 
        image = new  GreenfootImage((int)width + 2, (int)height + 2);
        setImage(image);
        image.setColor(new Color (r, g, b, 250));
        image.fillRect(1, resize ,(int)width,(int) height);  
        
        //Setup the shadow bar
        image.setColor(Color.GRAY);
        image.fillRect(1,1, (int)width,resize);
        
        //Setup the border
        image.setColor(Color.BLACK);        
        image.drawRect (0,0,(int) width + 1, (int) height + 1); 
    
    }
    /**
     * Indicated when timer is finished
     * @returns True if it is done 
     */
    public boolean isDone()
    {
        if (resize > height)
        {
            return true;
        }
        return false;
    }
}