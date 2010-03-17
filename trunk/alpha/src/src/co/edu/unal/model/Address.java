package src.co.edu.unal.model;

public class Address {
	public Address(Data dir,int addresed_register_size){
		if (dir.isPositive())
			direction=dir;
		else
			direction=null;
		
		beginning=0;
		ending=addresed_register_size-1;
	}
	public Address(Data dir,int x, int y){
		this(dir,y+1);
		beginning=x;
	}
	public Data getDirection() {
		return direction;
	}
	public void setDirection(Data direction) {
		if (direction.isPositive())
			this.direction = direction;
	}
	public int getBeginning() {
		return beginning;
	}
	public void setBeginning(int beginning) {
		if (beginning>=0)
			this.beginning = beginning;
	}
	public int getEnding() {
		return ending;
	}
	public void setEnding(int ending) {
		if (ending>=0)
			this.ending = ending;
	}
	private Data direction;
	private int beginning;
	private int ending;
}
