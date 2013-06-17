package UnnesInvader;

public class Laser extends MovingObject{
	
	private final int range = 650;
	
	public Laser(int x, int y){
		setImage("Resources/z.gif");
		speed = 10;
		this.x = x;
		this.y = y;
	}
	
	public void move(){
    	y += speed;
    	if(y >= range)
    		alive = false;
    }
}
