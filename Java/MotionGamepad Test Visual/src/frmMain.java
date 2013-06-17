import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import javax.swing.JPanel;

import MotionGamepad.*;

public class frmMain {

	private JFrame frame;
	private GamepadInterface Pad = new GamepadInterface();
	JScrollBar scrollBarPitch = new JScrollBar();
	JScrollBar scrollBarRoll = new JScrollBar();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
		
				try {
					frmMain window = new frmMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public frmMain() {
		initialize();
		Pad.ConnectGamepad();
		Pad.BeginCalibrating();
		Pad.GamepadOn();
		(new Thread(new Reader())).start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		scrollBarRoll.setBounds(34, 39, 17, 212);
		frame.getContentPane().add(scrollBarRoll);
		
		
		scrollBarPitch.setOrientation(JScrollBar.HORIZONTAL);
		scrollBarPitch.setBounds(85, 39, 271, 17);
		frame.getContentPane().add(scrollBarPitch);
		
		JLabel lblRoll = new JLabel("Roll");
		lblRoll.setBounds(34, 11, 46, 14);
		frame.getContentPane().add(lblRoll);
		
		JLabel lblPitch = new JLabel("Pitch");
		lblPitch.setBounds(207, 11, 46, 14);
		frame.getContentPane().add(lblPitch);
		
		JPanel pnlYaw = new JPanel();
		pnlYaw.setBounds(85, 88, 271, 163);
		frame.getContentPane().add(pnlYaw);
	}
	
	private class Reader implements Runnable 
    {
        public void run ()
        {
            //while (Pad.IsGamepadConnected())
            //{
            	scrollBarPitch.setValue(scrollBarPitch.getMaximum() * (Pad.GetPitchElevation() / 255));
            	scrollBarPitch.setValue(scrollBarPitch.getMaximum() * (Pad.GetPitchElevation() / 255));
            //}       
        }
    }
}
