package componentesCustom;

import com.jfoenix.controls.JFXTextField;

public class SoloLetrasTextField extends JFXTextField{

	
	public SoloLetrasTextField() {
		
	}
	
	
	
	@Override
	public void replaceText(int start, int end, String text) {
		if (text.matches("[a-zA-Z ]") || text.isEmpty()) {
			super.replaceText(start, end, text);
		}
	}
	
	
	@Override
	public void replaceSelection(String replacement) {
		super.replaceSelection(replacement);
	}
	
	
}
