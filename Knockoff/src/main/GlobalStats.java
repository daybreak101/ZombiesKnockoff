package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import utils.Utils;

public class GlobalStats {

	private Handler handler;

	// global stats
	private long globalKills, globalHeadshots, globalDowns, totalGames, perksAte, perkSpins,
				boxSpins, boxPulls, gunsUpgraded, trapPulls;
	private double averageRound;

	public GlobalStats(Handler handler) {
		this.handler = handler;
		readGlobalStats();

	}

	public void readGlobalStats() {
		String file = Utils.loadFileAsString("/info/globalStats.txt");
		String[] tokens = file.split("\\s+");

		if (tokens.length == 0) {
			globalKills = 0;
			globalHeadshots = 0;
			globalDowns = 0;
			totalGames = 0;
			perksAte = 0;
			perkSpins = 0;
			boxPulls = 0;
			boxSpins = 0;
			gunsUpgraded = 0;
			trapPulls = 0;
			averageRound = 0;
			return;
		}
		globalKills = Utils.parseInt(tokens[0]);
		globalHeadshots = Utils.parseInt(tokens[1]);
		globalDowns = Utils.parseInt(tokens[2]);
		totalGames = Utils.parseInt(tokens[3]);
		perksAte = Utils.parseInt(tokens[4]);
		perkSpins = Utils.parseInt(tokens[5]);
		boxPulls = Utils.parseInt(tokens[6]);
		boxSpins = Utils.parseInt(tokens[7]);
		gunsUpgraded = Utils.parseInt(tokens[8]);
		trapPulls = Utils.parseInt(tokens[9]);
		averageRound = Utils.parseDouble(tokens[10]);
	}

	// make new file global stats
	public void writeToFile() {
		try {
			FileWriter writer = new FileWriter("res/info/globalStats.txt");
			BufferedWriter buffer = new BufferedWriter(writer);

			buffer.write(Long.toString(globalKills));
			buffer.newLine();
			buffer.write(Long.toString(globalHeadshots));
			buffer.newLine();
			buffer.write(Long.toString(globalDowns));
			buffer.newLine();
			buffer.write(Long.toString(totalGames));
			buffer.newLine();
			buffer.write(Long.toString(perksAte));
			buffer.newLine();
			buffer.write(Long.toString(perkSpins));
			buffer.newLine();
			buffer.write(Long.toString(boxPulls));
			buffer.newLine();
			buffer.write(Long.toString(boxSpins));
			buffer.newLine();
			buffer.write(Long.toString(gunsUpgraded));
			buffer.newLine();
			buffer.write(Long.toString(trapPulls));
			buffer.newLine();
			buffer.write(Double.toString(averageRound));
			
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

	public long getGlobalKills() {
		return globalKills;
	}

	public long getGlobalDowns() {
		return globalDowns;
	}

	public long getTotalGames() {
		return totalGames;
	}

	public long getPerksAte() {
		return perksAte;
	}

	public void addPerk() {
		this.perksAte++;
	}

	public long getPerkSpins() {
		return perkSpins;
	}

	public void addPerkSpin() {
		perkSpins++;
	}

	public long getBoxSpins() {
		return boxSpins;
	}

	public void addBoxSpin() {
		this.boxSpins++;
	}

	public long getBoxPulls() {
		return boxPulls;
	}

	public void addBoxPull() {
		this.boxPulls++;;
	}

	public long getGunsUpgraded() {
		return gunsUpgraded;
	}

	public void addGunUpgrade() {
		this.gunsUpgraded++;
	}

	public long getTrapPulls() {
		return trapPulls;
	}

	public void addTrapPull() {
		this.trapPulls++;
	}
	
	public double getAverageRound() {
		return averageRound;
	}

	public void calculateNewAverageRound(int newRound) {
		averageRound = ((averageRound * (totalGames - 1)) + newRound)/(totalGames);
	}

	public long getGlobalHeadshots() {
		return globalHeadshots;
	}

	public void addHeadshot() {
		this.globalHeadshots++;
	}
	
}
