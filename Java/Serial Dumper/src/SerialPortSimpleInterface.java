import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public final class SerialPortSimpleInterface {
	private CommPortIdentifier 	_PortIdentifier;
	private CommPort			_CommPort;
	private SerialPort 			_SerialPort;
	private int 				_BAUDRATE, _STOP_BITS, _PARITY, _DATABITS;
	InputStream					_SerialInputStream;
	OutputStream				_SerialOutputSream;
	 
	public SerialPortSimpleInterface() {
		super();
		_BAUDRATE = 9600;
		_STOP_BITS = SerialPort.STOPBITS_1;
		_PARITY = SerialPort.PARITY_NONE;
	}
	
	
	

	public ArrayList<String> GetPortNames() {
		int i;
		ArrayList<String> PortNames = new ArrayList<String>();
		CommPortIdentifier CurrentIdentifier;
		
		for (i = 1; i < 512; i++) {
			try {
				CurrentIdentifier = CommPortIdentifier.getPortIdentifier(
						"COM" + Integer.toString(i));
				PortNames.add("COM" + Integer.toString(i));
			}
			catch (NoSuchPortException Ex) {
				
			}
		}
		return PortNames;
	}
	
	/*
	public String ReadExisting() {
		
	}
	
	public int Connect() {
		
	}*/
}
