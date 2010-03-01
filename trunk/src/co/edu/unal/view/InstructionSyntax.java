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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class InstructionSyntax extends JTextField {
	class AutoDocument extends PlainDocument {

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
			String s2 = getMatch(getText(0, i) + s);
			int j = (i + s.length()) - 1;
			if (isStrict && s2 == null) {
				s2 = getMatch(getText(0, i));
				j--;
			} else if (!isStrict && s2 == null) {
				super.insertString(i, s, attributeset);
				return;
			}
			super.remove(0, getLength());
			super.insertString(0, s2, attributeset);
			setSelectionStart(i + 1);
			setSelectionEnd(getLength());
		}

		@Override
		public void remove(int i, int j) throws BadLocationException {
			if (i == 0) {
				super.remove(i, j);
				return;
			}
			int k = getSelectionStart();
			if (k > 0)
				k--;
			String s = getMatch(getText(0, k));
			if (!isStrict && s == null) {
				super.remove(i, j);
			} else {
				super.remove(0, getLength());
				super.insertString(0, s, null);
			}
			try {
				setSelectionStart(k);
				setSelectionEnd(getLength());
			} catch (Exception exception) {
			}
		}

	}

	public InstructionSyntax(DefaultTreeModel tree) {
		isCaseSensitive = false;
		isStrict = false;
		if (tree == null) {
			throw new IllegalArgumentException("values can not be null");
		} else {
			dataTree = tree;
			init();
			return;
		}
	}

	public InstructionSyntax() {
		this(getDefaultInstructionSet());
	}

	private static DefaultTreeModel getDefaultInstructionSet() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("set");
		DefaultTreeModel modelo = new DefaultTreeModel(root);
		DefaultMutableTreeNode load = new DefaultMutableTreeNode("LOAD ");
		DefaultMutableTreeNode jump = new DefaultMutableTreeNode("JUMP ");
		DefaultMutableTreeNode loadmq = new DefaultMutableTreeNode("LOAD MQ");
		DefaultMutableTreeNode loadm = new DefaultMutableTreeNode("LOAD M(");
		DefaultMutableTreeNode loadmqm = new DefaultMutableTreeNode("LOAD MQ,M(");
		DefaultMutableTreeNode loadmm = new DefaultMutableTreeNode("LOAD -M(");
		DefaultMutableTreeNode jumpm = new DefaultMutableTreeNode("JUMP M(");
		DefaultMutableTreeNode jumpcm = new DefaultMutableTreeNode("JUMP +M(");
		modelo.insertNodeInto(load, root, 0);
		modelo.insertNodeInto(jump, root, 1);
		modelo.insertNodeInto(loadm, load, 0);
		modelo.insertNodeInto(loadmq, load, 1);
		modelo.insertNodeInto(loadmqm, load, 2);
		modelo.insertNodeInto(loadmm, load, 3);
		modelo.insertNodeInto(jumpm, jump, 0);
		modelo.insertNodeInto(jumpcm, jump, 1);
		return modelo;
	}

	private void init() {
		setDocument(new AutoDocument());
		// if (isStrict && dataTree.size() > 0)
		// setText(dataTree.get(0).toString());
	}

	protected String getMatch(String s) {
		return getMatch(s, dataTree.getRoot());
	}

	protected String getMatch(String s, Object padre) {
		for (int i = 0; i < dataTree.getChildCount(padre); i++) {
			String instruccion = dataTree.getChild(padre, i).toString();
			if (instruccion != null) {
				if (!isCaseSensitive
						&& (s.toUpperCase()
								.startsWith(instruccion.toUpperCase()) || s.toUpperCase().equals(instruccion.toUpperCase())))
					return getMatch(s, dataTree.getChild(padre, i));
				if (!isCaseSensitive
						&& instruccion.toUpperCase()
								.startsWith(s.toUpperCase())) {
					return instruccion;
				}
				if (isCaseSensitive && (s.startsWith(instruccion) || s.equals(instruccion)))
					return getMatch(s, dataTree.getChild(padre, i));
				if (isCaseSensitive	&& instruccion.startsWith(s)) {
					return instruccion;
				}
				
			}
		}

		return null;
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

	public DefaultTreeModel getDataTree() {
		return dataTree;
	}

	public void setDataList(DefaultTreeModel tree) {
		if (tree == null) {
			throw new IllegalArgumentException("values can not be null");
		} else {
			dataTree = tree;
			return;
		}
	}

	protected DefaultTreeModel dataTree;

	protected boolean isCaseSensitive;

	protected boolean isStrict;
}

