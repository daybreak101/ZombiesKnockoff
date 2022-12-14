package hud;

import java.awt.Graphics;
import java.util.ArrayList;

import main.Handler;

public class HudManager {

	private Handler handler;
	private ArrayList<HudElement> elements;
	private GameplayElement gameplayHUD;
	private Scoreboard scoreboard;

	public HudManager(Handler handler) {
		this.handler = handler;
		scoreboard = new Scoreboard(handler);
		scoreboard.isVisible = false;
		gameplayHUD = new GameplayElement(handler);
		gameplayHUD.isVisible = true;
		elements = new ArrayList<HudElement>();
	}

	public void tick() {
		if (scoreboard.isVisible)
			scoreboard.tick();
		if (gameplayHUD.isVisible)
			gameplayHUD.tick();
		for (int i = 0; i < elements.size(); i++) {
			HudElement e = elements.get(i);
			e.tick();
			if (!e.isActive) {
				elements.remove(e);
			}
		}
	}

	public void render(Graphics g) {
		for (HudElement o : elements) {
			if (o.isVisible)
				o.render(g);
		}
		if (scoreboard.isVisible)
			scoreboard.render(g);
		if (gameplayHUD.isVisible)
			gameplayHUD.render(g);

	}

	public void setInvisible() {
		scoreboard.isVisible = false;
		gameplayHUD.isVisible = false;
		for (HudElement o : elements) {
			o.isVisible = false;
		}
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public ArrayList<HudElement> getObjects() {
		return elements;
	}

	public void addObject(HudElement o) {
		elements.add(o);
	}

	public void removeObject(HudElement o) {
		elements.remove(o);
	}

	public GameplayElement getGameplayHUD() {
		return gameplayHUD;
	}

	public void setGameplayHUD(GameplayElement gameplayHUD) {
		this.gameplayHUD = gameplayHUD;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

}
