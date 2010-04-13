package src.co.edu.unal.controller;

import java.util.ArrayList;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import src.co.edu.unal.model.Statement;
import src.co.edu.unal.model.instructions.Instruction;

public class SyntaxVerifier {
	public static boolean verifyLineSyntax(String line){
		for (Instruction instruc : App.getInstance().getInstructionSet()){
			if (instruc.getInstructionSyntax().matchPattern(line))
				return true;
		}
		for (Statement stat : App.getInstance().getBasicStatements()){
			if (stat.getRegex().match(line))
				return true;
		}
		return false;
	}
	public static String getMatch(String line){
		String match=line.toUpperCase();
		for (Instruction instruc : App.getInstance().getInstructionSet()){
			if (instruc.getInstructionSyntax().getPredictor().startsWith(match))
				return instruc.getInstructionSyntax().getPredictor();
		}
		for (Statement stat : App.getInstance().getBasicStatements()){
			if (stat.getPredictor().startsWith(match))
				return stat.getPredictor();
		}
		return null;
	}
	public static ArrayList<Integer> verifyDocumentSyntax(Document doc){
		javax.swing.text.Element root = doc.getDefaultRootElement();
		int lineCount=0;
		ArrayList<Integer> errorLines= new ArrayList<Integer>();
		for (int i=0; i<root.getElementCount();i++){
			int start=root.getElement(i).getStartOffset();
//			int length = root.getElement(i).getEndOffset()-root.getElement(i).getStartOffset()- System.getProperty("line.separator").length();
			int length = root.getElement(i).getEndOffset()
			- root.getElement(i).getStartOffset()
			- 2;
			boolean isStatement=false;
			boolean isInstruction=false;
			try {
				if(!doc.getText(start,length).isEmpty()){
					for (Statement stat : App.getInstance().getBasicStatements()){
						if(stat.getRegex().match(doc.getText(start,length))){
							isStatement=true;
							break;
						}
					}
					for (Instruction inst : App.getInstance().getInstructionSet()){
						if (inst.getInstructionSyntax().matchPattern(doc.getText(start,length))){
							isInstruction=true;
							break;
						}
					}
					if (!isInstruction && !isStatement){
						errorLines.add(lineCount);
					}
				}
				lineCount++;
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return errorLines;
	}
}

