package src.co.edu.unal.model;

import src.co.edu.unal.controller.StatementCompiler;

public class Statement {
	public Statement(StatementSyntax regex, StatementCompiler statcompiler,
			String predictor) {
		this.regex = regex;
		this.compiler = statcompiler;
		this.predictor = predictor;
	}

	public StatementSyntax getRegex() {
		return regex;
	}

	public StatementCompiler getCompiler() {
		return compiler;
	}

	public String getPredictor() {
		return predictor;
	}

	private StatementSyntax regex;
	private StatementCompiler compiler;
	private String predictor;

}
