package src.co.edu.unal.model;

import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.controller.SyntaxVerifier;

public class CodeDocument extends PlainDocument {
	public CodeDocument(JTextArea owner){
		this.owner=owner;
	}

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

			int lineStart = owner.getLineStartOffset(owner.getLineOfOffset(owner.getCaretPosition()));
			int lineLength = (owner.getLineEndOffset(owner.getLineOfOffset(owner.getCaretPosition())) - owner.getLineStartOffset(owner.getLineOfOffset(owner.getCaretPosition())));
			String s1 = getText(lineStart, lineLength);
			String s2 = SyntaxVerifier.getMatch(s1 + s);
			App.getInstance().getM_mframe().setTitle(s1 + s + " - " + s2);
			int j = (i + s.length()) - 1;
			if (isStrict && s2 == null) {
				s2 = SyntaxVerifier.getMatch(s1);
				j--;
			} else if (!isStrict && s2 == null) {
				super.insertString(i, s, attributeset);
				return;
			}

			super.remove(lineStart, lineLength);
			super.insertString(lineStart, s2, attributeset);

			owner.setSelectionStart(i + 1);
			owner.setSelectionEnd((owner.getLineEndOffset(owner.getLineOfOffset(owner.getCaretPosition()))));

		}

		@Override
		public void remove(int i, int j) throws BadLocationException {
			super.remove(i, j);

			String s = SyntaxVerifier.getMatch(getText(0, getLength()));
			if (isStrict && s != null) {
				super.remove(0, getLength());
				super.insertString(0, s, null);
			}
			
		}


		JTextArea owner;
		boolean isStrict;
}
