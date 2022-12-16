package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import utils.Utils;

public class GlobalStats {

	private Handler handler;

	// global stats
	private long globalKills, globalDowns, totalGames;

	public GlobalStats(Handler handler) {
		this.handler = handler;
		readGlobalStats();

	}

	public void readGlobalStats() {
		String file = Utils.loadFileAsString("/info/globalStats.txt");
		String[] tokens = file.split("\\s+");

		if (tokens.length == 0) {
			globalKills = 0;
			globalDowns = 0;
			totalGames = 0;
			return;
		}
		globalKills = Utils.parseInt(tokens[0]);
		globalDowns = Utils.parseInt(tokens[1]);
		totalGames = Utils.parseInt(tokens[2]);
	}

	// make new file global stats
	public void writeToFile() {
		try {
			FileWriter writer = new FileWriter("res/info/globalStats.txt");
			BufferedWriter buffer = new BufferedWriter(writer);

			buffer.write(Long.toString(globalKills));
			buffer.newLine();
			buffer.write(Long.toString(globalDowns));
			buffer.newLine();
			buffer.write(Long.toString(totalGames));

			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void gainKill() {
		globalKills++;
	}
	
	public void gainDown() {
		globalDowns++;
	}
	
	public void addGame() {
		totalGames++;
	}
}
