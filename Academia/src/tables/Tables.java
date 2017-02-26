package tables;

import java.util.ArrayList;

import dbconnect.DBConnect;

abstract public class Tables {

	public abstract int getSize();
	public abstract ArrayList<String> getAttributes();
	public abstract void setAttributes();
	public abstract void setConnection(DBConnect conn);
	public abstract DBConnect getConnection();
	public abstract void register();
}
