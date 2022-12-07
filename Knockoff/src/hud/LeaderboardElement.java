package hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

import main.Handler;
import states.GameState;
import states.State;
import utils.Utils;

public class LeaderboardElement extends HudElement {

	ArrayList<LeaderboardSpot> spots;
	boolean newScore = false;
	String newName = "";
	
	int typeTicker = 10, typeCooldown = 10;
	int newGameTicker = 0, newGameCountdown = 600;

	public LeaderboardElement(Handler handler) {
		super(400, 100, 0, 0, handler);
		spots = new ArrayList<LeaderboardSpot>();
		readFromFile();
		// check if top 10 then do...
		checkIfTop10();
		//writeToFile();
	}

	public void readFromFile() {
		try {
			//FileReader reader = new FileReader("res/info/leaderboard.txt");
			
			InputStream sr = Utils.class.getResourceAsStream("/info/leaderboard.txt");
			InputStreamReader is = new InputStreamReader(sr);
			BufferedReader buffer = new BufferedReader(is);
			String line, result[] = new String[2];
			LeaderboardSpot spot;

			while ((line = buffer.readLine()) != null) {
				result = line.split("-");

				spot = new LeaderboardSpot(result[0], Integer.parseInt(result[1]));
				spots.add(spot);
			}
			spots.trimToSize();

			buffer.close();
			is.close();
			sr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkIfTop10() {
		int round = handler.getRoundLogic().getCurrentRound();
		LeaderboardSpot newSpot = new LeaderboardSpot("", round);
		if (spots.size() < 10) {
			ArrayList<LeaderboardSpot> temp = new ArrayList<LeaderboardSpot>();
			temp = spots;
			temp.add(newSpot);
			spots = temp;
			organize();
			newScore = true;
		} else if (spots.get(spots.size() - 1).round < round) {
			spots.set(spots.size() - 1, newSpot);
			organize();
			newScore = true;
		}

	}

	// implement your own sorting algorithm
	public void organize() {
		spots.sort(new Comparator<LeaderboardSpot>() {

			@Override
			public int compare(LeaderboardSpot o1, LeaderboardSpot o2) {
				if (o1.round > o2.round)
					return -1;
				else if (o1.round < o2.round)
					return 1;
				else
					return 0;
			}

		});

		for (int i = 0; i < spots.size(); i++) {
			System.out.println(spots.get(i).name + ", " + spots.get(i).round);
		}

	}

	public void writeToFile() {
		try {
			FileWriter writer = new FileWriter("res/leaderboard.txt");
			BufferedWriter buffer = new BufferedWriter(writer);
			for (int i = 0; i < spots.size(); i++) {
				buffer.write(spots.get(i).name);
				buffer.write("-");
				buffer.write(Integer.toString(spots.get(i).round));
				buffer.newLine();
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		typeTicker++;
		if (newScore && typeTicker >= typeCooldown) {
			
			String key = checkInput();
			if (key == "~") {
				if(newName != "") {
					for(LeaderboardSpot e: spots)
						if(e.name == "")
							e.name = newName;
					newScore = false;
					writeToFile();
				}
				
			} else if (key == "-") {
				if(newName != null  && (newName.length() == 1 || newName.length() == 0)) {
					typeTicker = 0;
					newName = "";
				}
				else if (newName != null) {
					typeTicker = 0;
					newName = newName.substring(0, newName.length() - 1);
				}

			} else {
				if(key != "") {
					typeTicker = 0;
				}
				if(newName.length() < 12) {
					newName += key;
				}
				
			}

		}
		else if(newScore) {
			
		}
		else {
			newGameTicker++;
			if(newGameTicker >= newGameCountdown) {
				handler.getGame().gameState = new GameState(handler);
				State.setState(handler.getGame().gameState);
			}
		}

	}

	public String checkInput() {
		if (handler.getKeyManager().a && handler.getKeyManager().sprint)
			return "A";
		if (handler.getKeyManager().b && handler.getKeyManager().sprint)
			return "B";
		if (handler.getKeyManager().c && handler.getKeyManager().sprint)
			return "C";
		if (handler.getKeyManager().d && handler.getKeyManager().sprint)
			return "D";
		if (handler.getKeyManager().e && handler.getKeyManager().sprint)
			return "E";
		if (handler.getKeyManager().f && handler.getKeyManager().sprint)
			return "F";
		if (handler.getKeyManager().g && handler.getKeyManager().sprint)
			return "G";
		if (handler.getKeyManager().h && handler.getKeyManager().sprint)
			return "H";
		if (handler.getKeyManager().i && handler.getKeyManager().sprint)
			return "I";
		if (handler.getKeyManager().j && handler.getKeyManager().sprint)
			return "J";
		if (handler.getKeyManager().k && handler.getKeyManager().sprint)
			return "K";
		if (handler.getKeyManager().l && handler.getKeyManager().sprint)
			return "L";
		if (handler.getKeyManager().m && handler.getKeyManager().sprint)
			return "M";
		if (handler.getKeyManager().n && handler.getKeyManager().sprint)
			return "N";
		if (handler.getKeyManager().o && handler.getKeyManager().sprint)
			return "O";
		if (handler.getKeyManager().p && handler.getKeyManager().sprint)
			return "P";
		if (handler.getKeyManager().q && handler.getKeyManager().sprint)
			return "Q";
		if (handler.getKeyManager().r && handler.getKeyManager().sprint)
			return "R";
		if (handler.getKeyManager().s && handler.getKeyManager().sprint)
			return "S";
		if (handler.getKeyManager().t && handler.getKeyManager().sprint)
			return "T";
		if (handler.getKeyManager().u && handler.getKeyManager().sprint)
			return "U";
		if (handler.getKeyManager().v && handler.getKeyManager().sprint)
			return "V";
		if (handler.getKeyManager().w && handler.getKeyManager().sprint)
			return "W";
		if (handler.getKeyManager().x && handler.getKeyManager().sprint)
			return "X";
		if (handler.getKeyManager().y && handler.getKeyManager().sprint)
			return "Y";
		if (handler.getKeyManager().z && handler.getKeyManager().sprint)
			return "Z";
		if (handler.getKeyManager().a)
			return "a";
		if (handler.getKeyManager().b)
			return "b";
		if (handler.getKeyManager().c)
			return "c";
		if (handler.getKeyManager().d)
			return "d";
		if (handler.getKeyManager().e)
			return "e";
		if (handler.getKeyManager().f)
			return "f";
		if (handler.getKeyManager().g)
			return "g";
		if (handler.getKeyManager().h)
			return "h";
		if (handler.getKeyManager().i)
			return "i";
		if (handler.getKeyManager().j)
			return "j";
		if (handler.getKeyManager().k)
			return "k";
		if (handler.getKeyManager().l)
			return "l";
		if (handler.getKeyManager().m)
			return "m";
		if (handler.getKeyManager().n)
			return "n";
		if (handler.getKeyManager().o)
			return "o";
		if (handler.getKeyManager().p)
			return "p";
		if (handler.getKeyManager().q)
			return "q";
		if (handler.getKeyManager().r)
			return "r";
		if (handler.getKeyManager().s)
			return "s";
		if (handler.getKeyManager().t)
			return "t";
		if (handler.getKeyManager().u)
			return "u";
		if (handler.getKeyManager().v)
			return "v";
		if (handler.getKeyManager().w)
			return "w";
		if (handler.getKeyManager().x)
			return "x";
		if (handler.getKeyManager().y)
			return "y";
		if (handler.getKeyManager().z)
			return "z";
		if (handler.getKeyManager().enter)
			return "~";
		if (handler.getKeyManager().backspace)
			return "-";
		else
			return "";
	}

	int transparency = 0;
	@Override
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
		if (newScore) {
			g.drawString("Enter Your name", (int) x, (int) y);
			g.drawString(newName, (int) x, (int) y + 100);
		}  
		else {
		

			g.drawString("Leaderboard", (int) x, (int) y);

			for (int i = 0; i < (spots.size()); i++) {
				if(i == 10) {
					break;
				}
				g.drawString(spots.get(i).name, (int) x - 70, (int) y + ((i + 1) * 40));
				g.drawString(Integer.toString(spots.get(i).round), (int) x + 250, (int) y + ((i + 1) * 40));
			}
		}
		
		
		if(newGameTicker > newGameCountdown - 255){
			
			Color color = new Color(0, 0, 0, transparency);
			transparency++;
			
			g.setColor(color);
			g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
			
		}

	}

}

class LeaderboardSpot {
	public String name;
	public int round;

	public LeaderboardSpot(String name, int round) {
		this.name = name;
		this.round = round;
	}
}
