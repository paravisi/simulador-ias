package src.co.edu.unal.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.controller.Executer;

public class ExecuteAll extends AbstractAction implements Runnable{

	public ExecuteAll() {
		putValue( SMALL_ICON, new ImageIcon( getClass().getClassLoader().getResource( BASE + "Play All.png" ) ) );

	}

	public ExecuteAll(String name) {
		super(name);
	}

	public ExecuteAll(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		
		Thread hilo=new Thread(this);
		
		hilo.start();
		App.getInstance().setPCinitiated(false);
		

	}
	private static final String BASE = "src/co/edu/unal/view/resources/";
	@Override
	public void run() {
		Boolean follow;
		do{
			follow=Executer.Execute();
		}while(follow);
		
	}
}
