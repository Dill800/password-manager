package pwmanager;

import java.io.Serializable;

public class Data implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	private String username;
	private String password;
	
	String[] chain;
	
	public Data(String description, String username, String password, String pin) {
		
		this.description = description;
		this.username = username;
		this.password = password;
		
		this.constructChain(pin);
		
		// Encrypts and saves password automatically
		this.encrypt();
	}
	
	public Data(String username, String password, String pin) {
		
		this("No Description", username, password, pin);
		
	}
	
	public String getEncryptedPassword() {
		return this.password;
	}
	
	private void encrypt() {
		
		String result = "";
		
		// password.length() and chain[x].length equal
		for(int i = 0; i < password.length(); i++) {
			
			int change = Integer.parseInt(chain[0].charAt(i) + "") - Integer.parseInt(chain[1].charAt(i) + "") + Integer.parseInt(chain[2].charAt(i) + "");

			result += (char)((int)password.charAt(i) + change);
			
			
		}
		
		this.password = result;
		
		
	}
	
	public String decrypt(String pin) {
		
		constructChain(pin);
		
		String result = "";
		
		for(int i = 0; i < password.length(); i++) {
			
			int change = Integer.parseInt(chain[0].charAt(i) + "") - Integer.parseInt(chain[1].charAt(i) + "") + Integer.parseInt(chain[2].charAt(i) + "");

			result += (char)((int)password.charAt(i) - change);
			
			
		}
		
		return result;
		
	}
	
	public void constructChain(String pin) {
		
		chain = new String[3];
		
		// First Link
		chain[0] = pin + "";
		
		// Second Link
		chain[1] = "";
		String pinSquared = (long)(Integer.parseInt(pin)) * (long)(Integer.parseInt(pin)) + "";
		for(int i = pinSquared.length() - pin.length(); i < pinSquared.length(); i++) {
			chain[1] += pinSquared.charAt(i);
		}
		
		// Third Link
		chain[2] = "";
		for(int i = 0; i < pin.length(); i++) {
			chain[2] += pin.charAt(pin.length() - i - 1);
		} 
		
		// Resize to pw
		for(int chainIndex = 0; chainIndex < 3; chainIndex++) {
			String newChain = "";
			
			for(int i = 0; i < this.password.length(); i++) {
				
				newChain += chain[chainIndex].charAt(i % chain[chainIndex].length());
				
				
			}
			
			chain[chainIndex] = newChain;
			
		}
		
		
	}
	
	public void printChain() {
		
		
		for(String e : chain) {
			System.out.println(e);
		}
		
	}

	public String getDescription() {
		return description;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	
}
