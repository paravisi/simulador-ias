package src.co.edu.unal.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JTextField;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.model.Register;
import src.co.edu.unal.model.RegisterListener;

public class RegisterView extends JTextField implements
		RegisterListener {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public RegisterView(Register model) {
		super(model.getBits_size());
		owner = model;
		super.setDocument(new RegisterDocument());
		addFocusListener(owner);
		owner.addListener(this);
	
	}

	@Override
	public void processRegisterEvent(RegisterEvent e)
			throws InterruptedException {
		if (e == RegisterEvent.WRITE || e == RegisterEvent.IASWRITE) {
			this.setText(App.getInstance().getActiveBase().getTransformer()
					.transformValue(owner.Read(0,owner.getBits_size()-1).value()));
		}
		if (e == RegisterEvent.IASREAD) {
			Color oldColor = this.getBackground();
			this.setBackground(Color.GREEN);
			this.paintComponent(this.getGraphics());
			Thread.sleep(App.getInstance().getInstructionsTimeDelay());
			//TODO: Mejorar el retraso  - Thread.sleep(200);
			this.setBackground(oldColor);
			this.paintComponent(this.getGraphics());
		}
		if (e== RegisterEvent.IASWRITE){
			Color oldColor = this.getBackground();
			this.setBackground(Color.YELLOW);
			paintComponent(this.getGraphics());
			Thread.sleep(App.getInstance().getInstructionsTimeDelay());
			//TODO: Mejorar el retraso  - Thread.sleep(200);
			this.setBackground(oldColor);
			paintComponent(this.getGraphics());

		}
	}
	

    // Since we're always going to fill our entire
    // bounds, allow Swing to optimize painting of us
	@Override
    public boolean isOpaque() {
        return false;
    }
	
    protected void paintComponent(Graphics g) {
    	int width = getWidth()-3;
        int height = getHeight()-3;

        // Paint a rounded rectangle in the background.
        g.setColor(getBackground());
        g.fillRect(1, 1, width, height);

        // Now call the superclass behavior to paint the foreground.
        super.paintComponent(g);
    }

	
	

	private Register owner;
}
