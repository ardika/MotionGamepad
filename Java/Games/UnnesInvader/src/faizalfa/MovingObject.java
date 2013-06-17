package faizalfa;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class MovingObject {
	protected int x, y, width, height, speed;
	protected boolean alive = true, explode = false;
	protected Image image;
	protected ImageIcon icon;
	
	MovingObject(){
	}
	
	public void setImage(String imageSource){
		icon = new ImageIcon(this.getClass().getResource(imageSource));
		image = icon.getImage();
		width = image.getWidth(null);
		height = image.getHeight(null);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int y){
		this.y = y; 
	}
	
	public void setX(int x){
		this.x = x; 
	}
	
	public Image getImage(){
		return image;
	}
	
	public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
