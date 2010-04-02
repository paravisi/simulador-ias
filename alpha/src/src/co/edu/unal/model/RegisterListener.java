package src.co.edu.unal.model;

public interface RegisterListener {
	enum RegisterEvent {
		READ,WRITE,IASWRITE,IASREAD
	}

	public void processRegisterEvent(RegisterEvent e) throws InterruptedException;
}
