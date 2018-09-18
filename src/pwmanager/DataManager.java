package pwmanager;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class DataManager implements Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Data> data;
	private ArrayList<String> descriptions;
	
	private transient FileOutputStream f;
	private transient ObjectOutputStream o;
	private transient FileInputStream fIn;
	private transient ObjectInputStream oIn;
	
	public DataManager() {
		
		data = new ArrayList<>();
		descriptions = new ArrayList<>();
		
	}
	
	public void addNewEntry(String desc, String username, String password, String pin) {
		
		Data inputData = new Data(desc, username, password, pin);
		data.add(inputData);
		descriptions.add(desc);
		
	}
	
	public void deleteEntry(int index) {
		
		data.remove(index);
		descriptions.remove(index);
		
	}
	
	public Data getData(int index) {
		
		return data.get(index);
		
	}
	
	public ArrayList<String> getDescriptions() {
		return descriptions;
	}
	
	public void save() {
		
		try {
			f = new FileOutputStream(new File("data"));
			o = new ObjectOutputStream(f);

			// Writes bag, player, shop
			o.writeObject(this);
			System.out.println("Wrote");
			o.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void retrieve() {
		
		try {
			fIn = new FileInputStream(new File("data"));
			oIn = new ObjectInputStream(fIn);

			DataManager retrievedObj = (DataManager)oIn.readObject();

			// Deep copy
			this.data = retrievedObj.getData();
			this.descriptions = retrievedObj.getDescriptions();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ArrayList<Data> getData() {
		return data;
	}

	public void setData(ArrayList<Data> data) {
		this.data = data;
	}

	public void setDescriptions(ArrayList<String> usernames) {
		this.descriptions = usernames;
	}
	
	
}
