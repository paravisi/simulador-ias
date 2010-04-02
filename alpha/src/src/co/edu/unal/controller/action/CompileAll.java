package src.co.edu.unal.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;

import src.co.edu.unal.controller.Compiler;

public class CompileAll extends AbstractAction {
	
	public CompileAll(JTextArea code){
		putValue( SMALL_ICON,new ImageIcon( getClass().getClassLoader().getResource( BASE + "Compile.png" )));
		doc=code;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Compiler.compile(doc);
	}
	JTextArea doc;
	private static final String BASE = "src/co/edu/unal/view/resources/";

}
