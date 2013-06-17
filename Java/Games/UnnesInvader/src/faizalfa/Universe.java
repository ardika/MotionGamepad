package faizalfa;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Universe extends JPanel implements ActionListener {

	private static final long serialVersionUID = -6539815249586621515L;
	
	private Timer timer;
	private Spaceship spaceship;
    private ArrayList<Alien> aliens;
    private ArrayList<Missile> missiles;
    private ArrayList<Laser> lasers;

    private boolean gameOn;
    private int width, height;
    
    private Random chance = new Random();
    
	public Universe(){
		addKeyListener(new Adapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		/*setSize(100, 200);
		height = getHeight();
		width = getWidth();*/
		gameOn = true;
		
		spaceship = new Spaceship();
		
		initAliens();
		
		timer = new Timer(100, this);
		timer.start();
	}
	
	public void initAliens() {
		aliens = new ArrayList<Alien>();
		
		for (int col=0; col<11; col++) {//32
			aliens.add(new Alien("squid", 5+45*col, 1));
		}
		
		for (int row=1; row<3; row++) {//33
			for (int col=0; col<11; col++) {
				aliens.add(new Alien("crab", 5+45*col, 1+40*row));
			}
		}
		
		for (int row=3; row<5; row++) {
			for (int col=0; col<11; col++) {
				aliens.add(new Alien("octo", 1+45*col, 1+40*row));
			}
		}
	}

	public void paint(Graphics g){
		super.paint(g);
		
		if(gameOn){
			drawSpaceship(g);
			drawAlien(g);
			drawMissile(g);
			drawLaser(g);
			
			g.setColor(Color.WHITE);
            g.drawString("Aliens left: " + aliens.size(), 5, 15);
		}
		
		else if(gameOn==false){
			String msg = "Game Over";

            g.setColor(Color.white);
            g.drawString(msg,200,300);
		}

			Toolkit.getDefaultToolkit().sync();
			g.dispose();
	}
	
	public void drawSpaceship(Graphics g){
		g.drawImage(spaceship.getImage(), spaceship.getX(), spaceship.getY(), this);
	}
	
	public void drawMissile(Graphics g){
		missiles = spaceship.getMissiles();

		for (int i = 0; i < missiles.size(); i++ ) {
			Missile m = (Missile) missiles.get(i);
			if(m.isAlive()){
				g.drawImage(m.getImage(), m.getX(), m.getY(), this);
				m.move();
			}
			else missiles.remove(i);
		}
	}
	
	public void drawAlien(Graphics g){
		for (int i = 0; i < aliens.size(); i++) {
            Alien a = (Alien)aliens.get(i);
            if (a.isAlive()){
            	g.drawImage(a.getImage(), a.getX(), a.getY(), this);
            	a.move();
            	if(a.getX() < 0){
            		a.setDirection(true);
            		moveAliensDown();
            	}
            	if(a.getX() > 650){
            		a.setDirection(false);
            		moveAliensDown();
            	}
            	if(a.getLuck() == chance.nextInt(777)){
            		a.fire();
            	}
            }
            else aliens.remove(i);
        }
	}
	
	public void drawLaser(Graphics g){
		for (int i = 0; i < aliens.size(); i++) {
			Alien a = (Alien) aliens.get(i);
			lasers = a.getLasers();
			for (int j = 0; j < lasers.size(); j++ ) {
				Laser l = (Laser) lasers.get(j);
				if(l.isAlive()){
					g.drawImage(l.getImage(), l.getX(), l.getY(), this);
					l.move();
				}
				else lasers.remove(j);
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(aliens.size()==0){
			gameOn = false;
		}
		spaceship.move();
        checkCollisions();
        repaint();
	}

	public void moveAliensDown(){
		for (int i = 0; i < aliens.size(); i++) {
            Alien a = (Alien)aliens.get(i);
			a.setY(a.getY() + 15);
		}
	}
	
	public void checkCollisions() {

        Rectangle spaceshipArea = spaceship.getBounds();

        for (int j = 0; j<aliens.size(); j++) {
            Alien a = (Alien) aliens.get(j);
            Rectangle alienArea = a.getBounds();

            if (spaceshipArea.intersects(alienArea)) {
                spaceship.setAlive(false);
                a.setAlive(false);
                gameOn = false;
//                System.exit(0);
            }
        }

        for (int i = 0; i < missiles.size(); i++) {
            Missile m = (Missile) missiles.get(i);

            Rectangle missileArea = m.getBounds();

            for (int j = 0; j<aliens.size(); j++) {
                Alien a = (Alien) aliens.get(j);
                Rectangle alienArea = a.getBounds();

                if (missileArea.intersects(alienArea)) {
                    m.setAlive(false);
                    a.setAlive(false);
                }
            }
        }
    }
	
	private class Adapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            spaceship.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            spaceship.keyPressed(e);
        }
    }
}
