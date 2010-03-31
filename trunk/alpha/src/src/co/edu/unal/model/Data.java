package src.co.edu.unal.model;
/* Esta clase almacena datos con y sin signo enteros para ser representados
 * en diversos sitemas numéricos (hexadecimal,binario y octal). 
 * La forma en la que se representa el signo (complemento a 2, bias, signo, etc)
 * no es de mayor importancia para esta clase pues solo almacena información
 * la forma de representar el signo ante el usuario debe estar a cargo de 
 * la clase que contenga a los objetos de esta clase.
 * Cuando devuelve un número negativo en cualquier sistema, lo devuelve con
 * un menos (-) como prefijo.
 * */
public class Data {
	public Data(Long data){
		m_data=data;
		isPositive=(m_data<0)?false:true;

	}
	public Data(String data, Base base){
		this(new Long("0"));
		data=data.toUpperCase();
		if((data.matches("-{0,1}([A-F,0-9]{1,})") && base.getBase() ==16) ||
				(data.matches("-{0,1}([0-7]{1,})") && base.getBase() ==8)||
				(data.matches("-{0,1}([0-1]{1,})") && base.getBase() ==2))
			m_data=fromBase(data, base.getBase());
		isPositive=(m_data<0)?false:true;
		//else throw exception ---
	}
	public Data(){
		this(new Long("0"));
	}
	public Long value(){
		return m_data;
	}

	public boolean isPositive() {
		return isPositive;
	}
	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}
	public void value(Data d){
		m_data=d.value();
	}
	public String Binary(){
//		if (m_data<0)
//			return "-" + Integer.toBinaryString((m_data*(-1)));
		return Long.toBinaryString(m_data);
	}

	public static Long fromBase(String number,int base){
		int sign = 1;
		if (number.matches("-{1}.*")){
			sign=-1;
			number = (String) number.subSequence(1, number.length());
		}
		long base10=new Long(0);
		int posTranslate;
		for (int i=number.length()-1,j=0;i>=0;i--,j++){
			for (posTranslate=0;posTranslate<translate.length;posTranslate++){
				if (number.charAt(i)==translate[posTranslate])
					break;
			}
			base10=base10+(long)(posTranslate*Math.pow(base,j));
		}
		return new Long(base10*sign);
	}
	private static char translate[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; 
	private Long m_data;
	private boolean isPositive;
}
