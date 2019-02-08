package DataType;

import java.util.ArrayList;

public class HCFWords {
	public static HCFWords mHCFWords = new HCFWords();
	public static HCFWords get() {
		return mHCFWords;
	}

	static ArrayList<String> hintwords;

	public HCFWords() {
		hintwords = new ArrayList<String>();

		hintwords.add("Control input or external information wrong or missing");
		hintwords.add("Flaws in creation, process changes, incorrect modification or adaption");
		hintwords.add("Process Model inconsistent, incomplete or incorrect");
		hintwords.add("Component failures, Changes over time");
		hintwords.add("Inadequate or missing feedback, Feedback Delays");
		hintwords.add("Incorrect or no information provided, Measurement inaccuracies, Feedback Delays");
		hintwords.add("Delayed operation");
		hintwords.add("Inappropriate, ineffective or missing control action");
		hintwords.add("Process input missing or wrong");
		hintwords.add("Unindentified or out-of-range disturbance");
		hintwords.add("Process output contributes to system hazard");
		hintwords.add("Inadequate Actuator Operation");
		hintwords.add("Inadequate Sensor Operation");
		hintwords.add("Missing or wrong communication with another controller");
		hintwords.add("Conflicting control actions");

	}

	public static String getHCFWord(int i) {
		return hintwords.get(i);
	}

	public static int getHCFWorsSize() {return hintwords.size();}
}
