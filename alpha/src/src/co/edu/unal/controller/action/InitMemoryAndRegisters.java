package src.co.edu.unal.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.model.Data;

public class InitMemoryAndRegisters extends AbstractAction {
	
	public InitMemoryAndRegisters() {
		putValue( SMALL_ICON, new ImageIcon( getClass().getClassLoader().getResource( BASE + "restart_memory.png" ) ) );

	}

	public InitMemoryAndRegisters(String name) {
		super(name);
	}

	public InitMemoryAndRegisters(String name, Icon icon) {
		super(name, icon);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		App.getInstance().getApp_memory().init();
		for (String s:App.getInstance().getBasicRegisters().keySet())
			App.getInstance().getBasicRegisters().get(s).Write(new Data(new Long(0)));
	}
	private static final String BASE = "src/co/edu/unal/view/resources/";

}
