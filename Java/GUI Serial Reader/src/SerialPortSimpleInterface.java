import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

public final class SerialPortSimpleInterface {
	private CommPortIdentifier 	_PortIdentifier;
	private CommPort			_CommPort;
	private SerialPort 			_SerialPort;
	private int 				_BAUDRATE, _STOP_BITS, _PARITY, _DATABITS;
	private InputStream			_SerialInputStream;
	private OutputStream		_SerialOutputSream;
	private int 				_READ_EXISTING_BUFFER_LEN	=	2048;
	private boolean				_IsConnected = false;
	 
	public SerialPortSimpleInterface() {
		super();
		_BAUDRATE = 9600;
		_STOP_BITS = SerialPort.STOPBITS_1;
		_PARITY = SerialPort.PARITY_NONE;
		_DATABITS =  SerialPort.DATABITS_8;
	}
	
	
	public boolean OpenPort(String PortName) throws Exception {
		_PortIdentifier = CommPortIdentifier.getPortIdentifier(PortName);
        
        if ( _PortIdentifier.isCurrentlyOwned() ) {
            return false;
        }
        else {
        	_CommPort = _PortIdentifier.open(this.getClass().getName(),2000);
        	
            if ( _CommPort instanceof SerialPort ) {
            	_SerialPort = (SerialPort) _CommPort;
            	_SerialPort.setSerialPortParams(_BAUDRATE,_DATABITS,_STOP_BITS,_PARITY);
                
                _SerialInputStream = _SerialPort.getInputStream();
                _SerialOutputSream = _SerialPort.getOutputStream();
                _IsConnected = true;
                return true;
            }
        }
        return true;
	} 
	
	public boolean ClosePort() {
		if (!_IsConnected) 
			return true;
		try {
			_SerialPort.close();
		}
		catch (Exception Ex) {
			return false;
		}
		if (!_IsConnected) 
			return true;
		else
			return false;
	}	
	
	public boolean IsConnected() {
		return _IsConnected;
	}
	
	public ArrayList<String> GetPortNames() {
		ArrayList<String> list = new ArrayList<String>();

	    Enumeration portList = CommPortIdentifier.getPortIdentifiers();

	    while (portList.hasMoreElements()) {
	        CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
	        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	            list.add(portId.getName());
	        }
	    }

	    return list;
	}
	
	
	public String ReadExisting() {
		byte[] buffer = new byte[_READ_EXISTING_BUFFER_LEN];
		String strBuff = new String("");
        int len = -1;
        
        if (!_IsConnected)
        	return strBuff;
        try {
            if ( ( len = this._SerialInputStream.read(buffer)) > -1 ) {
            	strBuff = (new String(buffer,0,len));        
            }
        }
        catch (Exception e) {
            return strBuff;
        }     
        return strBuff;
	}
	
	public boolean Write(String Data) {
		if (!_IsConnected)
        	return false;
		try {
			_SerialOutputSream.write(Data.getBytes());
		}
		catch (IOException Ex) {
			return false;
		}
		return true;
		
	}
	
	
}
