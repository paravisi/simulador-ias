package src.co.edu.unal.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import src.co.edu.unal.controller.App;

public class BasicRegisterView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BasicRegisterView(){
		this.setOpaque(true);
		this.setLayout(new GridLayout(5,2 ));
		this.add(new JLabel("AC")); 	this.add(App.getInstance().getBasicRegisters().get("AC").getG_view());
		this.add(new JLabel("MQ")); 	this.add(App.getInstance().getBasicRegisters().get("MQ").getG_view());
		this.add(new JLabel("IR")); 	this.add(App.getInstance().getBasicRegisters().get("IR").getG_view());
		this.add(new JLabel("IBR")); 	this.add(App.getInstance().getBasicRegisters().get("IBR").getG_view());
		this.add(new JLabel("PC")); 	this.add(App.getInstance().getBasicRegisters().get("PC").getG_view());		
	}

}
