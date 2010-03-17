package src.co.edu.unal.model;

public interface RegisterListener {
	enum RegisterEvent {
		READ,WRITE
	}

	public void processRegisterEvent(RegisterEvent e) throws InterruptedException;
}
