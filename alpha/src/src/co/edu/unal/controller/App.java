package src.co.edu.unal.controller;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.co.edu.unal.model.Address;
import src.co.edu.unal.model.Base;
import src.co.edu.unal.model.BaseTransform;
import src.co.edu.unal.model.Data;
import src.co.edu.unal.model.Memory;
import src.co.edu.unal.model.Register;
import src.co.edu.unal.model.instructions.Instruction;
import src.co.edu.unal.model.instructions.InstructionSyntaxInformation;
import src.co.edu.unal.view.MainFrame;

public class App {
	public synchronized static App getInstance() {
		while (m_this == null) {
			m_this = new App();
		}

		return m_this;
	}

	private void initializeBasicRegisters() {
		basicRegisters.put("AC", new Register(40));
		basicRegisters.put("MQ", new Register(40));
		basicRegisters.put("PC", new Register(10, false));
		basicRegisters.put("IR", new Register(20, false));
		basicRegisters.put("IBR", new Register(20, false));
	}

	private void initializeInstructionSet() {
		instructionSet.add(new Instruction(new Data("1", hexa),
				new InstructionSyntaxInformation("LOAD M", "\\)", "LOAD M(X)"),
				new InstructionExecuter() {
					@Override
					public boolean Execute(Address x) {

						App.getInstance().getBasicRegisters().get("AC").Write(
								App.getInstance().getApp_memory().Read(x));
						return true;
					}
				}));
		instructionSet.add(new Instruction(new Data("9", hexa),
				new InstructionSyntaxInformation("LOAD MQ,M", "\\)",
						"LOAD MQ,M(X)"), new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {

						App.getInstance().getBasicRegisters().get("MQ").Write(
								App.getInstance().getApp_memory().Read(x));
						return true;
					}
				}));
		instructionSet.add(new Instruction(new Data("B", hexa),
				new InstructionSyntaxInformation("MUL M", "\\)", "MUL M(X)"),
				new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {

						Data MQ = App.getInstance().getBasicRegisters().get(
								"MQ").Read();
						Data MX = App.getInstance().getApp_memory().Read(x);
						Long result = new Long(MQ.value() * MX.value());
						Register Multiplication = new Register(result, 80, true);
						App.getInstance().getBasicRegisters().get("AC").Write(
								Multiplication.Read(0, 39));
						App.getInstance().getBasicRegisters().get("MQ").Write(
								Multiplication.Read(40, 79));
						return true;
					}
				}));
		instructionSet.add(new Instruction(new Data("21", hexa),
				new InstructionSyntaxInformation("STOR M", "\\)", "STOR M(X)"),
				new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {

						App.getInstance().getApp_memory().Write(
								x,
								App.getInstance().getBasicRegisters().get("AC")
										.Read());
						return true;
					}
				}));
		instructionSet.add(new Instruction(new Data("A", hexa),
				new InstructionSyntaxInformation("LOAD MQ"),
				new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {

						App.getInstance().getBasicRegisters().get("AC").Write(App.getInstance().getBasicRegisters().get("MQ").Read());
						return true;
					}
				}));
		instructionSet.add(new Instruction(new Data("2", hexa),
				new InstructionSyntaxInformation("LOAD -M","//)","LOAD -M(x)"),
				new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {

						App.getInstance().getBasicRegisters().get("AC").Write(new Data(App.getInstance().getApp_memory().Read(x).value()*(-1)));
						return true;
					}
				}));

	}

	private App() {
		m_mframe = new MainFrame();
		app_memory = new Memory(40, memory_length);
		setActiveBase(binary);
		initializeBasicRegisters();

	}

	public Memory getApp_memory() {
		return app_memory;
	}

	public static void main(String[] args) {
		App.getInstance().run();
		App.getInstance().getApp_memory().Write(
				new Address(new Data(new Long("0")), 20, 39),
				new Data("09002", hexa));
		App.getInstance().getApp_memory().Write(
				new Address(new Data(new Long("2")), App.getInstance()
						.getApp_memory().getReg_size()), new Data("159", hexa));
		App.getInstance().getApp_memory().Write(
				new Address(new Data(new Long("1")), 20, 39),
				new Data("0B002", hexa));
		App.getInstance().getInstructionSet().get(1).Exec(
				new Address(new Data(new Long("0")), App.getInstance()
						.getApp_memory().getReg_size()));
		App.getInstance().getInstructionSet().get(2).Exec(
				new Address(new Data(new Long("1")), App.getInstance()
						.getApp_memory().getReg_size()));
		System.out.println(App.getInstance().getBasicRegisters().get("MQ")
				.Read().value());
		System.out.println(App.getInstance().getBasicRegisters().get("AC")
				.Read().value());
	}

	public void run() {
		initializeInstructionSet();
		for (Register reg : app_memory.getRegisters()) {
			m_mframe.addComp(reg.getG_view());
		}
		m_mframe.init();
		m_mframe.setTitle("IAS Simulator");
		m_mframe.setExtendedState(Frame.MAXIMIZED_BOTH);
		m_mframe.setVisible(true);

	}

	public List<InstructionSyntaxInformation> getInstrucionSyntaxInfoList() {
		List<InstructionSyntaxInformation> list = new ArrayList<InstructionSyntaxInformation>();
		return list;
	}

	public MainFrame getM_mframe() {
		return m_mframe;
	}

	public Map<String, Register> getBasicRegisters() {
		return basicRegisters;
	}

	public static Base getHexa() {
		return hexa;
	}

	public static Base getOctal() {
		return octal;
	}

	public static Base getBinary() {
		return binary;
	}

	public Base getActiveBase() {
		return activeBase;
	}

	public void setActiveBase(Base activeBase) {
		memoryAddresWritingWidth = (int) Math.ceil(bus_width
				/ (Math.log(actual_base) / Math.log(2)));
		memoryRegisterWritingWidth = (int) Math.ceil(app_memory.getReg_size()
				/ (Math.log(actual_base) / Math.log(2)));
		this.activeBase = activeBase;

	}

	public int getMemoryRegisterWritingWidth() {
		return memoryRegisterWritingWidth;
	}

	public int getMemoryAddresWritingWidth() {
		return memoryAddresWritingWidth;
	}

	public ArrayList<Instruction> getInstructionSet() {
		return instructionSet;
	}

	private static App m_this;
	private MainFrame m_mframe;
	private Memory app_memory;
	private int memory_length = 4;
	private ArrayList<Instruction> instructionSet = new ArrayList<Instruction>();
	private Map<String, Register> basicRegisters = new HashMap<String, Register>();
	private int actual_base;
	private int bus_width = 10;
	private int memoryAddresWritingWidth;
	private static Base hexa = new Base(16, "0123456789ABCDEF",
			"[0-9,A-F,a-f]", new BaseTransform() {

				@Override
				public String transformValue(Long value) {
					return Long.toHexString(value);
				}
			});
	private int memoryRegisterWritingWidth;
	private static Base octal = new Base(8, "01234567", "[0-7]",
			(new BaseTransform() {

				@Override
				public String transformValue(Long value) {
					return Long.toOctalString(value);
				}
			}));

	private static Base binary = new Base(2, "01", "[0-1]",
			new BaseTransform() {

				@Override
				public String transformValue(Long value) {
					return Long.toBinaryString(value);
				}
			});
	private Base activeBase;

}
