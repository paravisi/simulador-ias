package src.co.edu.unal.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.controller.Executer;

public class ExecuteOne extends AbstractAction implements Runnable {

	public ExecuteOne() {
		putValue( SMALL_ICON, new ImageIcon( getClass().getClassLoader().getResource( BASE + "Play.png" ) ) );

	}

	public ExecuteOne(String name) {
		super(name);
	}

	public ExecuteOne(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		hilo=new Thread(this);
		

		hilo.start();
		
	}
	private static final String BASE = "src/co/edu/unal/view/resources/";
	private Thread hilo;
	@Override
	public void run() {
		boolean follow = Executer.Execute();
		if (!follow)
			App.getInstance().setPCinitiated(false);
		
	}

}
