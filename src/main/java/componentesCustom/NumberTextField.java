package componentesCustom;

import com.jfoenix.controls.JFXTextField;

public class NumberTextField extends JFXTextField{
	
	
	public NumberTextField() {
		
	}
	
	
	
	@Override
	public void replaceText(int start, int end, String text) {
		if (text.matches("[0-9]") || text.isEmpty()) {
			super.replaceText(start, end, text);
		}
	}
	
	
	
	@Override
	public void replaceSelection(String replacement) {
		super.replaceSelection(replacement);
	}

}
