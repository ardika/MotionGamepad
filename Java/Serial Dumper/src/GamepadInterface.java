import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamepadInterface {
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
	
	public GamepadInterface() {
		_NormalPitchMap = new ArrayList<Integer>(256);
		_NormalRollMap = new ArrayList<Integer>(256);
		_NormalYawMap = new ArrayList<Integer>(256);
		_CalibratedPitchMap = new ArrayList<Integer>(256);
		_CalibratedRollMap = new ArrayList<Integer>(256);
		_CalibratedYawMap = new ArrayList<Integer>(256);
		_PacketPattern = Pattern.compile("X(.{3})Y(.{3})M(.{3})S(\\d{1})(\\d{1})(\\d{1})\n");
		
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
	
	public void BeginCalibrating(){
		_IsCalibrating = true;
	}

	public void StartGamepad() {
		
	}
	
	public void StopGamepad() {
		
	}
	
	public boolean Feed(String Packet) {
		if (!ExtractInfo(Packet))
			return false;
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
		_PacketMatcher = _PacketPattern.matcher(Packet);
		
		if (!_PacketMatcher.matches())
			return false;
		_LastAcceleroX = Integer.parseInt(_PacketMatcher.group(_PacketMatcher.groupCount()-5).trim());
		_LastAcceleroY = Integer.parseInt(_PacketMatcher.group(_PacketMatcher.groupCount()-4).trim());
		_LastMagneto = Integer.parseInt(_PacketMatcher.group(_PacketMatcher.groupCount()-3).trim());
		_LastButtonAState = ((_PacketMatcher.group(_PacketMatcher.groupCount()-2)).equals("1"))?(true):(false);
		_LastButtonBState = ((_PacketMatcher.group(_PacketMatcher.groupCount()-1)).equals("1"))?(true):(false);
		_LastButtonCState = ((_PacketMatcher.group(_PacketMatcher.groupCount())).equals("1"))?(true):(false);
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
