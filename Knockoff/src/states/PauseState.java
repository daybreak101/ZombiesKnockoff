package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import graphics.Assets;
import main.Handler;
import ui.ClickListener;
import ui.TextButton;
import ui.UIImageButton;
import ui.UIManager;

public class PauseState extends State {

	private UIManager uiManager;

	public PauseState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);

		uiManager.addObject(new TextButton(handler, 390, 350, 128, 64, "RESUME", 25, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(handler.getGame().gameState);

			}
		}));

		uiManager.addObject(new TextButton(handler, 390, 450, 128, 64, "SETTINGS", 25, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new SettingsState(handler));
			}
		}));

		uiManager.addObject(new TextButton(handler, 390, 550, 128, 64, "QUIT", 25, new ClickListener() {

			@Override
			public void onClick() {
				handler.getGlobalStats().calculateNewAverageRound(handler.getRoundLogic().getCurrentRound());
				handler.getGlobalStats().writeToFile();
				handler.getMouseManager().setUIManager(null);
				System.gc();
				handler.setWorld(null);
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
		handler.getWorld().render(g);
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 50));
		g.setColor(handler.getSettings().getHudColor());
		g.drawString("PAUSED", handler.getWidth() / 2 - 140, 200);
		uiManager.render(g);
		g.setColor(handler.getSettings().getLaserColor());
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);

		g.drawString(Integer.toString(handler.getProgression().getLevel()), 500, 700);
		g.drawString(Long.toString(handler.getProgression().getXP()), 600, 700);
		g.drawString("/ " + Long.toString(handler.getProgression().getXPNeeded()), 700, 700);

	}

}
