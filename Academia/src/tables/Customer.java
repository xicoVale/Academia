package tables;

import java.util.ArrayList;

public class Customer extends Tables {
	private final int SIZE = 13;
	private ArrayList<String> attributes;
	
	public Customer(){}
	
	@Override
	public int getSize() {
		return SIZE;
	}
	@Override
	public ArrayList<String> getAtributes() {
		return this.attributes;
	}
	@Override
	public void setAtributes(ArrayList<String> attributes) {
		this.attributes = attributes;
	}
}
