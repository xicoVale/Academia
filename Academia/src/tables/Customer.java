package tables;

import java.util.ArrayList;
import java.util.Iterator;

public class Customer extends Tables {
	private final int SIZE = 12;
	private ArrayList<String> attributes;
	
	public Customer(){
		setAtributes();
	}
	
	@Override
	public int getSize() {
		return SIZE;
	}
	@Override
	public ArrayList<String> getAtributes() {
		return this.attributes;
	}
	@Override
	public void setAtributes() {
		this.attributes = new ArrayList<String>();
	}
	public void register() {
		Iterator iterator = attributes.iterator();
		String query = "INSERT INTO customer (customerName, contactLastName, contactFistName, phone, adressLine1, adressLine2,";
		
		while (iterator.hasNext()) {
			
		}
	}
}
