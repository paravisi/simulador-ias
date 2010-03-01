package co.edu.unal.controller;

import java.awt.Frame;

import co.edu.unal.view.MainFrame;

public class App {
	public synchronized static App getInstance() {
		while (m_this == null) {
			m_this = new App();
		}

		return m_this;
	}
	
	private App(){
		m_mframe = new MainFrame();
	}

	public static void main(String[] args) {
		App.getInstance().run();

	}

	public void run() {
		m_mframe.init();
		m_mframe.setTitle("IAS Simulator");
		m_mframe.setExtendedState(Frame.MAXIMIZED_BOTH);
		m_mframe.setVisible(true);
	}

	private static App m_this;
	private MainFrame m_mframe;

}
