package src.co.edu.unal.model;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.model.RegisterListener.RegisterEvent;
import src.co.edu.unal.view.RegisterView;

public class Register implements FocusListener {

	public Register(int size) {
		this(size, true);
	}

	public Register(int size, boolean signed) {
		contained = new Data();
		setSize(size);
		setSigned(signed);
		m_listeners = new ArrayList<RegisterListener>();
	}

	public Register(Long value, int size, boolean signed) {
		this(size, signed);
		Data data = new Data(value);
		try {
			verifyLimits(data, size);
			Write(data);
		} catch (OutOfBounds e) {
			System.out.println("Number out of Bounds on Register declaration");
		}

	}

	private boolean verifyLimits(Data d, int size) throws OutOfBounds {
		if (isSigned()) {
			if ((d.value() < Math.pow(2, size - 1))
					&& (d.value() > ((-1) * Math.pow(2, size - 1))))
				return true;
			else
				throw new OutOfBounds();
		} else {
			if ((d.value() < Math.pow(2, size)))
				return true;
			else
				throw new OutOfBounds();
		}

	}

	public Register(String value, Base base, int size) {
		this(size);
		Write(new Data(value, base));
	}

	public Register(String value, int size) {
		this(size);
		Write(new Data(value, App.getBinary()));
	}
	public Data Read(boolean IASEvent) {
		if (IASEvent)
			notifyListeners(RegisterEvent.IASREAD);
		return Read();
		
	}
	public Data Read() {
		notifyListeners(RegisterEvent.READ);
		return contained;
	}
	public Data Read(int x, int y, boolean IASEvent) {
		if (IASEvent)
			notifyListeners(RegisterEvent.IASREAD);
		return Read(x, y);
	}

	public Data Read(int x, int y) {
		if (x < 0 || y < 0 || y >= bits_size) {
			System.out
					.println("Intentando leer un dato con y fuera de los límites");
			return null;
		}

		String reg = (contained.value() < 0) ? "1" : "0";

		for (int i = 0; i < bits_size; i++)
			reg += "0";

		reg = reg.substring(0, (bits_size - contained.Binary().length()))
				+ contained.Binary();
		
		
		
		Data ret = new Data(reg.substring(x, y + 1), App.getBinary());
		notifyListeners(RegisterEvent.READ);
		return ret;
	}
	
	public void Write(Data data,boolean IASEvent){
		Write(data);
		if (IASEvent)
			notifyListeners(RegisterEvent.IASWRITE);
	}

	public void Write(Data data) {
		try {
			if (verifyLimits(data, bits_size)) {
				contained.value(data);
				notifyListeners(RegisterEvent.WRITE);
			}
		} catch (Exception e) {
			System.out
					.println("Intentando escribir" + data.value() + " con Write() un número fuera de los límites y además isPositive="+data.isPositive());
			if (g_view!=null){
			g_view.setText(App.getInstance().getActiveBase().getTransformer()
					.transformValue(Read(0,getBits_size()-1).value()));
			}
		}

	}
	
	public void Write(Data data, int x,boolean IASEvent) {
		Write(data, x);
		if (IASEvent)
			notifyListeners(RegisterEvent.IASWRITE);
	}

	public void Write(Data data, int x) {
		Write(data, x, (x + data.Binary().length() - 1));
	}
	
	public void Write(Data data, int x, int y, boolean IASEvent) {
		Write(data,x,y);
		if (IASEvent)
			notifyListeners(RegisterEvent.IASWRITE);
	}

	public void Write(Data data, int x, int y) {
		String reg, inserting = data.Binary(), nreg = "0";

		reg = (contained.value() < 0) ? "1" : "0";

		for (int i = 1; i < bits_size; i++)
			reg += "0";

		reg = reg.substring(0, (bits_size - contained.Binary().length()))
				+ contained.Binary();

		for (int i = 1; i <= ((y - x) - data.Binary().length()); i++)
			inserting = "0" + inserting;
		if (data.isPositive())
			inserting = "0" + inserting;
		else
			inserting = "1" + inserting;

		if (data.Binary().length() - 1 <= (y - x) && x < bits_size
				&& y < bits_size) {

			if (x < y) {
				nreg = reg.substring(0, x) + inserting;
				if (y != bits_size - 1)
					nreg += reg.substring(y + 1);
			}
			if (nreg.charAt(0) == '1') {
				contained.setPositive(false);
				contained.value(new Data(nreg.substring(1), App.getBinary()));
			} else {
				contained.setPositive(true);
				contained.value(new Data(nreg, App.getBinary()));
			}
			notifyListeners(RegisterEvent.WRITE);
		}// TODO: Throw algo, pero las excepciones del write no estan manejadas
			// en ningún lado
	}

	private void setSize(int size) {
		this.bits_size = size;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public void addListener(RegisterListener dl) {
		assert dl != null;

		if (!m_listeners.contains(dl)) {
			m_listeners.add(dl);
		}
	}

	// not used in thBoundBoxis app
	public void removeListener(RegisterListener dl) {
		assert dl != null;

		m_listeners.remove(dl);
	}

	// observer design pattern
	public void notifyListeners(RegisterEvent de) {
		assert de != null;
		try {
			for (RegisterListener dl : m_listeners) {
				dl.processRegisterEvent(de);
			}
		} catch (Exception e) {
			System.out
					.println("Problema notificando a los listeners de Registos de un evento de "+ de.name());
		}

	}

	public int getBits_size() {
		return bits_size;
	}

	public RegisterView getG_view() {
		if (g_view == null) {
			g_view = new RegisterView(this);
		}
		return g_view;
	}

	public void setG_view(RegisterView gView) {
		g_view = gView;
	}

	private RegisterView g_view;

	@Override
	public void focusGained(FocusEvent e) {

	}

	@Override
	public void focusLost(FocusEvent e) {
		Write(new Data(g_view.getText(), App.getInstance().getActiveBase()));
	}

	private Data contained;
	private int bits_size;

	private boolean signed;
	// list of listeners
	private ArrayList<RegisterListener> m_listeners;

}
