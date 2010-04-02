package src.co.edu.unal.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.model.Register;

public class MemoryView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MemoryView(){
		
		setLayout(new GridLayout(App.getInstance().getApp_memory().getRegisters().size(),1 ));
		int count = 0; 
		for (Register reg : App.getInstance().getApp_memory().getRegisters()) {
			JPanel register = new JPanel();
			register.add(new JLabel(Integer.toString(count)),BorderLayout.WEST);
			count+=1;
			register.add(reg.getG_view(),BorderLayout.CENTER);
			this.add(register);
		}
	}

}
