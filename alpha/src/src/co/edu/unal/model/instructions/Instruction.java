package src.co.edu.unal.model.instructions;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.controller.InstructionExecuter;
import src.co.edu.unal.model.Address;
import src.co.edu.unal.model.Data;
import src.co.edu.unal.model.Register;

public class Instruction {

	public Instruction(Data codop, InstructionSyntaxInformation syntax,
			InstructionExecuter executer) {
		if (codop.isPositive())
			this.codop = codop;
		this.syntax = syntax;
		this.executer = executer;
	}

	public InstructionSyntaxInformation getInstructionSyntaxInformation() {
		return syntax;
	}

	public boolean Exec(Address MAR) {
		if (executer.Execute((MAR)))
			return true;
		else
			return false;
	}



	public boolean codopCorrespondece(Address instruction) {
		if (App.getInstance().getApp_memory().Read(instruction).value() == codop
				.value())
			return true;
		else
			return false;
	}
	public Data getCodop() {
		return codop;
	}
	private InstructionExecuter executer;
	private InstructionSyntaxInformation syntax;
	private Data codop;


}
