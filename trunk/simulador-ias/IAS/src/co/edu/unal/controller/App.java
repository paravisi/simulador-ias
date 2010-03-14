package co.edu.unal.controller;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.unal.model.Address;
import co.edu.unal.model.Data;
import co.edu.unal.model.Memory;
import co.edu.unal.model.Register;
import co.edu.unal.model.instructions.Instruction;
import co.edu.unal.model.instructions.InstructionSyntaxInformation;
import co.edu.unal.view.MainFrame;

public class App {
	public synchronized static App getInstance() {
		while (m_this == null) {
			m_this = new App();
		}

		return m_this;
	}

	private void initializeBasicRegisters(){
		basicRegisters.put("AC",new Register(40));
		basicRegisters.put("MQ",new Register(40));
		basicRegisters.put("PC",new Register(10,false));
	}
	private void initializeInstructionSet(){
		instructionSet.add(new Instruction(new Data("A", 16),
				new InstructionSyntaxInformation("LOAD M\\(([0-9,A-F,a-f]{"
						+ memoryAddresWidth + "})\\)", "LOAD M(X)"),
				new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {
						
						App.getInstance().getBasicRegisters().get("AC").Write(App.getInstance().getApp_memory().Read(x));
						return true;
					}
				}));
		instructionSet.add(new Instruction(new Data("9", 16),
				new InstructionSyntaxInformation("LOAD MQ,M\\(([0-9,A-F,a-f]{"
						+ memory_length + "})\\)", "LOAD MQ,M(X)"),
				new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {
						
						App.getInstance().getBasicRegisters().get("MQ").Write(App.getInstance().getApp_memory().Read(x));
						return true;
					}
				}));
	}

	private App() {
		m_mframe = new MainFrame();
		app_memory = new Memory(memory_registers_width, memory_length);
		chageBase(16);
		initializeBasicRegisters();
		initializeInstructionSet();
	}

	public Memory getApp_memory() {
		return app_memory;
	}

	public static void main(String[] args) {
		App.getInstance().run();

	}

	public void run() {
		//m_mframe.init();
		//m_mframe.setTitle("IAS Simulator");
		//m_mframe.setExtendedState(Frame.MAXIMIZED_BOTH);
		//m_mframe.setVisible(true);
		App.getInstance().getApp_memory().Write(new Address(new Data(0), 20, 39),new Data ("09002",16));
		App.getInstance().getApp_memory().Write(new Address(new Data(2),memory_registers_width),new Data ("B",16));
		instructionSet.get(1).Exec(new Address(new Data(0),memory_registers_width));
		System.out.println(App.getInstance().getBasicRegisters().get("MQ").Read().value());
	}

	public List<InstructionSyntaxInformation> getInstrucionSyntaxInfoList() {
		List<InstructionSyntaxInformation> list = new ArrayList<InstructionSyntaxInformation>();
		list.add(new InstructionSyntaxInformation("LOAD -M\\(([0-9,A-F,a-f]{"
				+ memory_length + "})\\)", "LOAD -M(X)"));
		list.add(new InstructionSyntaxInformation(
				"LOAD -\\|M\\(([0-9,A-F,a-f]{" + memory_length + "})\\)\\|",
				"LOAD -|M(X)|"));
		list.add(new InstructionSyntaxInformation("LOAD \\|M\\(([0-9,A-F,a-f]{"
				+ memory_length + "})\\)\\|", "LOAD |M(X)|"));
		list.add(new InstructionSyntaxInformation("LOAD MQ", "LOAD MQ"));
		list.add(new InstructionSyntaxInformation("JUMP M\\(([0-9,A-F,a-f]{"
				+ memory_length + "})(,0:19)\\)", "JUMP M(X,0:19)"));
		list.add(new InstructionSyntaxInformation("JUMP M\\(([0-9,A-F,a-f]{"
				+ memory_length + "})(,20:39)\\)", "JUMP M(X,20:39)"));
		list.add(new InstructionSyntaxInformation("STOR M\\(([0-9,A-F,a-f]{"
				+ memory_length + "})\\)", "STOR M(X)"));
		list.add(new InstructionSyntaxInformation("STOR M\\(([0-9,A-F,a-f]{"
				+ memory_length / actual_base + "})(,8:19)\\)",
				"STOR M(X,8:19)"));
		list.add(new InstructionSyntaxInformation("STOR M\\(([0-9,A-F,a-f]{"
				+ memory_length + "})(,28:39)\\)", "JUMP M(X,28:39)"));
		return list;
	}

	public MainFrame getM_mframe() {
		return m_mframe;
	}
	
	public void chageBase(int newBase){
		this.actual_base=newBase;
		memoryAddresWidth = (int) Math.ceil(bus_width/(Math.log(actual_base)/Math.log(2)));
	}

	public int getMemory_registers_width() {
		return memory_registers_width;
	}

	public Map<String, Register> getBasicRegisters() {
		return basicRegisters;
	}
	private static App m_this;
	private MainFrame m_mframe;

	private Memory app_memory;
	private int memory_length = 100;
	private int memory_registers_width = 40;

	private ArrayList<Instruction> instructionSet = new ArrayList<Instruction>();

	private Map<String, Register> basicRegisters = new HashMap<String, Register>();



	private int actual_base;
	private int bus_width=10;
	private int memoryAddresWidth;
}
