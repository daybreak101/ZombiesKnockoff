package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import utils.Utils;

public class Progression {
	private Handler handler;
	private long xp;
	private int level;
	private int coins;
	private long xpNeeded;

	public Progression(Handler handler) {
		this.handler = handler;
		xp = 0;
		level = 1;
		coins = 0;
		readProgression();
		calculateXPNeeded();
	}

	private void readProgression() {
		String file = Utils.loadFileAsString("/info/progression.txt");
		String[] tokens = file.split("\\s+");

		if (tokens.length == 0) {
			xp = 0;
			level = 1;
			coins = 0;
		} else {
			xp = Utils.parseInt(tokens[0]);
			level = Utils.parseInt(tokens[1]);
			coins = Utils.parseInt(tokens[2]);
		}
	}

	public void writeToFile() {
		try {
			FileWriter writer = new FileWriter("res/info/progression.txt");
			BufferedWriter buffer = new BufferedWriter(writer);

			buffer.write(Long.toString(xp));
			buffer.newLine();
			buffer.write(Integer.toString(level));
			buffer.newLine();
			buffer.write(Integer.toString(coins));

			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 100 per kill
	// 150 per headshot kill
	// 5 repair barricade
	// 100 * round survived
	public void gainXP(int xp) {
		this.xp += xp;
		checkLevel();
		writeToFile();
	}

	private void checkLevel() {
		if (xp >= xpNeeded) {
			level++;
			coins++;
			xp = xp - xpNeeded;
			calculateXPNeeded();
			checkLevel();
		}
	}

	private void calculateXPNeeded() {
		xpNeeded = (long) (1000 * Math.pow(level, 1.1) + 1000);
	}

	public int getCoins() {
		return coins;
	}

	public int getLevel() {
		return level;
	}

	public long getXP() {
		return xp;
	}

	public long getXPNeeded() {
		return xpNeeded;
	}

}
