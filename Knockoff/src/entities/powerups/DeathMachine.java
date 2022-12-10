package entities.powerups;

import java.awt.Color;
import java.awt.Graphics;

import graphics.Assets;
import main.Handler;

public class DeathMachine extends PowerUps {

	public DeathMachine(Handler handler, float x, float y) {
		super(handler, x, y);
		name = "Death Machine";
		icon = Assets.deathmachine;
		floatingAsset = null;
	}

	@Override
	public void unbuff() {
		handler.getRoundLogic().getPowerups().setDeathMachineActive(false);
		handler.getPlayer().getInv().switchWeapon();
		
	}

	@Override
	public void fulfillInteraction() {
		handler.getRoundLogic().getPowerups().setDeathMachineActive(true);
		handler.getPlayer().getInv().setCurrentGun(3);
	}

	@Override
	public void render(Graphics g) {
		if(!pickedUp) {
			g.setColor(Color.pink);
			g.drawOval((int) (x - handler.getGameCamera().getxOffset()) , (int) (y - handler.getGameCamera().getyOffset()), width, height);
		}
	}

}
