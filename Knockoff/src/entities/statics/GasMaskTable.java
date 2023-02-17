package entities.statics;

import java.awt.Graphics;

import entities.creatures.playerinfo.GasMask;
import graphics.Assets;
import main.Handler;

public class GasMaskTable extends InteractableStaticEntity {

	private boolean cantAfford = false;
	
	//has gas mask means that they have full durability on gas mask
	private boolean hasGasMask = false;
	
	
	public GasMaskTable(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, width, height);
		triggerText = "Press F to refill gas mask: 1000";
	}
	
	@Override
	public void render(Graphics g) {
		//change asset
		g.drawImage(Assets.ammoBox, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null);
		
	}

	public void fulfillInteraction() {
		// && gas mask is not full
		GasMask gasMask = handler.getPlayer().getInv().getGasMask();
		if(cooldownTimer >= cooldown ) {
			cooldownTimer = 0;
			if(handler.getPlayer().getInv().purchase(1000) && gasMask.getCurrentDurability() < gasMask.getMaxDurability()) {
				gasMask.repairMask();;
				cantAfford = false;
			}
			else {
				cantAfford = true;
				cooldownTimer = 0;
			}
			
		}
		
		// already has full has mask
		else if(gasMask.getCurrentDurability() >= gasMask.getMaxDurability()) {
			cooldownTimer = 0;
			hasGasMask = true;
		}
		
		
	}

	@Override
	public void postTick() {
		
		if(cantAfford && cooldownTimer < cooldown) {
			triggerText = "Not enough points!";
		}
		else if(hasGasMask && cooldownTimer < cooldown) {
			triggerText = "Already have gas mask!";
		}
		else if(cooldownTimer >= cooldown) {
			hasGasMask = false;
			triggerText = "Press F to refill gas mask: 1000";
		}
		else {
			triggerText = "";
		}
	}

	
}
