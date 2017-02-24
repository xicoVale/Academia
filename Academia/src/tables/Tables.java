package tables;

import java.util.ArrayList;

import dbconnect.DBConnect;

abstract public class Tables {

	public abstract int getSize();
	public abstract ArrayList<String> getAttributes();
	public abstract void setAttributes();
	public abstract void setConn(DBConnect conn);
	public abstract DBConnect getConn();
	public abstract void register();
}
