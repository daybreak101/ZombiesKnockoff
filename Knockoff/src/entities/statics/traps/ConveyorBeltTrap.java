package entities.statics.traps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import entities.blood.Blood;
import entities.creatures.Stoker;
import entities.creatures.Zombie;
import entities.creatures.ZombieType;
import main.Handler;

public class ConveyorBeltTrap extends Trap {

	private Rectangle furnace, conveyor;

	public ConveyorBeltTrap(Handler handler, float x, float y, float switchX, float switchY, int switchRotation) {
		super(handler, x, y, 200, 200, switchX, switchY, switchRotation, 5 * 60, 2000);
		cooldown = 30 * 60;
		furnace = new Rectangle((int) (x + bounds.x - 1), (int) (y + bounds.y - 1), bounds.width + 1,
				bounds.height + 1);
		conveyor = new Rectangle((int) (x - 600), (int) (y + 25), 600,
				150);
	}

	public void postTick() {
		if (cooldownTimer > cooldown) {
			activated = false;
		} else if (activated && cooldownTimer <= cooldown) {
			moveEntitiesTowardFurnace();
			killInArea();
		}
	}

	public void render(Graphics g) {
		//draw conveyor
		g.setColor(Color.gray);
		g.fillRect((int) (conveyor.x - handler.getGameCamera().getxOffset()), (int) (conveyor.y - handler.getGameCamera().getyOffset()),
				conveyor.width, conveyor.height);
		
		//draw furnace
		g.setColor(Color.black);
		g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height);
		if (!activated) {
			g.setColor(Color.gray);
			g.fillRect((int) (x - handler.getGameCamera().getxOffset()),
					(int) (y + 20 - handler.getGameCamera().getyOffset()), width/2, height - 40);
		} else {
			g.setColor(Color.orange);
			g.fillRect((int) (x - handler.getGameCamera().getxOffset()),
					(int) (y + 20 - handler.getGameCamera().getyOffset()), width/2, height - 40);
		}

	}

	public void killInArea() {
		if (handler.getPlayer().getHitbox().intersects(furnace)) {
			handler.getPlayer().takeDamage(handler.getPlayer().getHealth());
		}
		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (e.getHitBox(0, 0).intersects(furnace)) {
				if (e.getZombieType() == ZombieType.STOKER) {
					((Stoker) e).getAngry();
				} else {
					e.dieByTrap();
				}
			}
		}
	}
	
	public void moveEntitiesTowardFurnace() {
		if (handler.getPlayer().getHitbox().intersects(conveyor)) {
			handler.getPlayer().addToMoveX(2);
		}
		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (e.getHitBox(0, 0).intersects(conveyor)) {
				e.setxMove(7);
				e.moveX();
			}
		}
		for(Blood e: handler.getWorld().getEntityManager().getBlood()) {
			if(e.getBloodType() != -1 && e.getRect().intersects(conveyor)) {
				e.moveX();
			}
		}
	}

}
