package src.co.edu.unal.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.model.Register;

public class MemoryView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MemoryView(){
		setLayout(new GridLayout(App.getInstance().getApp_memory().getRegisters().size(),2 ));
		int count = 0; 
		for (Register reg : App.getInstance().getApp_memory().getRegisters()) {
			this.add(new JLabel(Integer.toString(count)));
			count+=1;
			this.add(reg.getG_view());
		}
	}

}
