import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;


public class frmMain extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmMain frame = new frmMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public frmMain() {
		final SerialPortSimpleInterface _serial = new SerialPortSimpleInterface();
		
		// membuat vektor untuk ittem combobox
		Vector ComboxItem = new Vector();
		// mengambil daftar com serial yang terdeteksi
		ArrayList<String> PortNames = _serial.GetPortNames();
		// menambahkan semuana bke vekor
		ComboxItem.addAll(_serial.GetPortNames());
		// membuat permodelannyya
		final DefaultComboBoxModel ComboxModel = new DefaultComboBoxModel(ComboxItem);
		final JComboBox cmbPortNames = new JComboBox(ComboxModel);
		
		cmbPortNames.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
		
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		cmbPortNames.setBounds(25, 11, 89, 20);
		contentPane.add(cmbPortNames);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					_serial.OpenPort(cmbPortNames.getSelectedItem().toString());
					
				}
				catch (Exception Ex) {
					
				}
			}
		});
		btnNewButton.setBounds(124, 10, 142, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (_serial.IsConnected())
					_serial.Write("V1000B1000E");
			}
		});
		btnNewButton_1.setBounds(134, 44, 89, 23);
		contentPane.add(btnNewButton_1);
	}
}
