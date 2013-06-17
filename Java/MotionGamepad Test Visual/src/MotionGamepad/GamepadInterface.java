package MotionGamepad;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.InputStream;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GamepadInterface {
	private GamepadSerialCommunication _SerialComm;
	private Reader _GamepadReader;
	private Thread _GamepadReaderThread;
	private int _ZeroIndex = 127;
	private List<Integer> _NormalRollMap;
	private List<Integer> _NormalPitchMap;
	private List<Integer> _NormalYawMap;
	private List<Integer> _CalibratedRollMap;
	private List<Integer> _CalibratedPitchMap;
	private List<Integer> _CalibratedYawMap;
	private int _LastAcceleroX;
	private int _LastAcceleroY;
	private int _LastMagneto;
	private int _LastPitchElevation;
	private int _LastRollElevation;
	private int _LastYawElevation;
	private boolean _LastButtonAState;
	private boolean _LastButtonBState;
	private boolean _LastButtonCState;
	private Pattern _PacketPattern;
	private Matcher _PacketMatcher;
	private boolean _IsCalibrating = false;
	private String _PacketBuffer;
	
	private int	   _PROTOCOL_TEST_TIMEOUT = 5000;
	private String _TEST_PROTCL = new String("p");
	private String _TOOGLE_TRANSMISSION = new String("t");
	private String _TEST_PROTCL_RESPONSE = new String("unnes!");
	private String _ACCLRX_FLAG = new String("X");
	private String _ACCLRY_FLAG = new String("Y");
	private String _MAGNTO_FLAG = new String("M");
	private String _BUTTON_FLAG = new String("S");
	private String _END_PACKET = new String("E");	
	
	private String _VBRT_FLAG = new String("V");
	private String _BUZZ_FLAG = new String("B");
	
	public GamepadInterface() {
		_SerialComm = new GamepadSerialCommunication();
		_PacketBuffer = new String("");
		_NormalPitchMap = new ArrayList<Integer>(256);
		_NormalRollMap = new ArrayList<Integer>(256);
		_NormalYawMap = new ArrayList<Integer>(256);
		_CalibratedPitchMap = new ArrayList<Integer>(256);
		_CalibratedRollMap = new ArrayList<Integer>(256);
		_CalibratedYawMap = new ArrayList<Integer>(256);
		
		
		_PacketPattern = Pattern.compile(_ACCLRX_FLAG + "(.{3})" + 
				_ACCLRY_FLAG + "(.{3})" + _MAGNTO_FLAG + "(.{3})" + 
				_BUTTON_FLAG + "(\\d{1})(\\d{1})(\\d{1})" + _END_PACKET);
		
		for (int i=127; i >= 0; i--) {
			_NormalPitchMap.add(i);
			_NormalRollMap.add(i);
			_NormalYawMap.add(i);
			_CalibratedPitchMap.add(i);
			_CalibratedRollMap.add(i);
			_CalibratedYawMap.add(i);
		}
		
		for (int i=255; i >= 128; i--) {
			_NormalPitchMap.add(i);
			_NormalRollMap.add(i);
			_NormalYawMap.add(i);
			_CalibratedPitchMap.add(i);
			_CalibratedRollMap.add(i);
			_CalibratedYawMap.add(i);
		}
	}
	
	public boolean GamepadOn() {
		if (_GamepadReaderThread != null) {
			return false;
		}
		SendToogleTransmissionCommand();
		while (_SerialComm.BytesToRead() <= 0) {
			SendToogleTransmissionCommand();
			try {
				Thread.sleep(100);
			} catch (Exception Ex){};
		}
		this.PerformGamepadEffect(0, 500);
		_GamepadReader = new Reader(this);
		_GamepadReaderThread = new Thread(_GamepadReader);
		_GamepadReaderThread.start();
		return true;
	}
	
	public boolean IsGamepadConnected() {
		return _SerialComm.IsConnected();
	}
	
	public void GamepadOff() {
		if (!_GamepadReaderThread.equals(null)) {
			_GamepadReaderThread.stop();
		}
	}
	
	public boolean ConnectGamepad() {
		try {
			_SerialComm.OpenPort(this.GetGamepadPortName());
		}
		catch (Exception Ex) {};
		return _SerialComm.IsConnected();
	}
	
	public GamepadSerialCommunication GetSerial() {
		return _SerialComm;
	}
	
	private void SendToogleTransmissionCommand() {
		_SerialComm.Write(_TOOGLE_TRANSMISSION);
	}
	
	public boolean DisconnectGamepad() {
		try {
			_SerialComm.ClosePort();
		}
		catch (Exception Ex) {};
		return _SerialComm.IsConnected();
	}
	
	private class Reader implements Runnable 
    {
		private GamepadInterface _Interface;
		
        public Reader (GamepadInterface Interface)
        {
        	this._Interface = Interface;
        }
        
        public void run ()
        {
            while (this._Interface.IsGamepadConnected())
            {
            	
            	if (_Interface.GetSerial().BytesToRead() > 0) {
            		this._Interface.Feed(_Interface.GetSerial().ReadExisting());
            		try {
                		Thread.sleep(50);
                	} catch (Exception Ex) {};
            	}
            }       
        }
    }

	
	private boolean TestProtocol() {
		String Response = new String("");
		int TimeEsplased = 0;
		
		while (TimeEsplased++ < _PROTOCOL_TEST_TIMEOUT && !Response.contains(_TEST_PROTCL_RESPONSE)) {
			_SerialComm.Write(_TEST_PROTCL);
			Response += _SerialComm.ReadExisting();
			try {
				Thread.sleep(1);
			}
			catch (Exception Ex) {};
			
		}
		return (Response.contains(_TEST_PROTCL_RESPONSE));
	}
	
	private String GetGamepadPortName() {
		ArrayList<String> AvailablePortNames = new ArrayList<String>();		
		String GamepadPortName = new String("");
		AvailablePortNames.addAll(_SerialComm.GetPortNames());
		
		for (Iterator<String> i = AvailablePortNames.iterator(); i.hasNext();) {
			try {
				String CurrentAvailablePortName = i.next().toString();
				if (!CommPortIdentifier.getPortIdentifier(CurrentAvailablePortName).isCurrentlyOwned()) {
					if (_SerialComm.OpenPort(CurrentAvailablePortName)) {
						// test protocol
						if (this.TestProtocol()) {
							GamepadPortName = CurrentAvailablePortName;
						}
						_SerialComm.ClosePort();
					}
				}
			}
			catch (Exception Ex) {
				
			}
			
		}
		return GamepadPortName;
	}
	
	public void BeginCalibrating(){
		_IsCalibrating = true;
	}
	
	public void PerformGamepadEffect(int VibrateMilis, int BuzzMilis) {
		if (_SerialComm.IsConnected()) {
			_SerialComm.Write(_VBRT_FLAG + Integer.toString(VibrateMilis) +
								_BUZZ_FLAG + Integer.toString(BuzzMilis) + _END_PACKET);
			_SerialComm.Write(_VBRT_FLAG + Integer.toString(VibrateMilis) +
					_BUZZ_FLAG + Integer.toString(BuzzMilis) + _END_PACKET);
		}
		
	}
	
	public boolean Feed(String Packet) {
		_PacketBuffer += Packet;
		if (!ExtractInfo(_PacketBuffer))
			return false;
		_PacketBuffer = "";
		if (_IsCalibrating) {
			BuildMap(_NormalPitchMap, _CalibratedPitchMap, _LastAcceleroY);
			BuildMap(_NormalRollMap, _CalibratedRollMap, _LastAcceleroX);
			BuildMap(_NormalYawMap, _CalibratedYawMap, _LastMagneto);
			_IsCalibrating = false;
		}
		_LastPitchElevation = _ZeroIndex - _CalibratedPitchMap.indexOf(_LastAcceleroY);
		_LastRollElevation = _ZeroIndex  - _CalibratedRollMap.indexOf(_LastAcceleroX);
		_LastYawElevation =_ZeroIndex  - _CalibratedYawMap.indexOf(_LastMagneto);
		return true;
	}
	
	private void BuildMap(List<Integer> NormalMap, List<Integer> CalibratedMap, int NewZeroElement) {
		int ZeroPoint = (NormalMap.size() - 1) / 2;
		int Shift = ZeroPoint - NormalMap.indexOf(NewZeroElement);
		
		CalibratedMap.clear();
		
		if (Shift < 0) {
			// geser kiri
			CalibratedMap.addAll(NormalMap.subList(Math.abs(Shift), NormalMap.size()));
			CalibratedMap.addAll(NormalMap.subList(0, Math.abs(Shift)));
		}
		else {
			// geser kanan
			CalibratedMap.addAll(NormalMap.subList(NormalMap.size() - Math.abs(Shift), 
					NormalMap.size()));
			CalibratedMap.addAll(NormalMap.subList(0, NormalMap.size() -  Math.abs(Shift)));
		} 
	}
	
	private boolean ExtractInfo(String Packet) {
		String LastPacket;
		if (Packet.length() >= 17) {
			try {
				LastPacket = Packet.substring(Packet.lastIndexOf("E")-16, Packet.lastIndexOf("E")+1);
			}
			catch (Exception Ex) {
				return false;
			}
		}
		else
			return false;
		_PacketMatcher = _PacketPattern.matcher(LastPacket);
		
		if (!_PacketMatcher.matches())
			return false;
		_LastAcceleroX = Integer.parseInt(_PacketMatcher.group(1).trim());
		_LastAcceleroY = Integer.parseInt(_PacketMatcher.group(2).trim());
		_LastMagneto = Integer.parseInt(_PacketMatcher.group(3).trim());
		_LastButtonAState = ((_PacketMatcher.group(4)).equals("1"))?(true):(false);
		_LastButtonBState = ((_PacketMatcher.group(5)).equals("1"))?(true):(false);
		_LastButtonCState = ((_PacketMatcher.group(6)).equals("1"))?(true):(false);
		return true;
	}
	
	public int GetRollElevation() {
		return _LastRollElevation;
	}
	
	public int GetPitchElevation() {
		return _LastPitchElevation;
	}
	
	public int GetYawElevation() {
		return _LastYawElevation;
	}
	
	public boolean GetButtonAState() {
		return _LastButtonAState;
	}
	
	public boolean GetButtonBState() {
		return _LastButtonBState;
	}
	
	public boolean GetButtonCState() {
		return _LastButtonCState;
	}
	
}
