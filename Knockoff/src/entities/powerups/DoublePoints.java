package entities.powerups;

import java.awt.Color;
import java.awt.Graphics;

import graphics.Assets;
import main.Handler;

public class DoublePoints extends PowerUps{

	public DoublePoints(Handler handler, float x, float y) {
		super(handler, x, y);
		name = "Double Points";
		icon = Assets.doublepoints;
		floatingAsset = null;
	}

	@Override
	public void unbuff() {
		handler.getRoundLogic().getPowerups().setDoublePointsActive(false);
	}

	@Override
	public void fulfillInteraction() {
		handler.getRoundLogic().getPowerups().setDoublePointsActive(true);
	}


	@Override
	public void render(Graphics g) {
		if(!pickedUp) {
			g.setColor(Color.blue);
			g.drawOval((int) (x - handler.getGameCamera().getxOffset()) , (int) (y - handler.getGameCamera().getyOffset()), width, height);
		}
	}	
}
