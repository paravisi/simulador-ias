package co.edu.unal.model;

import org.omg.IOP.ExceptionDetailMessage;

public class Register {

	public Register(int size){
		this(size,true);
	}
	public Register(int size,boolean signed) {
		contained = new Data();
		setSize(size);
		setSigned(signed);
	}
	public Register(int value, int size, boolean signed){
		this(size,signed);
		Data data = new Data(value);
		try{
			verifyLimits(data, size);
			Write(data);
		}
		catch(OutOfBounds e){
			System.out.println("Number out of Bounds on Register declaration");
		}
		
	}
	
	private boolean verifyLimits(Data d,int size) throws OutOfBounds{
		if (isSigned()){
			if ((d.value()<Math.pow(2, size-1)) && (d.value()<((-1)*Math.pow(2, size-1))))
				return true;
			else
				throw new OutOfBounds();
		}
		else{
			if ((d.value()<Math.pow(2, size)))
					return true;
			else
				throw new OutOfBounds();
		}
		
		
	}
	
	public Register(String value,int base,int size){
		this(size);
		Write(new Data(value,base));
	}
	public Register(String value,int size){
		this(size);
		Write(new Data(value,2));
	}
	
	public Data Read(){
		return contained;
	}

	public Data Read(int x, int y){
		if (x<0 || y<0)
			return null;
		String reg=(contained.value()<0)?"1":"0";
		
		for (int i=1;i<bits_size;i++)
			reg+="0";

		reg = reg.substring(0, ((bits_size-1)-contained.Binary().length()))+contained.Binary();
		Data ret= new Data(reg.substring(x, y),2);
		return ret;
	}
	
	public void Write(Data data) {
		contained.value(data);
	}
	
	public void Write(Data data,int x){
		Write(data,x,(x+data.Binary().length()-1));
	}

	public void Write(Data data, int x, int y){
		String reg,inserting=data.Binary(),nreg="0";
		
		reg=(contained.value()<0)?"1":"0";
		
		for (int i=1;i<bits_size;i++)
			reg+="0";

		reg = reg.substring(0, ((bits_size-1)-contained.Binary().length()))+contained.Binary();

		for(int i=0;i<=((y-x)-data.Binary().length());i++)
			inserting="0"+inserting;
		
		if(data.Binary().length()<=(y-x) && x<bits_size && y<bits_size){
			
			if (x<y){
				nreg=reg.substring(0,x)+inserting;
				if(y!=bits_size-1)
					nreg+=reg.substring(y+1);
			}
			if (x==0 && y==bits_size){
				contained.setPositive(data.isPositive());
			}
			Write(new Data(nreg,2));
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

	Data contained;
	int bits_size;
	boolean signed;

}
