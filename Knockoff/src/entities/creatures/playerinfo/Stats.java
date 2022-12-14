package entities.creatures.playerinfo;

import main.Handler;

public class Stats {

	private Handler handler;
	private int kills, downs, score;
	
	public Stats(Handler handler) {
		this.handler = handler;
		kills = 0;
		score = 0;
		downs = 0;
	}
	
	public void gainKill() {
		kills++;
	}
	
	public void gainScore(int dScore) {
		score += dScore;
	}
	
	public void gainDown() {
		downs++;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDowns() {
		return downs;
	}

	public void setDowns(int downs) {
		this.downs = downs;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


}
