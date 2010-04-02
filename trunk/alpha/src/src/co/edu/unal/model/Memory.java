package src.co.edu.unal.model;

import java.util.ArrayList;

public class Memory {

	public Memory(int register_size, int memory_size) {
		this.reg_size=register_size;
		registers = new ArrayList<Register>(memory_size);
		for (int i=0;i<memory_size;i++)
			registers.add(new Register(register_size));
	}
	
	public void init(){
		for (Register reg : registers){
			reg.Write(new Data());
		}
	}

	public Data Read(Address adr){
		return registers.get(adr.getDirection().value().intValue()).Read();
	}
	public Data Read(Address adr,boolean IASEvent){
		return registers.get(adr.getDirection().value().intValue()).Read(IASEvent);
	}
	public Data Read(Data position, int x, int y,boolean IASEvent) {
		if (position.isPositive())
			return registers.get(position.value().intValue()).Read(x, y,IASEvent);
		return null;
	}
	public Data Read(Data position, int x, int y) {
		if (position.isPositive())
			return registers.get(position.value().intValue()).Read(x, y);
		return null;
	}

	public void Write(Address position, Data data) {
		registers.get(position.getDirection().value().intValue()).Write(data);
	}
	public void Write(Address position, Data data,boolean IASEvent) {
		registers.get(position.getDirection().value().intValue()).Write(data,IASEvent);
	}

	public void Write(Data position, Data data, int x, int y) {
		if (position.isPositive())
			registers.get(position.value().intValue()).Write(data, x, y);
	}
	public void Write(Data position, Data data, int x, int y,boolean IASEvent) {
		if (position.isPositive())
			registers.get(position.value().intValue()).Write(data, x, y,IASEvent);
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
