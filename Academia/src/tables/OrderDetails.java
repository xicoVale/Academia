package tables;

import java.util.ArrayList;

public class OrderDetails extends Tables {
	private final int SIZE = 5;
	private ArrayList<String> attributes;
	
	public OrderDetails(){}
	
	@Override
	public int getSize() {
		return SIZE;
	}
	@Override
	public ArrayList<String> getAttributes() {
		return this.attributes;
	}
	@Override
	public void setAttributes() {
		this.attributes = new ArrayList<String>();
	}
}
