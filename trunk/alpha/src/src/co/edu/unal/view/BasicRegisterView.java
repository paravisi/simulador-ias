package src.co.edu.unal.view;

import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JLabel;

import src.co.edu.unal.controller.App;

public class BasicRegisterView extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BasicRegisterView(){
		this.setLayout(new GridLayout(5,2 ));
		this.add(new JLabel("AC")); 	this.add(App.getInstance().getBasicRegisters().get("AC").getG_view());
		this.add(new JLabel("MQ")); 	this.add(App.getInstance().getBasicRegisters().get("MQ").getG_view());
		this.add(new JLabel("IR")); 	this.add(App.getInstance().getBasicRegisters().get("IR").getG_view());
		this.add(new JLabel("IBR")); 	this.add(App.getInstance().getBasicRegisters().get("IBR").getG_view());
		this.add(new JLabel("PC")); 	this.add(App.getInstance().getBasicRegisters().get("PC").getG_view());		
	}

}
