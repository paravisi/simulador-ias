//http://www.java2s.com/Code/Java/Swing-JFC/AutocompleteTextField.htm

/* From http://java.sun.com/docs/books/tutorial/index.html */

/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

package co.edu.unal.view;

import java.util.List;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import co.edu.unal.controller.App;
import co.edu.unal.model.instructions.InstructionSyntaxInformation;

public class InstructionSyntaxTextField extends JTextField {
	class AutoDocument extends PlainDocument {

		@Override
		public void replace(int i, int j, String s, AttributeSet attributeset)
				throws BadLocationException {
			super.remove(i, j);
			insertString(i, s, attributeset);
			App.getInstance().getM_mframe().verifySyntax();
		}

		@Override
		public void insertString(int i, String s, AttributeSet attributeset)
				throws BadLocationException {
			if (s == null || "".equals(s))
				return;
			
			String s1 = getText(0, i);
			String s2 = getMatch(s1 + s);
			int j = (i + s.length()) - 1;
			if (isStrict && s2 == null) {
				s2 = getMatch(s1);
				j--;
			} else if (!isStrict && s2 == null) {
				super.insertString(i, s, attributeset);
				return;
			}
			if (autoComboBox != null && s2 != null)
				autoComboBox.setSelectedValue(s2);
			super.remove(0, getLength());
			super.insertString(0, s2, attributeset);
			
			setSelectionStart(i + 1);
			setSelectionEnd(getLength());
			App.getInstance().getM_mframe().verifySyntax();
		}

		@Override
		public void remove(int i, int j) throws BadLocationException {
			super.remove(i, j);
			
//			int k = getSelectionStart();
//			if (k > 0)
//				k--;
			String s = getMatch(getText(0, getLength()));
			if (isStrict && s != null) {
				super.remove(0, getLength());
				super.insertString(0, s, null);
			}
			if (autoComboBox != null && s != null)
				autoComboBox.setSelectedValue(s);
//			try {
//				setSelectionStart(k);
//				setSelectionEnd(getLength());
//			} catch (Exception exception) {
//			}
			App.getInstance().getM_mframe().verifySyntax();
		}

	}

	public InstructionSyntaxTextField(List<InstructionSyntaxInformation> list) {
		isCaseSensitive = false;
		isStrict = false;
		autoComboBox = null;
		if (list == null) {
			throw new IllegalArgumentException("values can not be null");
		} else {
			dataList = list;
			init();
			return;
		}
	}

	InstructionSyntaxTextField(List<InstructionSyntaxInformation> list, Java2sAutoComboBox b) {
		isCaseSensitive = false;
		isStrict = false;
		autoComboBox = null;
		if (list == null) {
			throw new IllegalArgumentException("values can not be null");
		} else {
			dataList = list;
			autoComboBox = b;
			init();
			return;
		}
	}

	private void init() {
		setDocument(new AutoDocument());
		// if (isStrict && dataList.size() > 0)
		// setText(dataList.get(0).toString());
	}

	private String getMatch(String s) {
		for (int i = 0; i < dataList.size(); i++) {
			InstructionSyntaxInformation instructionDescriptor = dataList.get(i);
			if (instructionDescriptor != null) {
				if (!isCaseSensitive
						&& instructionDescriptor.getPredictor().toUpperCase().startsWith(s.toUpperCase()))
					return instructionDescriptor.getPredictor();
				if (isCaseSensitive && instructionDescriptor.getPredictor().startsWith(s))
					return instructionDescriptor.getPredictor();
			}
		}

		return null;
	}
	
	public boolean verifySyntax(){
		try {
			String instruction=getDocument().getText(0, (getDocument().getLength()));
			for (int i = 0; i < dataList.size(); i++) {
				InstructionSyntaxInformation instructionDescriptor = dataList.get(i);
				if (instructionDescriptor.matchPattern(instruction)){
					return true;
				}
			}
		} catch (BadLocationException e) {
			return false;
		}
		return false;
	}

	public void replaceSelection(String s) {
		AutoDocument _lb = (AutoDocument) getDocument();
		if (_lb != null)
			try {
				int i = Math.min(getCaret().getDot(), getCaret().getMark());
				int j = Math.max(getCaret().getDot(), getCaret().getMark());
				_lb.replace(i, j - i, s, null);
			} catch (Exception exception) {
			}
	}

	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public void setCaseSensitive(boolean flag) {
		isCaseSensitive = flag;
	}

	public boolean isStrict() {
		return isStrict;
	}

	public void setStrict(boolean flag) {
		isStrict = flag;
	}

	public List<InstructionSyntaxInformation> getDataList() {
		return dataList;
	}

	public void setDataList(List<InstructionSyntaxInformation> list) {
		if (list == null) {
			throw new IllegalArgumentException("values can not be null");
		} else {
			dataList = list;
			return;
		}
	}

	private List<InstructionSyntaxInformation> dataList;

	private boolean isCaseSensitive;

	private boolean isStrict;

	private Java2sAutoComboBox autoComboBox;
}
