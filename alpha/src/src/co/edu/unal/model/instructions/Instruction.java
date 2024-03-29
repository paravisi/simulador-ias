package src.co.edu.unal.model.instructions;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.controller.InstructionCompiler;
import src.co.edu.unal.controller.InstructionExecuter;
import src.co.edu.unal.model.Address;
import src.co.edu.unal.model.Data;

public class Instruction {

	public Instruction(Data codop, InstructionSyntaxInformation syntax,
			InstructionExecuter executer,
			InstructionCompiler instructionCompiler) {
		if (codop.isPositive())
			this.codop = codop;
		this.syntax = syntax;
		this.executer = executer;
		this.compiler = instructionCompiler;
	}

	public InstructionSyntaxInformation getInstructionSyntax() {
		return syntax;
	}

	public boolean Exec(Address MAR) {
		if (executer.Execute((MAR)))
			return true;
		else
			return false;
	}

	public void compile(Address addressToWriteIn, String instruction) {
		App.getInstance().getApp_memory().Write(
				addressToWriteIn.getDirection(), codop,
				addressToWriteIn.getBeginning(),
				addressToWriteIn.getBeginning() + 7);// Escribir en los primeros
														// 8 bits de la
														// dirección en la que
														// se esta escribiendo
														// la instrucción, el
														// codop de la
														// instrucción
		App.getInstance().getApp_memory().Write(
				addressToWriteIn.getDirection(),
				this.compiler.compile(instruction),
				addressToWriteIn.getBeginning() + 8,
				addressToWriteIn.getEnding()); // Escribir en los restantes bits
												// (los últimos 12 de la
												// dirección que se proporciona
												// al método) la dirección
												// obtenida por el compiler de
												// la instrucción

	}

	public Data getCodop() {
		return codop;
	}

	private InstructionExecuter executer;
	private InstructionSyntaxInformation syntax;
	private Data codop;
	private InstructionCompiler compiler;

}
