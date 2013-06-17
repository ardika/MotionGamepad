package faizalfa;

public class Missile extends MovingObject{
	
	private final int range = -8;
	
	public Missile(int x, int y){
		setImage("Resources/missile.png");
		speed = 10;
		this.x = x;
		this.y = y;
	}

    public void move(){
    	y -= speed;
    	if(y <= range)
    		alive = false;
    }
}