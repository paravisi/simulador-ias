package src.co.edu.unal.controller.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;

import src.co.edu.unal.controller.SyntaxVerifier;
import src.co.edu.unal.view.LineNumberedBorder;

public class VerifyDocumentSyntax extends AbstractAction {

	public VerifyDocumentSyntax(JTextArea doc){
		putValue( SMALL_ICON,new ImageIcon( getClass().getClassLoader().getResource( BASE + "check.png" )));
		this.doc=doc;
	}

	public VerifyDocumentSyntax(String name, Icon icon, JTextArea doc) {
		super(name, icon);
		this.doc=doc;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Integer> errors = new ArrayList<Integer>();
		for ( Integer error : SyntaxVerifier.verifyDocumentSyntax(doc.getDocument())){
			errors.add(error);
		}
		((LineNumberedBorder)doc.getBorder()).setErrorLines(errors);
		doc.repaint();
	}
	private JTextArea doc;
	private static final String BASE = "src/co/edu/unal/view/resources/";

}
