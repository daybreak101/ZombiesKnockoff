package entities.powerups;

import java.awt.Color;
import java.awt.Graphics;

import main.Handler;

public class InstaKill extends PowerUps{

	public InstaKill(Handler handler, float x, float y) {
		super(handler, x, y);
	}

	@Override
	public void unbuff() {
		handler.getRoundLogic().getPowerups().setInstakillActive(false);
	}

	@Override
	public void fulfillInteraction() {
		handler.getRoundLogic().getPowerups().setInstakillActive(true);
	}

	@Override
	public void render(Graphics g) {
		if(!pickedUp) {	
			g.setColor(Color.red);
			g.drawOval((int) (x - handler.getGameCamera().getxOffset()) , (int) (y - handler.getGameCamera().getyOffset()), width, height);
		}
	}	
	
}
