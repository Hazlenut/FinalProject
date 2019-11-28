import java.util.ArrayList;

public class Concert extends Event {
	private int times;
	private String showName;
	public Concert() {
		
	}
	public Concert(String showName) {
		this.showName = showName;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
}
