package maps;

import java.awt.Graphics;

import entities.Entity;
import graphics.Assets;
import main.Handler;

public class FactoryMap extends Entity{
	
	

	public FactoryMap(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, width, height);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.factoryMap,(int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height, null);
		
	}

}
