package src.co.edu.unal.view;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import src.co.edu.unal.controller.App;

public class RegisterDocument extends PlainDocument {
	//TODO: Añadir soporte de ingreso de números negativos
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
		if ((s.matches(App.getInstance().getActiveBase().getRegex()+"*"))&&((this.getText(0, this.getLength())+s).matches(App.getInstance().getActiveBase().getRegex()+"{0,"+App.getInstance().getMemoryRegisterWritingWidth()+"}"))){
			super.insertString(i, s, attributeset);
		}

	}
}
