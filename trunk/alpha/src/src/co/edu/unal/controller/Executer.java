package src.co.edu.unal.controller;

import src.co.edu.unal.model.Address;

public class Executer {
	public Executer(){
		
	}
	public static void Execute(){
		currentExecutionAddress = new Address(App.getInstance().getBasicRegisters().get("PC").Read(),10);
		currentExecutionAddress.setBeginning(0);
		currentExecutionAddress.setBeginning(19);
		App.getInstance().getBasicRegisters().get("IR").Write(App.getInstance().getApp_memory().Read(currentExecutionAddress));
		currentExecutionAddress.setBeginning(10);
		currentExecutionAddress.setBeginning(39);
		App.getInstance().getBasicRegisters().get("IBR").Write(App.getInstance().getApp_memory().Read(currentExecutionAddress));
		if ((App.getInstance().getBasicRegisters().get("IR").Read().value() +App.getInstance().getBasicRegisters().get("IBR").Read().value())==0){
			return;
		}
	}
	private static Address currentExecutionAddress;
}
