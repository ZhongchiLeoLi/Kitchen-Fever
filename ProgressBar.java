import greenfoot.*;  
/**
 * @author Alexander Yee  
 * @version October 2018
 */
public class ProgressBar extends Actor
{
    private double resize; // changes the size of the bar during countdown

    private double width;
    private double height;
    private int r;
    private int g;
    private int b;

    private GreenfootImage image;
    private GreenfootImage text;

    public ProgressBar (double width, double height)
    {
        this.width = width;
        this.height = height;
        r = 100;
        g = 200;
        b = 250;

        //Displays the ability bar when the object is added to the world
        setUp();
    } 

    private void setUp()
    {
        //Setup the border 
        image = new  GreenfootImage((int)width , (int)height );
        setImage(image);

        //Setup the main bar 
        image.setColor(new Color (r, g, b, 250));
        image.fillRect(1, 1 ,1,(int)height);  

        //Setup the shadow bar
        image.setColor(Color.GRAY);
        image.fillRect(1,1, 100,(int)height);
    }

    /**
     * Adds one level of progress    
     */
    public void resize()
    {
        resize ++;

        //Resize the main bar   
        image.setColor(new Color (r, g, b, 250));
        image.fillRect(1, 1 ,(int)(resize * (width/3)),(int)height);  

        //Resize the shadow bar
        image.setColor(Color.GRAY);
        image.fillRect((int)(resize * (width/3)) ,1, (int) (width - (resize * (width/3))),(int)height);

    }

    /**
     * Shift the par to the left
     */
    public void changeLocation()
    {
        setLocation (getX() - 120, getY());
    }

}
