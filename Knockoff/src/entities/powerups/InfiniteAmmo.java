package entities.powerups;

import java.awt.Color;
import java.awt.Graphics;

import graphics.Assets;
import main.Handler;

public class InfiniteAmmo extends PowerUps {

	public InfiniteAmmo(Handler handler, float x, float y) {
		super(handler, x, y);
		name = "Infinite Ammo";
		icon = Assets.infiniteammo;
		floatingAsset = null;
	}

	@Override
	public void unbuff() {
		handler.getRoundLogic().getPowerups().setInfiniteAmmoActive(false);
	
	}

	@Override
	public void fulfillInteraction() {
		handler.getRoundLogic().getPowerups().setInfiniteAmmoActive(true);
		handler.getWorld().getEntityManager().getPlayer().infiniteAmmo();
	}

	@Override
	public void render(Graphics g) {
		if(!pickedUp) {	
			g.setColor(Color.magenta);
			g.drawOval((int) (x - handler.getGameCamera().getxOffset()) , (int) (y - handler.getGameCamera().getyOffset()), width, height);
		}
	}

}
