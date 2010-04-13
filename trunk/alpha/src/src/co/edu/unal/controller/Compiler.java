package src.co.edu.unal.controller;

import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import src.co.edu.unal.model.Address;
import src.co.edu.unal.model.Data;
import src.co.edu.unal.model.Statement;
import src.co.edu.unal.model.instructions.Instruction;

public class Compiler {


	public static boolean compile(JTextArea doc) {
		// Primero verifique que la sintáxis del código es correcta
		if (SyntaxVerifier.verifyDocumentSyntax(doc.getDocument()).size() != 0) {
			return false;
		}
		// Si es correcta, empiece a recorrer todo el documento, compilando los
		// Statements
		javax.swing.text.Element root = doc.getDocument()
				.getDefaultRootElement();
		for (int i = 0; i < root.getElementCount(); i++) {
			int start = root.getElement(i).getStartOffset();
//			int length = root.getElement(i).getEndOffset()
//					- root.getElement(i).getStartOffset()
//					- System.getProperty("line.separator").length();
			int length = root.getElement(i).getEndOffset()
			- root.getElement(i).getStartOffset()
			-2;
			try {
				if (!doc.getText(start, length).isEmpty()) {
					for (Statement stat : App.getInstance()
							.getBasicStatements()) {
						if (stat.getRegex().match(doc.getText(start, length))) {
							stat.getCompiler().compile(
									doc.getText(start, length));
							break;
						}
					}

				}
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!App.getInstance().isPCinitiated()) {// Una vez compilados todos los Statements, el PC debe estar iniciado
			Data newPCValue = new Data(App.getInstance().getHigherAddressWithData().getDirection().value()+1);
			App.getInstance().getBasicRegisters().get("PC").Write(newPCValue);
			App.getInstance().setPCinitiated(true);
		}
			Address addressToWriteIn = new Address(App.getInstance().getBasicRegisters().get("PC").Read(), 0,19);
			String firstInstructionString ="";
			Instruction firstInstruction=null;
			for (int i = 0; i < root.getElementCount(); i++) {
				int start = root.getElement(i).getStartOffset();
//				int length = root.getElement(i).getEndOffset()
//						- root.getElement(i).getStartOffset()
//						- System.getProperty("line.separator").length();
				int length = root.getElement(i).getEndOffset()
				- root.getElement(i).getStartOffset()
				- 2;
				try {
					if (!doc.getText(start, length).isEmpty()) {
						for (Instruction inst : App.getInstance()
								.getInstructionSet()) {
							if (inst.getInstructionSyntax().matchPattern(
									doc.getText(start, length))) {
								if (firstInstructionString.isEmpty()){
									firstInstructionString=doc.getText(start, length);
									firstInstruction=inst;
								}else{	
										firstInstruction.compile(addressToWriteIn, firstInstructionString);
										addressToWriteIn.setBeginning(20);
										addressToWriteIn.setEnding(39);
										inst.compile(addressToWriteIn, doc.getText(start, length));
										firstInstructionString="";
										addressToWriteIn.setDirection(new Data(addressToWriteIn.getDirection().value()+1));
										addressToWriteIn.setBeginning(0);
										addressToWriteIn.setEnding(19);
									
								}
								break;	
							}
						}
						
					}

				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(!firstInstructionString.isEmpty() ){
				addressToWriteIn.setBeginning(20);
				addressToWriteIn.setEnding(39);
				firstInstruction.compile(addressToWriteIn, firstInstructionString);
			}
		
		App.getInstance().getBasicRegisters().get("IBR").Write(new Data(new Long(0)));
		return true;
	}
	
	
	private static Address incrementInstructionAddress(Address adr){
		Address retAdr=new Address(adr.getDirection(),adr.getBeginning(),adr.getEnding());
		if(adr.getEnding()==19){
			retAdr.setBeginning(20);
			retAdr.setEnding(39);
		}else{
			retAdr.setDirection(new Data(adr.getDirection().value()+1));
			retAdr.setBeginning(0);
			retAdr.setEnding(39);
		}
		
		return retAdr;
	}


	

}
