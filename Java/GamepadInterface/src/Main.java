
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String a = new String("");
		GamepadInterface intrfc = new GamepadInterface();
		
		intrfc.ConnectGamepad();
		intrfc.BeginCalibrating();
		intrfc.GamepadOn();
	
	}

}
