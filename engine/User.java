package engine;

public class User {
	private String username;
	private String password;
	// if you want to make a continue as guest option
	private boolean isGuest;
	private int factor;
	public User() {
		isGuest = true;
	}
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		isGuest = false;
		factor = 5;
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getIsGuest() {
		return isGuest;
	}
	public void setIsGuest(boolean guest) {
		this.isGuest = guest;
	}
	public int getFactor() {
		return factor;
	}
	public void setFactor(int factor) {
		this.factor = factor;
	}
	
	
	
}
