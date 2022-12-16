package states;

import java.awt.Color;
import java.awt.Graphics;

import main.Handler;
import ui.ClickListener;
import ui.TextButton;
import ui.UIManager;

public class MenuState extends State{
	
	private UIManager uiManager;

	public MenuState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		
		uiManager.addObject(new TextButton(handler, 300,400,300,100, "Start Game", 50, new ClickListener() {

			@Override
			public void onClick() {
				handler.getGlobalStats().addGame();
				handler.getMouseManager().setUIManager(null);
				handler.getGame().gameState = new GameState(handler);
				State.setState(handler.getGame().gameState);
				
			}}));
		
	}
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.black);
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		uiManager.render(g);
		g.setColor(handler.getSettings().getLaserColor());
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);
		
	}

}
