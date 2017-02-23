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
	public ArrayList<String> getAttributes() {
		return attributes;
	}

	@Override
	public void setAttributes() {
		this.attributes = new ArrayList<String>();
	}
	
}
