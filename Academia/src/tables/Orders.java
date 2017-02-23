package tables;

import java.util.ArrayList;

public class Orders extends Tables {
	private final int SIZE = 7;
	private ArrayList<String> attributes;
	
	public Orders(){}

	@Override
	public int getSize() {
		return SIZE;
	}

	@Override
	public ArrayList<String> getAtributes() {
		return attributes;
	}

	@Override
	public void setAtributes(ArrayList<String> attributes) {
		this.attributes = attributes;
	}
	
}
