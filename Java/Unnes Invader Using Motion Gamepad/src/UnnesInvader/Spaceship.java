package UnnesInvader;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import MotionGamepad.GamepadInterface;


public class Spaceship extends MovingObject{
	private int dx, dy;
	private ArrayList<Missile> missiles;
	private GamepadInterface SpaceshipController;
	
	public Spaceship(GamepadInterface Controller) {
		setImage("Resources/assaultship.png");
		SpaceshipController = Controller;
		
		missiles = new ArrayList<Missile>();
		speed = 15;
		x = 265;
		y = 380;
	}
	
	public void move(){
		x += -(SpaceshipController.GetPitchElevation());
		y += -(SpaceshipController.GetRollElevation());
		
		if (SpaceshipController.GetButtonAState())
			fire();
		if(x<1) x = 1;
		else if(x>630) x = 630;
		
		if(y<1) y = 1;
		else if(y>380) y = 380;
	}
		
	public ArrayList<Missile> getMissiles() {
        return missiles;
    }
	
	public void fire() {
		if(missiles.size()==0)
			missiles.add(new Missile(x + width/2-1, y));
	}
	
	public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_SPACE) {
            fire();
        }
        
        if (key == KeyEvent.VK_LEFT) {
        	dx = -speed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = speed;
        }

        if (key == KeyEvent.VK_UP) {
        	dy = -speed;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = speed;
        }
    }
	
	public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
}