package UnnesInvader;

import java.util.ArrayList;
import java.util.Random;

public class Alien extends MovingObject{
	
	private static boolean goRight;
	private ArrayList<Laser> lasers;
	private int luck;
	private Random randomLuck = new Random();
	private String name;
	
	public Alien(String alienName, int x, int y){
		lasers = new ArrayList<Laser>();
		setImage("Resources/"+alienName+".gif");
		speed = 1;
		luck = randomLuck.nextInt(777);
		name = alienName;
		this.x = x;
		this.y = y;
	}
		
	public ArrayList<Laser> getLasers(){
		return lasers;
	}
	
	public int getLuck(){
		return luck;
	}
	
	public String getName(){
		return name;
	}
	
	public void setDirection(boolean direction){
		goRight = direction;
	}
	
	public void move() {
        if(goRight)
        	x += speed;
        else
        	x -= speed;
    }
	
	public void fire(){
		lasers.add(new Laser(x + width/2, y));
	}
}
