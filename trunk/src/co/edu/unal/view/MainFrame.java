package co.edu.unal.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public MainFrame() {

	}

	public void init() {
		this.add(instruc);
	}
	InstructionSyntax instruc = new InstructionSyntax();
}
