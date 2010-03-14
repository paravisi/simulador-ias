package co.edu.unal.model.instructions;


public class InstructionSyntaxInformation {

	public InstructionSyntaxInformation(String c_spatron, String c_predictor) {
		patron = c_spatron;
		predictor = c_predictor;
	}

	public String getPredictor() {
		return predictor;
	}

	public boolean matchPattern(String s) {
		if (s.matches(patron))
			return true;
		return false;
	}

	private String patron;
	private String predictor;
}
