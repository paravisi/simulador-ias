package src.co.edu.unal.model;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class Base {
	public BaseTransform getTransformer() {
		return transformer;
	}

	public Base( int base, String charset,
			String regex, BaseTransform trans) {
		this.base = base;
		this.regex = regex;
		this.charset = charset;
		this.transformer = trans;
	}

	public int getBase() {
		return base;
	}

	public String getValidCharset() {
		return charset;
	}

	public String getRegex() {
		return regex;
	}



	private int base;
	private String charset;
	private String regex;
	private BaseTransform transformer;

}
