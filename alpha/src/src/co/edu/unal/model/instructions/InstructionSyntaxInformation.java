package src.co.edu.unal.model.instructions;

import src.co.edu.unal.controller.App;


public class InstructionSyntaxInformation {

	public InstructionSyntaxInformation(String c_spatronInicio, String c_patronFinal, String predictor) {
		Ipatron = c_spatronInicio;
		Fpatron = c_patronFinal;
		this.predictor=predictor;
	}

	public String getPredictor() {
		return predictor;
	}

	public boolean matchPattern(String s) {
		if (s.matches(Ipatron+"\\(("+App.getInstance().getActiveBase().getRegex()+"{"+App.getInstance().getMemoryAddresWritingWidth()+"})"+Fpatron))
			return true;
		return false;
	}

	private String Ipatron,Fpatron;
	private String predictor;
}
