package UnnesInvader;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import MotionGamepad.*;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 4432234790107287020L;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {
		setSize(new Dimension(700, 500));
		setTitle("Unnes Invader");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		initialize();
	}

	private void initialize() {
		add(new Universe());
	}

}
