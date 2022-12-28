package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;

import main.Handler;
import ui.ClickListener;
import ui.TextButton;
import ui.UIManager;

public class StatsState extends State {

	private UIManager uiManager;

	public StatsState(Handler handler) {
		super(handler);

		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);

		uiManager.addObject(new TextButton(handler, 500, 700, 100, 50, "Back", new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new MenuState(handler));

			}

		}));

	}

	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		uiManager.render(g);

		int yStart = 150;

		g.setColor(handler.getSettings().getHudColor());
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
		g.drawString("Total Games", handler.getWidth() / 4 - 160, yStart);
		g.drawString("Total Kills", handler.getWidth() / 4 - 160, yStart + 50);
		g.drawString("Total Headshots", handler.getWidth() / 4 - 160, yStart + 100);
		g.drawString("Total Downs", handler.getWidth() / 4 - 160, yStart + 150);
		g.drawString("Perks Ate", handler.getWidth() / 4 - 160, yStart + 200);
		g.drawString("Perk Spins", handler.getWidth() / 4 - 160, yStart + 250);
		g.drawString("Box Pulls", handler.getWidth() / 4 - 160, yStart + 300);
		g.drawString("Box Spins", handler.getWidth() / 4 - 160, yStart + 350);
		g.drawString("Traps Used", handler.getWidth() / 4 - 160, yStart + 400);
		g.drawString("K/D Ratio", handler.getWidth() / 4 - 160, yStart + 450);
		g.drawString("Average Round", handler.getWidth() / 4 - 160, yStart + 500);

		g.drawString(Long.toString(handler.getGlobalStats().getTotalGames()), handler.getWidth() / 2 - 160, 
				yStart);
		g.drawString(Long.toString(handler.getGlobalStats().getGlobalKills()), handler.getWidth() / 2 - 160,
				yStart + 50);
		g.drawString(Long.toString(handler.getGlobalStats().getGlobalHeadshots()), handler.getWidth() / 2 - 160,
				yStart + 100);
		g.drawString(Long.toString(handler.getGlobalStats().getGlobalDowns()), handler.getWidth() / 2 - 160,
				yStart + 150);
		g.drawString(Long.toString(handler.getGlobalStats().getPerksAte()), handler.getWidth() / 2 - 160,
				yStart + 200);
		g.drawString(Long.toString(handler.getGlobalStats().getPerkSpins()), handler.getWidth() / 2 - 160,
				yStart + 250);
		g.drawString(Long.toString(handler.getGlobalStats().getBoxPulls()), handler.getWidth() / 2 - 160,
				yStart + 300);
		g.drawString(Long.toString(handler.getGlobalStats().getBoxSpins()), handler.getWidth() / 2 - 160,
				yStart + 350);
		g.drawString(Long.toString(handler.getGlobalStats().getTrapPulls()), handler.getWidth() / 2 - 160,
				yStart + 400);

		DecimalFormat df = new DecimalFormat("0.00");
		if (handler.getGlobalStats().getGlobalDowns() <= 0) {
			g.drawString(df.format(
					(double) handler.getGlobalStats().getGlobalKills()),
					handler.getWidth() / 2 - 160, yStart + 450);
		} else {
			g.drawString(df.format(
					(double) handler.getGlobalStats().getGlobalKills() / handler.getGlobalStats().getGlobalDowns()),
					handler.getWidth() / 2 - 160, yStart + 450);
		}
		g.drawString(df.format(handler.getGlobalStats().getAverageRound()), handler.getWidth() / 2 - 160,
				yStart + 500);
		
		g.setColor(handler.getSettings().getLaserColor());
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);
	}

}
