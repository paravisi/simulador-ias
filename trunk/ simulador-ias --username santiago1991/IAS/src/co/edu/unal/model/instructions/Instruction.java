package co.edu.unal.model.instructions;

import co.edu.unal.controller.App;
import co.edu.unal.controller.InstructionExecuter;
import co.edu.unal.model.Address;
import co.edu.unal.model.Data;
import co.edu.unal.model.Register;

public class Instruction {
	
	public Instruction(Data codop, InstructionSyntaxInformation syntax, InstructionExecuter executer){
		if (codop.isPositive())
			this.codop=codop;
		this.syntax=syntax;
		this.executer=executer;
	}		
	
	public InstructionSyntaxInformation getInstructionSyntaxInformation(){
		return syntax;
	}
	
	public boolean Exec(Address instruction){
		if (executer.Execute((getAddr(instruction))))
			return true;
		else 
			return false;
	}
	
	private Address getAddr(Address instruction){
		Register instruc = new Register(App.getInstance().getApp_memory().Read(instruction).value(),20,false);
		return new Address(instruc.Read(9, 19),App.getInstance().getMemory_registers_width());
	}
	
	public boolean codopCorrespondece(Address instruction){
		if (App.getInstance().getApp_memory().Read(instruction).value()==codop.value())
			return true;
		else
			return false;
	}
	
	private InstructionExecuter executer;
	private InstructionSyntaxInformation syntax;
	private Data codop;
	 
}
