package states;

import java.awt.Color;
import java.awt.Graphics;

import main.Handler;
import ui.ClickListener;
import ui.TextButton;
import ui.UIManager;

public class LobbyState extends State{

	private UIManager uiManager;
	
	public LobbyState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		
		uiManager.addObject(new TextButton(handler, 700,700,300,100, "Launch", 30, new ClickListener() {

			@Override
			public void onClick() {
				handler.getGlobalStats().addGame();
				handler.getMouseManager().setUIManager(null);
				handler.getGame().gameState = new GameState(handler);
				State.setState(handler.getGame().gameState);
				
			}}));
		uiManager.addObject(new TextButton(handler, 100,700,300,100, "Back to Menu", 30, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new MenuState(handler));
				
			}}));
		uiManager.addObject(new TextButton(handler, 100,50,300,100, "Change Map", 30, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new MenuState(handler));
				
			}}));
		uiManager.addObject(new TextButton(handler, 100,150,300,100, "Armory", 30, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new MenuState(handler));
				
			}}));
		uiManager.addObject(new TextButton(handler, 100,250,300,100, "Upgrades", 30, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new UpgradesState(handler));
				
			}}));
		uiManager.addObject(new TextButton(handler, 100,350,300,100, "Blessings", 30, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new UpgradesState(handler));
				
			}}));
		uiManager.addObject(new TextButton(handler, 100,450,300,100, "Leaderboard", 30, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new UpgradesState(handler));
				
			}}));
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
		
		g.drawString("Golden Coins: " + handler.getProgression().getCoins(), 500, 600);
		
		g.setColor(handler.getSettings().getLaserColor());
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);
		
	}

}
