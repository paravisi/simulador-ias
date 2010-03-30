package src.co.edu.unal.view;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import src.co.edu.unal.controller.App;

public class RegisterDocument extends PlainDocument {
	@Override
	public void replace(int i, int j, String s, AttributeSet attributeset)
			throws BadLocationException {
		super.remove(i, j);
		insertString(i, s, attributeset);
	}

	@Override
	public void insertString(int i, String s, AttributeSet attributeset)
			throws BadLocationException {
		if (s == null || "".equals(s))
			return;
		if (s.matches(App.getInstance().getActiveBase().getRegex())){
			super.insertString(i, s, attributeset);
		}

	}
}
