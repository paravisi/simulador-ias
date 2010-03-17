package src.co.edu.unal.model;

import java.util.ArrayList;

public class Memory {

	public Memory(int register_size, int memory_size) {
		this.reg_size=register_size;
		registers = new ArrayList<Register>(memory_size);
		for (int i=0;i<memory_size;i++)
			registers.add(new Register(register_size));
	}

	public Data Read(Address adr){
		return Read(adr.getDirection(), adr.getBeginning(), adr.getEnding());
	}

	public Data Read(Data position, int x, int y) {
		if (position.isPositive())
			return registers.get(position.value().intValue()).Read(x, y);
		return null;
	}

	public void Write(Address position, Data data) {
			Write(position.getDirection(),data,position.getBeginning(),position.getEnding());
	}

	public void Write(Data position, Data data, int x, int y) {
		if (position.isPositive())
			registers.get(position.value().intValue()).Write(data, x, y);
	}
	
	public int getReg_size() {
		return reg_size;
	}
	public ArrayList<Register> getRegisters() {
		return registers;
	}

	private ArrayList<Register> registers;
	


	int reg_size,size;

}
