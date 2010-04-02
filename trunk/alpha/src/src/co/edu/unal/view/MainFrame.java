package src.co.edu.unal.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import src.co.edu.unal.controller.action.CompileAll;
import src.co.edu.unal.controller.action.ExecuteAll;
import src.co.edu.unal.controller.action.ExecuteOne;
import src.co.edu.unal.controller.action.InitMemoryAndRegisters;
import src.co.edu.unal.controller.action.VerifyDocumentSyntax;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	public MainFrame() {

	}

	private void setDefaultSize() {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension ss = t.getScreenSize();

		setBounds((ss.width - DEF_WIDTH) / 2, (ss.height - DEF_HEIGHT) / 2,
				DEF_WIDTH, DEF_HEIGHT);
		setBounds(new Rectangle(0, 0, 1500, 800));
	}

	public void init() {
		
		
		setDefaultSize();
		JTextArea code = new JTextArea(100,50);
		code.setDocument(new CodeDocument(code));
		
		MemoryView memView= new MemoryView();
		JScrollPane scrolledMemory = new JScrollPane(memView);

		JSplitPane memAndRegs =  new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrolledMemory, new BasicRegisterView());
	
		memAndRegs.setDividerLocation(700);
		
		JPanel buttons = new JPanel();
		JButton exec = new JButton(new ExecuteAll());
		JButton execOne = new JButton(new ExecuteOne());
		JButton syntax = new JButton(new VerifyDocumentSyntax(code));
		JButton compiler = new JButton(new CompileAll(code));
		JButton initMem = new JButton(new InitMemoryAndRegisters());
		
		buttons.add(initMem);
		buttons.add(syntax);
		buttons.add(compiler);
		buttons.add(execOne);
		buttons.add(exec);
		
		
		
		code.setBorder(new LineNumberedBorder(LineNumberedBorder.LEFT_JUSTIFY, LineNumberedBorder.RIGHT_SIDE));
		JScrollPane scrolledCode = new JScrollPane(code);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrolledCode,memAndRegs);
		splitPane.setDividerLocation(900);
		
		
		add(buttons,BorderLayout.NORTH);
		add(splitPane,BorderLayout.CENTER);

		

		
//		instruc = new InstructionSyntaxTextField(App.getInstance()
//				.getInstrucionSyntaxInfoList());
//		instruc.setFont(new Font("Arial", Font.BOLD, 15));
//		b.addActionListener(this);
//        checkPanel.add(instruc,BorderLayout.WEST);
//        checkPanel.add(b,BorderLayout.EAST);
		
        
		
	}
	
	public void addComp(Component com){
		checkPanel.add(com);
		this.update(getGraphics());
	}
	
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		verifySyntax();
//	}
//	public void verifySyntax(){
//		if (instruc.verifySyntax()){
//			b.setIcon(new ImageIcon(getClass().getClassLoader().getResource("co/edu/unal/view/resources/rigth.png")));
//		}
//		else
//			b.setIcon(new ImageIcon(getClass().getClassLoader().getResource("co/edu/unal/view/resources/wrong.png")));
//	}
//	JButton b = new JButton(new ImageIcon(getClass().getClassLoader().getResource("co/edu/unal/view/resources/wrong.png")));
//	InstructionSyntaxTextField instruc;
	public static final int DEF_WIDTH = 800;
	public static final int DEF_HEIGHT = 600;
	JPanel checkPanel = new JPanel(new GridLayout(0, 1));

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
