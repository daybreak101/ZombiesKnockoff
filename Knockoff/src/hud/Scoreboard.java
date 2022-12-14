package hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import entities.creatures.playerinfo.Stats;
import main.Handler;

public class Scoreboard extends HudElement {
	
	private Color hudColor;
	private Stats stats;

	public Scoreboard(Handler handler) {
		super(400, 100, 0, 0, handler);
		
	}

	@Override
	public void tick() {
		stats = handler.getPlayer().getStats();
		hudColor = handler.getSettings().getHudColor();

	}

	@Override
	public void render(Graphics g) {
		g.setColor(hudColor);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
		g.drawString("Score", (int) x, (int) y);
		g.drawString("Kills", (int) x + 100, (int) y);
		g.drawString("Downs", (int) x + 200, (int) y);

		
		g.drawString(String.valueOf(stats.getScore()), (int) x, (int) y + 100);
		g.drawString(String.valueOf(stats.getKills()), (int) x + 100, (int) y + 100);
		g.drawString(String.valueOf(stats.getDowns()), (int) x + 200, (int) y + 100);

	}
	
}