package src.co.edu.unal.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.model.Data;

public class InitMemoryAndRegisters extends AbstractAction {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		App.getInstance().getApp_memory().init();
		for (String s:App.getInstance().getBasicRegisters().keySet())
			App.getInstance().getBasicRegisters().get(s).Write(new Data(new Long(0)));
	}

}
