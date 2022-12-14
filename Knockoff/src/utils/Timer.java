package utils;

public class Timer {

	public int limit;
	public int counter;
	
	public Timer(int limit) {
		this.limit = limit;
	}
	
	public void tick() {
		counter++;
	}
	
	public void resetTimer() {
		counter = 0;
	}
	
	public boolean isReady() {
		if(counter >= limit) {
			counter = 0;
			return true;
		}
		return false;
	}
}
