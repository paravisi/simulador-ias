package src.co.edu.unal.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.model.Register;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	public MainFrame() {

	}

	private void setDefaultSize() {
		Toolkit t = Toolkit.getDefaultToolkit();
		//Dimension ss = t.getScreenSize();

		//setBounds((ss.width - DEF_WIDTH) / 2, (ss.height - DEF_HEIGHT) / 2,
		//		DEF_WIDTH, DEF_HEIGHT);
		setBounds(new Rectangle(0, 0, 450, 80));
	}

	public void init() {
		setDefaultSize();
		for (Register reg : App.getInstance().getApp_memory().getRegisters()) {
			this.addComp(reg.getG_view());
		}
//		instruc = new InstructionSyntaxTextField(App.getInstance()
//				.getInstrucionSyntaxInfoList());
//		instruc.setFont(new Font("Arial", Font.BOLD, 15));
//		b.addActionListener(this);
//        checkPanel.add(instruc,BorderLayout.WEST);
//        checkPanel.add(b,BorderLayout.EAST);
		
        add(checkPanel, BorderLayout.CENTER);
		
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
