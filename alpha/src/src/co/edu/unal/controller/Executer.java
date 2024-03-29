package src.co.edu.unal.controller;

import java.util.Map;

import src.co.edu.unal.model.Address;
import src.co.edu.unal.model.Data;
import src.co.edu.unal.model.Memory;
import src.co.edu.unal.model.Register;
import src.co.edu.unal.model.instructions.Instruction;

public class Executer {
	public Executer(){
		
	}
	public static boolean Execute(){ // Devuelve true si se sigue ejecutando, y false si debe parar la ejecución
		
		Map <String,Register> BasicReg= App.getInstance().getBasicRegisters();
		Memory memory = App.getInstance().getApp_memory();
		Address instructionMemoryAddress;
		if (BasicReg.get("IBR").Read(true).value()!=0){// En el caso en el que el IBR tenga algo 
			BasicReg.get("IR").Write(BasicReg.get("IBR").Read(0,7,true),true); // Guardar en IR los primeros 8 bits del IBR, es decir el codop de la instrucción
			instructionMemoryAddress = new Address(BasicReg.get("IBR").Read(8,19,true),40); // Guardar en MAR lo que esta en IBR de 8 a 19, es decir la posición de memoria sobre la que actua la instrucción 
			BasicReg.get("PC").Write(new Data(BasicReg.get("PC").Read().value()+1),true); // Incrmentar PC
			BasicReg.get("IBR").Write(new Data(new Long(0)),true);//Limpiar IBR para que no se quede en un ciclo infinito ejecutando siempre IBR
			return executeIR(instructionMemoryAddress);//Ejecutar IR y sigue la ejecución con el PC incrementado
		}
		else{// En el caso de que el IBR esté vacío
			if(memory.Read(BasicReg.get("PC").Read(true), 0, 19).value()!=0){ //En el caso de que se necesite la instrucción de la izquierda
				BasicReg.get("IR").Write(memory.Read(BasicReg.get("PC").Read(true), 0, 7,true),true); // Guarde en IR el codop de la instrucción de la izquierda 
				instructionMemoryAddress = new Address(memory.Read(BasicReg.get("PC").Read(true), 8, 19),40); //Guardar en el MAR lo que esta en la posición de memoria PC de las posiciones 8 a la 19
				BasicReg.get("IBR").Write(memory.Read(BasicReg.get("PC").Read(true), 20, 39),true); // Guardar en IBR la parte derecha del registro en la posición PC en la memoria
				return executeIR(instructionMemoryAddress);//Ejecutar IR y sigue a la ejecución del IBR
				
			}
			else{ //En el caso de que no se necesite la instrucción de la izquierda
				if(memory.Read(BasicReg.get("PC").Read(true), 20, 39,true).value()!=0){ //En el caso de que la instrucción de la derecha no sea cero
					BasicReg.get("IR").Write(memory.Read(BasicReg.get("PC").Read(true), 20, 27,true),true); // Guardar en IR el codop de la instrucción de la derecha (la única)
					instructionMemoryAddress = new Address(memory.Read(BasicReg.get("PC").Read(true), 28, 39),40); //Guardar en el MAR lo que esta en la posición de memoria PC de las posiciones 28 a la 39
					BasicReg.get("PC").Write(new Data(BasicReg.get("PC").Read().value()+1),true); // Incrmentar PC
					return executeIR(instructionMemoryAddress);//Ejecutar IR y sigue la ejecución con el PC incrementado
					
				}
				else{ // En el caso de que tanto izquierda como derecha sean cero
					return false;
				}
				
			}
		}
	}
	private static boolean executeIR(Address MAR){
		for (Instruction model : App.getInstance().getInstructionSet()){
			if (model.getCodop().value().intValue()==App.getInstance().getBasicRegisters().get("IR").Read().value().intValue()){
				model.Exec(MAR);
				return true;
			}
		
		}
		return false;
		
	}
	private static Address currentExecutionAddress;
}
