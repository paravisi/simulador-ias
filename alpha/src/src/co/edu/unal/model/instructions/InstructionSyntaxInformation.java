package src.co.edu.unal.model.instructions;

import src.co.edu.unal.controller.App;


public class InstructionSyntaxInformation {

	public InstructionSyntaxInformation(String c_spatronInicio, String c_patronFinal, String predictor) {
		Ipatron = c_spatronInicio;
		Fpatron = c_patronFinal;
		this.predictor=predictor;
	}
	public InstructionSyntaxInformation(String c_spatronInicio) {
		Fpatron ="";
		Ipatron = c_spatronInicio;
		this.predictor=c_spatronInicio;
	}

	public String getPredictor() {
		return predictor;
	}

	public boolean matchPattern(String s) {
		if ((!Fpatron.isEmpty()) && s.matches(Ipatron+"\\(("+App.getInstance().getActiveBase().getRegex()+"{1,"+App.getInstance().getMemoryAddresWritingWidth()+"})"+Fpatron))
			return true;
		if (Fpatron.isEmpty() && s.matches(Ipatron)){
			return true;
		}
			
		return false;

	}

	private String Ipatron,Fpatron;
	private String predictor;
}
