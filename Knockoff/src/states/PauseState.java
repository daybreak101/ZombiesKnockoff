package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import graphics.Assets;
import main.Handler;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;

public class PauseState extends State{
	
	private UIManager uiManager;

	public PauseState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		
		uiManager.addObject(new UIImageButton(400,400,128,64, Assets.btn_start, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(handler.getGame().gameState);
				
			}}));
		
		uiManager.addObject(new UIImageButton(400,550,128,64, Assets.btn_quit, new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				System.gc();
				State.setState(new MenuState(handler));
				
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
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 50)); 
		g.setColor(Color.green);
		g.drawString("PAUSED", handler.getWidth()/2 - 140, 200);
		uiManager.render(g);
		g.setColor(Color.RED);
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);
		
	}

}