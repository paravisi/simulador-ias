package src.co.edu.unal.controller;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import src.co.edu.unal.model.Address;
import src.co.edu.unal.model.Base;
import src.co.edu.unal.model.BaseTransform;
import src.co.edu.unal.model.Data;
import src.co.edu.unal.model.Memory;
import src.co.edu.unal.model.Register;
import src.co.edu.unal.model.Statement;
import src.co.edu.unal.model.StatementSyntax;
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
	
	private void initializeBasicStatements(){
		basicStatements.add(new Statement(new StatementSyntax() {
			
			@Override
			public boolean match(String s) {
				return s.matches("INIT ("+App.getInstance().getActiveBase().getRegex()+"{1,"+App.getInstance().getMemoryAddresWritingWidth()+"}) ("+App.getInstance().getActiveBase().getRegex()+"{1,"+App.getInstance().getMemoryRegisterWritingWidth()+"})");
			}
		},new StatementCompiler() {
			
			@Override
			public void compile(String statement) {
				String[] arguments = statement.split(" ");
				Address position = new Address (new Data(arguments[1],App.getInstance().getActiveBase()),40);
				Data initValue = new Data(arguments[2],App.getInstance().getActiveBase());
				App.getInstance().getApp_memory().Write(position, initValue);		
			}
		},"INIT X VALUE"));
basicStatements.add(new Statement(new StatementSyntax() {
			
			@Override
			public boolean match(String s) {
				return s.matches("ORG ("+App.getInstance().getActiveBase().getRegex()+"{1,"+App.getInstance().getMemoryAddresWritingWidth()+"})");
			}
		},new StatementCompiler() {
			
			@Override
			public void compile(String statement) {
				String[] arguments = statement.split(" ");
				Data initPCValue = new Data(arguments[1],App.getInstance().getActiveBase());
				App.getInstance().getBasicRegisters().get("PC").Write(initPCValue);
				App.getInstance().setPCinitiated(true);
			}
		}, "ORG X"));
	}

	private void initializeBasicRegisters() {
		basicRegisters.put("AC", new Register(40));
		basicRegisters.put("MQ", new Register(40));
		basicRegisters.put("PC", new Register(10, false));
		basicRegisters.put("IR", new Register(8, false));
		basicRegisters.put("IBR", new Register(20, false));
	}

	private void initializeInstructionSet() {
		InstructionCompiler oneCompleteMemoryAddres= new InstructionCompiler() {
			
			@Override
			public Data compile(String instruction) {
				int start=instruction.indexOf("(")+1;
				int end=instruction.indexOf(")");
				return new Data(instruction.substring(start, end),App.getInstance().getActiveBase());
			}
		};
		InstructionCompiler oneHalfMemoryAddress = new InstructionCompiler() {
			
			@Override
			public Data compile(String instruction) {
				int start=instruction.indexOf("(")-1;
				int end=instruction.indexOf(",");
				return new Data(instruction.substring(start, end),App.getInstance().getActiveBase());
			}
		};
		InstructionCompiler noMemoryAddress = new InstructionCompiler() {
			
			@Override
			public Data compile(String instruction) {
				return new Data("0",App.getBinary());
			}
		};
		
		instructionSet.add(new Instruction(new Data("1", hexa),
				new InstructionSyntaxInformation("LOAD M", "\\)", "LOAD M(X)"),
				new InstructionExecuter() {
					@Override
					public boolean Execute(Address x) {

						App.getInstance().getBasicRegisters().get("AC").Write(
								App.getInstance().getApp_memory().Read(x));
						return true;
					}
				},oneCompleteMemoryAddres));
		instructionSet.add(new Instruction(new Data("9", hexa),
				new InstructionSyntaxInformation("LOAD MQ,M", "\\)",
						"LOAD MQ,M(X)"), new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {

						App.getInstance().getBasicRegisters().get("MQ").Write(
								App.getInstance().getApp_memory().Read(x));
						return true;
					}
				},oneCompleteMemoryAddres));
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
				},oneCompleteMemoryAddres));
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
				},oneCompleteMemoryAddres));
		instructionSet.add(new Instruction(new Data("A", hexa),
				new InstructionSyntaxInformation("LOAD MQ"),
				new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {

						App.getInstance().getBasicRegisters().get("AC").Write(App.getInstance().getBasicRegisters().get("MQ").Read());
						return true;
					}
				},oneCompleteMemoryAddres));
		instructionSet.add(new Instruction(new Data("2", hexa),
				new InstructionSyntaxInformation("LOAD -M","\\)","LOAD -M(x)"),
				new InstructionExecuter() {

					@Override
					public boolean Execute(Address x) {

						App.getInstance().getBasicRegisters().get("AC").Write(new Data(App.getInstance().getApp_memory().Read(x).value()*(-1)));
						return true;
					}
				},oneCompleteMemoryAddres));
		
			instructionSet.add(new Instruction(new Data("3", hexa),
					new InstructionSyntaxInformation("LOAD \\|M", "\\)\\|", "LOAD |M(X)|"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {


							App.getInstance().getBasicRegisters().get("AC").Write(new Data(Math.abs(App.getInstance().getApp_memory().Read(x).value()))
									);
							return true;
						}
					},oneCompleteMemoryAddres));
			instructionSet.add(new Instruction(new Data("4", hexa),
					new InstructionSyntaxInformation("LOAD -\\|M", "\\)\\|", "LOAD -|M(X)|"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("AC").Write(new Data(Math.abs(App.getInstance().getApp_memory().Read(x).value())*(-1))
									);
							return true;
						}
					},oneCompleteMemoryAddres));
			instructionSet.add(new Instruction(new Data("5", hexa),
					new InstructionSyntaxInformation("ADD M", "\\)", "ADD M(X)"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("AC").Write(new Data(
									App.getInstance().getApp_memory().Read(x).value()+App.getInstance().getBasicRegisters().get("AC").Read().value()));
							return true;
						}
					},oneCompleteMemoryAddres));
			instructionSet.add(new Instruction(new Data("7", hexa),
					new InstructionSyntaxInformation("ADD \\|M", "\\)\\|", "ADD |M(X)|"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("AC").Write(new Data(
									Math.abs(App.getInstance().getApp_memory().Read(x).value())+App.getInstance().getBasicRegisters().get("AC").Read().value()));
							return true;
						}
					},oneCompleteMemoryAddres));
			instructionSet.add(new Instruction(new Data("6", hexa),
					new InstructionSyntaxInformation("SUB M", "\\)", "SUB M(X)"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("AC").Write(new Data(
									App.getInstance().getApp_memory().Read(x).value()-App.getInstance().getBasicRegisters().get("AC").Read().value()));
							return true;
						}
					},oneCompleteMemoryAddres));
			instructionSet.add(new Instruction(new Data("8", hexa),
					new InstructionSyntaxInformation("SUB \\|M", "\\)\\|", "SUB |M(X)|"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("AC").Write(new Data(
									Math.abs(App.getInstance().getApp_memory().Read(x).value())-App.getInstance().getBasicRegisters().get("AC").Read().value()));
							return true;
						}
					},oneCompleteMemoryAddres));
			instructionSet.add(new Instruction(new Data("C", hexa),
					new InstructionSyntaxInformation("DIV M", "\\)", "DIV M(X)"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("MQ").Write(new Data(
									App.getInstance().getBasicRegisters().get("AC").Read().value()/App.getInstance().getApp_memory().Read(x).value()));
							App.getInstance().getBasicRegisters().get("AC").Write(new Data(
									App.getInstance().getBasicRegisters().get("AC").Read().value()%App.getInstance().getApp_memory().Read(x).value()));
							return true;
						}
					},oneCompleteMemoryAddres));
			instructionSet.add(new Instruction(new Data("14", hexa),
					new InstructionSyntaxInformation("LSH "),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("AC").Write(new Data(
									App.getInstance().getApp_memory().Read(x).value()*(2)));
							return true;
						}
					},noMemoryAddress));
			instructionSet.add(new Instruction(new Data("15", hexa),
					new InstructionSyntaxInformation("RSH "),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("AC").Write(new Data(
									App.getInstance().getApp_memory().Read(x).value()/(2)));
							return true;
						}
					},noMemoryAddress));
			instructionSet.add(new Instruction(new Data("D", hexa),
					new InstructionSyntaxInformation("JUMP M", ",0:19\\)", "JUMP M(X,0:19)"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {

							App.getInstance().getBasicRegisters().get("PC").Write(x.getDirection()
									);
							App.getInstance().getBasicRegisters().get("IBR").Write(new Data(new Long("0")));
							return true;
						}
					},oneHalfMemoryAddress));
			
			instructionSet.add(new Instruction(new Data("10", hexa),
					new InstructionSyntaxInformation("JUMP\\+ M", ",20:39\\)", "JUMP M(X,20:39)"),
					new InstructionExecuter() {
						@Override
						
						public boolean Execute(Address x) {

							if(App.getInstance().getBasicRegisters().get("AC").Read().value()>=0){
							
							App.getInstance().getBasicRegisters().get("PC").Write(x.getDirection()
									);
							x.setBeginning(20);
							x.setEnding(39);
							App.getInstance().getBasicRegisters().get("IBR").Write(App.getInstance().getApp_memory().Read(x));}
							return true;
						}
					},oneHalfMemoryAddress));
			
			instructionSet.add(new Instruction(new Data("E", hexa),
					new InstructionSyntaxInformation("JUMP M", ",20:39\\)", "JUMP M(X,20:39)"),
					new InstructionExecuter() {
						@Override
						
						public boolean Execute(Address x) {

							
							
							App.getInstance().getBasicRegisters().get("PC").Write(x.getDirection()
									);
							x.setBeginning(20);
							x.setEnding(39);
							App.getInstance().getBasicRegisters().get("IBR").Write(App.getInstance().getApp_memory().Read(x));
							return true;
						}
					},oneHalfMemoryAddress));
			
			
			
			instructionSet.add(new Instruction(new Data("F", hexa),
					new InstructionSyntaxInformation("JUMP\\+ M", ",0:19\\)", "JUMP M(X,0:19)"),
					new InstructionExecuter() {
						@Override
						
						public boolean Execute(Address x) {

							if(App.getInstance().getBasicRegisters().get("AC").Read().value()>=0){
							
							App.getInstance().getBasicRegisters().get("PC").Write(x.getDirection()
									);
							App.getInstance().getBasicRegisters().get("IBR").Write(new Data(new Long("0")));}
							return true;
						}
					},oneHalfMemoryAddress));
			
			instructionSet.add(new Instruction(new Data("12", hexa),
					new InstructionSyntaxInformation("STOR M", ",8:19\\)", "STOR M(X,8:19)"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {
							x.setBeginning(8);
							x.setEnding(19);
							App.getInstance().getApp_memory().Write(x, App.getInstance().getBasicRegisters().get("AC").Read(28,39));
							return true;
						}
					},oneHalfMemoryAddress));
			instructionSet.add(new Instruction(new Data("13", hexa),
					new InstructionSyntaxInformation("STOR M", ",28:39\\)", "STOR M(X,28:39)"),
					new InstructionExecuter() {
						@Override
						public boolean Execute(Address x) {
							x.setBeginning(28);
							x.setEnding(39);
							App.getInstance().getApp_memory().Write(x, App.getInstance().getBasicRegisters().get("AC").Read(28,39));
							return true;
						}
					},oneHalfMemoryAddress));
			
	}
		private App() {
		m_mframe = new MainFrame();
		app_memory = new Memory(40, memory_length);
		setActiveBase(hexa);


	}

	public Memory getApp_memory() {
		return app_memory;
	}

	public static void main(String[] args) {
		App.getInstance().run();

	}

	public void run() {
		initializeBasicRegisters();
		initializeInstructionSet();
		initializeBasicStatements();
		m_mframe.init();
		m_mframe.setTitle("IAS Simulator");
		m_mframe.setExtendedState(Frame.MAXIMIZED_BOTH);
		m_mframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_mframe.setVisible(true);

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
				/ (Math.log(activeBase.getBase()) / Math.log(2)));
		memoryRegisterWritingWidth = (int) Math.ceil(app_memory.getReg_size()
				/ (Math.log(activeBase.getBase()) / Math.log(2)));
		this.activeBase = activeBase;

	}

	public int getMemoryRegisterWritingWidth() {
		return memoryRegisterWritingWidth;
	}

	public boolean isPCinitiated() {
		return isPCinitiated;
	}

	public void setPCinitiated(boolean isPCinitiated) {
		this.isPCinitiated = isPCinitiated;
	}
	public int getMemoryAddresWritingWidth() {
		return memoryAddresWritingWidth;
	}

	public ArrayList<Instruction> getInstructionSet() {
		return instructionSet;
	}
public ArrayList<Statement> getBasicStatements() {
		return basicStatements;
	}
	private static App m_this;
	private MainFrame m_mframe;
	private Memory app_memory;
	private int memory_length = 150;
	private ArrayList<Instruction> instructionSet = new ArrayList<Instruction>();
	private ArrayList<Statement> basicStatements = new ArrayList<Statement>();
	

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
	private boolean isPCinitiated=false;
	
}
