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

	int dw = 100, 
	dh = 50;
	
	public SettingsState(Handler handler) {
		super(handler);
		zoomLevelOptions = new ArrayList<TextButton>();
		hudColorOptions = new ArrayList<TextButton>();
		laserColorOptions = new ArrayList<TextButton>();
		zombieCounterOptions = new ArrayList<TextButton>();
		uiManager = new UIManager(handler);
		addZoomLevelOptions();
		addHudColorOptions();
		addLaserColorOptions();
		addZombieCounterOptions();
		uiManager.addObject(new TextButton(handler, 500, 700, 100, 50, "Back", new ClickListener() {

			@Override
			public void onClick() {
				handler.getSettings().writeToFile();
				handler.getMouseManager().setUIManager(null);
				State.setState(new PauseState(handler));
				
			}
			
		}));
		handler.getMouseManager().setUIManager(uiManager);
		
		int dw = 100, 
		dh = 50;
	
		
		
//		uiManager.addObject(new TextButton(handler, 400,365,dw,dh, "Here", new ClickListener() {
//
//			@Override
//			public void onClick() {
//				
//			}}));
//		
//		uiManager.addObject(new TextButton(handler, 400,415,dw,dh, "Here", new ClickListener() {
//
//			@Override
//			public void onClick() {
//				
//			}}));
//		
//		uiManager.addObject(new TextButton(handler, 400,465,dw,dh, "Here", new ClickListener() {
//
//			@Override
//			public void onClick() {
//				
//			}}));
//		
//		uiManager.addObject(new TextButton(handler, 400,515,dw,dh, "Here", new ClickListener() {
//
//			@Override
//			public void onClick() {
//				
//			}}));
	}
	
	public void addZombieCounterOptions() {
		zombieCounterOptions.add(new TextButton(handler, 500,515,50,dh, "Off", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zombieCounterOptions);
				handler.getSettings().setZombieCounter(false);
			}}));
		zombieCounterOptions.add(new TextButton(handler, 550,515,45,dh, "On", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zombieCounterOptions);
				handler.getSettings().setZombieCounter(true);
			}}));
		
		uiManager.addObjects(zombieCounterOptions);
		boolean isOn = handler.getSettings().isZombieCounter();
		int index;
		if(isOn == true)
			index = 1;
		else
			index = 0;
		zombieCounterOptions.get(index).setSelected(true);
	}
	
	public void addHudColorOptions() {
		hudColorOptions.add(new TextButton(handler, 300,415,75,dh, "Green", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.green);
			}}));
		
		hudColorOptions.add(new TextButton(handler, 375,415,80,dh, "Yellow", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.yellow);
			}}));
		
		hudColorOptions.add(new TextButton(handler, 455,415,58,dh, "Red", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.red);
			}}));
		
		hudColorOptions.add(new TextButton(handler, 513,415,63,dh, "Blue", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.blue);
			}}));
		hudColorOptions.add(new TextButton(handler, 576,415,100,dh, "Magenta", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.magenta);
			}}));
		hudColorOptions.add(new TextButton(handler, 676,415,67,dh, "Cyan", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.cyan);
			}}));
		hudColorOptions.add(new TextButton(handler, 743,415,86,dh, "Orange", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.orange);
			}}));
		hudColorOptions.add(new TextButton(handler, 829,415,75,dh, "White", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(hudColorOptions);
				handler.getSettings().setHudColor(Color.white);
			}}));
		
		uiManager.addObjects(hudColorOptions);
		Color currentColor = handler.getSettings().getHudColor();
		int index;
		if(currentColor == Color.green)
			index = 0;
		else if(currentColor == Color.yellow)
			index = 1;
		else if(currentColor == Color.red)
			index = 2;
		else if(currentColor == Color.blue)
			index = 3;
		else if(currentColor == Color.magenta)
			index = 4;
		else if(currentColor == Color.cyan)
			index = 5;
		else if(currentColor == Color.orange)
			index = 6;
		else if(currentColor == Color.white)
			index = 7;
		else
			index = 0;
		hudColorOptions.get(index).setSelected(true);
	}
	
	public void addLaserColorOptions() {
		laserColorOptions.add(new TextButton(handler, 300,465,75,dh, "Green", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.green);
			}}));
		
		laserColorOptions.add(new TextButton(handler, 375,465,80,dh, "Yellow", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.yellow);
			}}));
		
		laserColorOptions.add(new TextButton(handler, 455,465,58,dh, "Red", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.red);
			}}));
		
		laserColorOptions.add(new TextButton(handler, 513,465,63,dh, "Blue", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.blue);
			}}));
		laserColorOptions.add(new TextButton(handler, 576,465,100,dh, "Magenta", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.magenta);
			}}));
		laserColorOptions.add(new TextButton(handler, 676,465,67,dh, "Cyan", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.cyan);
			}}));
		laserColorOptions.add(new TextButton(handler, 743,465,86,dh, "Orange", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.orange);
			}}));
		laserColorOptions.add(new TextButton(handler, 829,465,75,dh, "White", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(laserColorOptions);
				handler.getSettings().setLaserColor(Color.white);
			}}));
		
		uiManager.addObjects(laserColorOptions);
		Color currentColor = handler.getSettings().getLaserColor();
		int index;
		if(currentColor == Color.green)
			index = 0;
		else if(currentColor == Color.yellow)
			index = 1;
		else if(currentColor == Color.red)
			index = 2;
		else if(currentColor == Color.blue)
			index = 3;
		else if(currentColor == Color.magenta)
			index = 4;
		else if(currentColor == Color.cyan)
			index = 5;
		else if(currentColor == Color.orange)
			index = 6;
		else if(currentColor == Color.white)
			index = 7;
		else
			index = 0;
		laserColorOptions.get(index).setSelected(true);
	}
	
	public void addZoomLevelOptions() {
		
		zoomLevelOptions.add(new TextButton(handler, 400,365,60,dh, "1.0x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.0);
				handler.getGameCamera().centerOnEntity(handler.getPlayer());
			}}));
		zoomLevelOptions.add(new TextButton(handler, 460,365,60,dh, "1.1x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.1);
				handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}}));
		zoomLevelOptions.add(new TextButton(handler, 520,365,60,dh, "1.2x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.2);
				handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}}));
		zoomLevelOptions.add(new TextButton(handler, 580,365,60,dh, "1.3x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.3);
				handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}}));
		zoomLevelOptions.add(new TextButton(handler, 640,365,60,dh, "1.4x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.4);
				handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}}));
		zoomLevelOptions.add(new TextButton(handler, 700,365,60,dh, "1.5x", new ClickListener() {

			@Override
			public void onClick() {
				deselectAll(zoomLevelOptions);
				handler.getSettings().setZoomLevel(1.5);
				handler.getGameCamera().centerOnEntity(handler.getPlayer());

			}}));
		
		uiManager.addObjects(zoomLevelOptions);
		
		int currentZoom = (int)Math.round( (handler.getSettings().getZoomLevel() - 1) * 10);
		zoomLevelOptions.get(currentZoom).setSelected(true);
	}
	
	public void deselectAll(ArrayList<TextButton> objects) {
		for(TextButton o: objects) {
			o.setSelected(false);
		}
	}


	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		handler.getWorld().render(g);
		g.setColor(new Color(0,0,0,150));
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		uiManager.render(g);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 50)); 
		g.setColor(handler.getSettings().getHudColor());
		g.drawString("SETTINGS", handler.getWidth()/2 - 160, 200);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30)); 
		g.drawString("Zoom Level", handler.getWidth()/4 - 160, 400);
		g.drawString("HUD Color", handler.getWidth()/4 - 160, 450);
		g.drawString("Laser Color", handler.getWidth()/4 - 160, 500);
		g.drawString("Zombie Counter", handler.getWidth()/4 - 160, 550);

		
		
		
		g.setColor(handler.getSettings().getLaserColor());
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);
	}

}