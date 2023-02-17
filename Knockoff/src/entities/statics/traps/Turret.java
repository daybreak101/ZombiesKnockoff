package entities.statics.traps;

import java.awt.Color;
import java.awt.Graphics;

import main.Handler;
import weapons.Gun;
import weapons.Minigun;

public class Turret extends Trap{
	Minigun gun = new Minigun(handler, true);
	
	public Turret(Handler handler, float x, float y, float switchX, float switchY,
			int switchRotation) {
		super(handler, x, y, 50, 50, switchX, switchY, switchRotation, 40 * 60, 1500);
		cooldown = 30 * 60;
	}
	
	public void postTick() {
		if(cooldownTimer > cooldown) {
			activated = false;
		}
		else if(activated && cooldownTimer <= cooldown) {
			gun.tick();
			killInArea();
		}
	}
	
	public void render(Graphics g) {
			g.setColor(Color.gray);
			g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height);

	}

	public void killInArea() {
		gun.shootAsTurret(x, y);
	}

}
