package entities.statics;

import java.awt.Graphics;

import graphics.Assets;
import main.Handler;

public class AmmoRefill extends InteractableStaticEntity {
	
	private boolean cantAfford = false;
	private boolean fullAmmo = false;

	public AmmoRefill(Handler handler, float x, float y) {
		super(handler, x, y, 75, 75);
		triggerText = "Press F to refill current weapon: 1000";
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.ammoBox, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null);
		
	}

	public void fulfillInteraction() {
		if(cooldownTimer >= cooldown &&
				!(handler.getWorld().getEntityManager().getPlayer().getInv().getGun().getCurrentReserve() == handler.getWorld().getEntityManager().getPlayer().getInv().getGun().getMaxReserve())) {
			cooldownTimer = 0;
			if(handler.getWorld().getEntityManager().getPlayer().getInv().purchase(1000)) {
				handler.getWorld().getEntityManager().getPlayer().getInv().purchaseAmmo();
				cantAfford = false;
			}
			else {
				cantAfford = true;
				cooldownTimer = 0;
			}
			
		}
		if(cooldownTimer >= cooldown && (handler.getPlayer().getInv().getGun().getName() == "Flamethrower" || handler.getPlayer().getInv().getGun().getName() == "HotBox") && 
				!(handler.getPlayer().getInv().getGun().getCurrentClip() == handler.getPlayer().getInv().getGun().getClip())) {
			cooldownTimer = 0;
			if(handler.getWorld().getEntityManager().getPlayer().getInv().purchase(1000)) {
				handler.getWorld().getEntityManager().getPlayer().getInv().purchaseAmmo();
				cantAfford = false;
			}
			else {
				cantAfford = true;
				cooldownTimer = 0;
			}
			
		}
		else if(handler.getWorld().getEntityManager().getPlayer().getInv().getGun().getCurrentReserve() == handler.getWorld().getEntityManager().getPlayer().getInv().getGun().getMaxReserve()) {
			cooldownTimer = 0;
			fullAmmo = true;
		}
		
		
	}

	@Override
	public void postTick() {
		
		if(cantAfford && cooldownTimer < cooldown) {
			triggerText = "Not enough points!";
		}
		else if(fullAmmo && cooldownTimer < cooldown) {
			triggerText = "Full on ammo!";
		}
		else if(cooldownTimer >= cooldown) {
			fullAmmo = false;
			triggerText = "Press F to refill current weapon: 1000";
		}
		else {
			triggerText = "";
		}
	}

}
