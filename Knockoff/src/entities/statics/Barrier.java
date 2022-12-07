package entities.statics;

import java.awt.Graphics;

import graphics.Assets;
import main.Handler;

public class Barrier extends InteractableStaticEntity {

	private int health;
	private boolean broken;
	
	public Barrier(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, width, height);
		broken = false;
		triggerText = "Press F to repair barricade";
	}

	@Override
	public void render(Graphics g) {
		if(broken)
			g.drawImage(Assets.stone, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null );
		else
			g.drawImage(Assets.mysteryBox, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null );
		
	}

}
