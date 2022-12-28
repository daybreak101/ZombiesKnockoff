package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import main.Handler;
import ui.ClickListener;
import ui.TextButton;
import ui.UIManager;
import ui.UIObject;

public class SettingsState extends State {

	private UIManager uiManager;
	private ArrayList<TextButton> zoomLevelOptions;
	private ArrayList<TextButton> hudColorOptions;
	private ArrayList<TextButton> laserColorOptions;
	private ArrayList<TextButton> zombieCounterOptions;
	private ArrayList<TextButton> toggleCritOptions;
	private ArrayList<TextButton> toggleDamageOptions;
	private ArrayList<TextButton> healthBarOptions;
	
	int dw = 100, dh = 50;

	public SettingsState(Handler handler) {
		super(handler);
		zoomLevelOptions = new ArrayList<TextButton>();
		hudColorOptions = new ArrayList<TextButton>();
		laserColorOptions = new ArrayList<TextButton>();
		zombieCounterOptions = new ArrayList<TextButton>();
		toggleCritOptions = new ArrayList<TextButton>();
		toggleDamageOptions = new ArrayList<TextButton>();
		healthBarOptions = new ArrayList<TextButton>();
		uiManager = new UIManager(handler);
		addZoomLevelOptions();
		addHudColorOptions();
		addLaserColorOptions();
		addZombieCounterOptions();
		addToggleCritOptions();
		addToggleDamageOptions();
		addHealthBarOptions();
		uiManager.addObject(new TextButton(handler, 500, 700, 100, 50, "Back", new ClickListener() {

			@Override
			public void onClick() {
				handler.getSettings().writeToFile();
				handler.getMouseManager().setUIManager(null);
				if (handler.getWorld() != null)
					State.setState(new PauseState(handler));
				else
					State.setState(new MenuState(handler));

			}

		}));
		handler.getMouseManager().setUIManager(uiManager);

		int dw = 100, dh = 50;

	}
	
	public void addHealthBarOptions() {
		int y = 565;
		healthBarOptions.add(new TextButton(handler, 500, y, 50, dh, "Off", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(healthBarOptions);
				handler.getSettings().setHealthBar(false);
			}
		}));
		healthBarOptions.add(new TextButton(handler, 550, y, 45, dh, "On", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(healthBarOptions);
				handler.getSettings().setHealthBar(true);
			}
		}));

		uiManager.addObjects(healthBarOptions);
		boolean isOn = handler.getSettings().isHealthBar();
		int index;
		if (isOn == true)
			index = 1;
		else
			index = 0;
		healthBarOptions.get(index).setSelected(true);
	}
	
	public void addToggleDamageOptions() {
		int y = 515;
		toggleDamageOptions.add(new TextButton(handler, 500, y, 50, dh, "Off", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(toggleDamageOptions);
				handler.getSettings().setToggleDamage(false);
			}
		}));
		toggleDamageOptions.add(new TextButton(handler, 550, y, 45, dh, "On", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(toggleDamageOptions);
				handler.getSettings().setToggleDamage(true);
			}
		}));

		uiManager.addObjects(toggleDamageOptions);
		boolean isOn = handler.getSettings().isToggleDamage();
		int index;
		if (isOn == true)
			index = 1;
		else
			index = 0;
		toggleDamageOptions.get(index).setSelected(true);
	}
	
	public void addToggleCritOptions() {
		int y = 465;
		toggleCritOptions.add(new TextButton(handler, 500, y, 50, dh, "Off", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(toggleCritOptions);
				handler.getSettings().setToggleCrits(false);
			}
		}));
		toggleCritOptions.add(new TextButton(handler, 550, y, 45, dh, "On", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(toggleCritOptions);
				handler.getSettings().setToggleCrits(true);
			}
		}));

		uiManager.addObjects(toggleCritOptions);
		boolean isOn = handler.getSettings().isToggleCrits();
		int index;
		if (isOn == true)
			index = 1;
		else
			index = 0;
		toggleCritOptions.get(index).setSelected(true);
	}

	public void addZombieCounterOptions() {
		int y = 415;
		zombieCounterOptions.add(new TextButton(handler, 500, y, 50, dh, "Off", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zombieCounterOptions);
				handler.getSettings().setZombieCounter(false);
			}
		}));
		zombieCounterOptions.add(new TextButton(handler, 550, y, 45, dh, "On", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zombieCounterOptions);
				handler.getSettings().setZombieCounter(true);
			}
		}));

		uiManager.addObjects(zombieCounterOptions);
		boolean isOn = handler.getSettings().isZombieCounter();
		int index;
		if (isOn == true)
			index = 1;
		else
			index = 0;
		zombieCounterOptions.get(index).setSelected(true);
	}

	public void addHudColorOptions() {
		int y = 315;
		hudColorOptions.add(new TextButton(handler, 300, y, 75, dh, "Green", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.green);
			}
		}));

		hudColorOptions.add(new TextButton(handler, 375, y, 80, dh, "Yellow", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.yellow);
			}
		}));

		hudColorOptions.add(new TextButton(handler, 455, y, 58, dh, "Red", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.red);
			}
		}));

		hudColorOptions.add(new TextButton(handler, 513, y, 63, dh, "Blue", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.blue);
			}
		}));
		hudColorOptions.add(new TextButton(handler, 576, y, 100, dh, "Magenta", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.magenta);
			}
		}));
		hudColorOptions.add(new TextButton(handler, 676, y, 67, dh, "Cyan", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.cyan);
			}
		}));
		hudColorOptions.add(new TextButton(handler, 743, y, 86, dh, "Orange", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.orange);
			}
		}));
		hudColorOptions.add(new TextButton(handler, 829, y, 75, dh, "White", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.white);
			}
		}));

		uiManager.addObjects(hudColorOptions);
		Color currentColor = handler.getSettings().getHudColor();
		int index;
		if (currentColor == Color.green)
			index = 0;
		else if (currentColor == Color.yellow)
			index = 1;
		else if (currentColor == Color.red)
			index = 2;
		else if (currentColor == Color.blue)
			index = 3;
		else if (currentColor == Color.magenta)
			index = 4;
		else if (currentColor == Color.cyan)
			index = 5;
		else if (currentColor == Color.orange)
			index = 6;
		else if (currentColor == Color.white)
			index = 7;
		else
			index = 0;
		hudColorOptions.get(index).setSelected(true);
	}

	public void addLaserColorOptions() {
		int y = 365;
		laserColorOptions.add(new TextButton(handler, 300, y, 75, dh, "Green", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.green);
			}
		}));

		laserColorOptions.add(new TextButton(handler, 375, y, 80, dh, "Yellow", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.yellow);
			}
		}));

		laserColorOptions.add(new TextButton(handler, 455, y, 58, dh, "Red", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.red);
			}
		}));

		laserColorOptions.add(new TextButton(handler, 513, y, 63, dh, "Blue", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.blue);
			}
		}));
		laserColorOptions.add(new TextButton(handler, 576, y, 100, dh, "Magenta", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.magenta);
			}
		}));
		laserColorOptions.add(new TextButton(handler, 676, y, 67, dh, "Cyan", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.cyan);
			}
		}));
		laserColorOptions.add(new TextButton(handler, 743, y, 86, dh, "Orange", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.orange);
			}
		}));
		laserColorOptions.add(new TextButton(handler, 829, y, 75, dh, "White", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.white);
			}
		}));

		uiManager.addObjects(laserColorOptions);
		Color currentColor = handler.getSettings().getLaserColor();
		int index;
		if (currentColor == Color.green)
			index = 0;
		else if (currentColor == Color.yellow)
			index = 1;
		else if (currentColor == Color.red)
			index = 2;
		else if (currentColor == Color.blue)
			index = 3;
		else if (currentColor == Color.magenta)
			index = 4;
		else if (currentColor == Color.cyan)
			index = 5;
		else if (currentColor == Color.orange)
			index = 6;
		else if (currentColor == Color.white)
			index = 7;
		else
			index = 0;
		laserColorOptions.get(index).setSelected(true);
	}

	public void addZoomLevelOptions() {

		int y = 265;
		zoomLevelOptions.add(new TextButton(handler, 400, y, 60, dh, "1.0x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.0);
				if (handler.getPlayer() != null)
					handler.getGameCamera().centerOnEntity(handler.getPlayer());
			}
		}));
		zoomLevelOptions.add(new TextButton(handler, 460, y, 60, dh, "1.1x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.1);
				if (handler.getPlayer() != null)
					handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}
		}));
		zoomLevelOptions.add(new TextButton(handler, 520, y, 60, dh, "1.2x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.2);
				if (handler.getPlayer() != null)
					handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}
		}));
		zoomLevelOptions.add(new TextButton(handler, 580, y, 60, dh, "1.3x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.3);
				if (handler.getPlayer() != null)
					handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}
		}));
		zoomLevelOptions.add(new TextButton(handler, 640, y, 60, dh, "1.4x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.4);
				if (handler.getPlayer() != null)
					handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}
		}));
		zoomLevelOptions.add(new TextButton(handler, 700, y, 60, dh, "1.5x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.5);
				if (handler.getPlayer() != null)
					handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}
		}));

		uiManager.addObjects(zoomLevelOptions);

		int currentZoom = (int) Math.round((handler.getSettings().getZoomLevel() - 1) * 10);
		zoomLevelOptions.get(currentZoom).setSelected(true);
	}

	public void deselectAll(ArrayList<TextButton> objects) {
		for (TextButton o : objects) {
			o.setSelected(false);
		}
	}

	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		if (handler.getWorld() != null) {
			handler.getWorld().render(g);
		} else {
			g.setColor(Color.black);
			g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		}
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		uiManager.render(g);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 50));
		g.setColor(handler.getSettings().getHudColor());
		g.drawString("SETTINGS", handler.getWidth() / 2 - 160, 200);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
		g.drawString("Zoom Level", handler.getWidth() / 4 - 160, 300);
		g.drawString("HUD Color", handler.getWidth() / 4 - 160, 350);
		g.drawString("Laser Color", handler.getWidth() / 4 - 160, 400);
		g.drawString("Zombie Counter", handler.getWidth() / 4 - 160, 450);
		g.drawString("Toggle Crits", handler.getWidth() / 4 - 160, 500);
		g.drawString("Toggle Damage", handler.getWidth() / 4 - 160, 550);
		g.drawString("Zombie Health", handler.getWidth() / 4 - 160, 600);

		g.setColor(handler.getSettings().getLaserColor());
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);
	}

}
