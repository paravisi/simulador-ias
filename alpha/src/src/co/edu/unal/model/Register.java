package src.co.edu.unal.model;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import src.co.edu.unal.controller.App;
import src.co.edu.unal.model.RegisterListener.RegisterEvent;
import src.co.edu.unal.view.RegisterView;

public class Register implements FocusListener{

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
		Write(new Data(value,App.getBinary()));
	}

	public Data Read() {
		notifyListeners(RegisterEvent.READ);
		return contained;
	}

	public Data Read(int x, int y) {
		if (x < 0 || y < 0 || y>=bits_size){
			System.out.println("Intentando leer un dato con y fuera de los límites");
			return null;
		}
		String reg = (contained.value() < 0) ? "1" : "0";

		for (int i = 1; i < bits_size; i++)
			reg += "0";

		reg = reg.substring(0, ((bits_size - 1) - contained.Binary().length()))
				+ contained.Binary();
		Data ret = new Data(reg.substring(x, y), App.getBinary());
		notifyListeners(RegisterEvent.READ);
		return ret;
	}

	public void Write(Data data) {
		Write(data, 0, bits_size-1);
	}

	public void Write(Data data, int x) {
		Write(data, x, (x + data.Binary().length() - 1));
	}

	public void Write(Data data, int x, int y) {
		String reg, inserting = data.Binary(), nreg = "0";

		reg = (contained.value() < 0) ? "1" : "0";

		for (int i = 0; i < bits_size; i++)
			reg += "0";

		reg = reg.substring(0, ((bits_size - 1) - contained.Binary().length()))
				+ contained.Binary();

		for (int i = 0; i <= ((y - x) - data.Binary().length()); i++)
			inserting = "0" + inserting;

		if (data.Binary().length()-1 <= (y - x) && x < bits_size && y < bits_size) {

			if (x < y) {
				nreg = reg.substring(0, x) + inserting;
				if (y != bits_size - 1)
					nreg += reg.substring(y + 1);
			}
			if (x == 0 && y == bits_size) {
				contained.setPositive(data.isPositive());
			}
			contained.value(new Data(nreg, App.getBinary()));
			notifyListeners(RegisterEvent.WRITE);
		}
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
		try{
			for (RegisterListener dl : m_listeners) {
				dl.processRegisterEvent(de);
			}
		}catch(Exception e){
			System.out.println("Problema notificando a los listeners de Registos");
		}
		
	}
	public int getBits_size() {
		return bits_size;
	}
	public RegisterView getG_view() {
		if (g_view==null){
			g_view=new RegisterView(this);
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
		Write(new Data(g_view.getText(),App.getInstance().getActiveBase()));
	}
	private Data contained;
	private int bits_size;

	private boolean signed;
	// list of listeners
	private ArrayList<RegisterListener> m_listeners;


}
