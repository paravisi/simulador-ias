package src.co.edu.unal.view;

import java.awt.Color;

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
		super();
		owner = model;
		super.setDocument(new RegisterDocument());
		addFocusListener(owner);
		/*String mask = "";
		for (int i = 0; i < ((int) Math.ceil(owner.getBits_size()
				/ (Math.log(App.getInstance().getActiveBase().getBase()) / Math
						.log(2)))); i++)
			mask += "#";

		try {
			MaskFormatter formatter = new MaskFormatter();
			formatter.setValidCharacters(new String(App.getInstance()
					.getActiveBase().getValidCharset()));
			formatter.setAllowsInvalid(false);
			formatter.setInvalidCharacters(null);
			formatter.install(this);
		} catch (Exception e) {
			System.out
					.println("Problemas durante la declaración de un MaskFormatter de Registros");
		}
		this.setValue(App.getInstance().getActiveBase().getTransformer()
				.transformValue(owner.Read().value()));
		owner.addListener(this);
		setFocusLostBehavior(COMMIT_OR_REVERT);
	*/
	}

	@Override
	public void processRegisterEvent(RegisterEvent e)
			throws InterruptedException {
		if (e == RegisterEvent.WRITE) {
			this.setText(App.getInstance().getActiveBase().getTransformer()
					.transformValue(owner.Read().value()));
			Color oldColor = this.getBackground();
			this.setBackground(Color.YELLOW);
			Thread.sleep(100);
			this.setBackground(oldColor);

		}
		if (e == RegisterEvent.READ) {
			Color oldColor = this.getBackground();
			this.setBackground(Color.GREEN);
			Thread.sleep(200);
			this.setBackground(oldColor);
		}
	}
	

	
	

	private Register owner;
}
