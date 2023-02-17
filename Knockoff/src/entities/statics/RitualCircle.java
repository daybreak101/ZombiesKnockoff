package entities.statics;

import java.awt.Graphics;
import java.util.Random;

import graphics.Assets;
import main.Handler;
import utils.Timer;


public class RitualCircle extends InteractableStaticEntity {

	private int soulsFed;
	private String blessing;
	private boolean cantAfford = false;
	private Timer pickupTimer;
	private boolean canPickup;
	
	public RitualCircle(Handler handler, float x, float y) {
		super(handler, x, y, 0, 0);
		triggerText = "Souls for a blessing";
		blessing = "";
		soulsFed = 0;
		canPickup = false;
		pickupTimer = new Timer(600);
	}
	
	public void feedSoul() {
		soulsFed++;
	}
	
	//spin for random blessing
	@Override
	public void fulfillInteraction() {
		//spin for blessing
		if(!canPickup && cooldownTimer >= cooldown) {
			
			if(handler.getPlayer().getInv().purchase(500)) {
				canPickup = true;
				cantAfford = false;
				cooldownTimer = 0;
				blessing = getRandomBlessing();
				
				//change to blessing spins
				//handler.getGlobalStats().addBoxSpin();
				
			}
			else {
				cantAfford = true;
				cooldownTimer = 0;
			}
			
		}
		//grab blessing
		else if(canPickup && cooldownTimer >= cooldown && !pickupTimer.isReady()) {
			cooldownTimer = 0;
			canPickup = false;
			pickupTimer.resetTimer();
			handler.getPlayer().getInv().getBlessings().setBlessing(blessing);
			//change to blessings pulled
			//handler.getGlobalStats().addBoxPull();
		}
		
	}
	
	public String getRandomBlessing() {
		Random rand = new Random();
		int rng = rand.nextInt(5);
		
		switch(rng) {
		case 0:
			return "Double Time";
		case 1:
			return "Infinite Supply";
		case 2:
			return "No Mercy";
		case 3:
			return "Death Machine!";
		case 4:
			return "Crawl Space";
		default:
			return "HP up";

		}
	}
	
	@Override
	public void postTick() {
		if(cantAfford && cooldownTimer < cooldown) {
			pickupTimer.resetTimer();;
			triggerText = "Not enough points!";
		}	
		else if(canPickup && cooldownTimer >= cooldown) {
			triggerText = "Press F to trade blessing for " + blessing;
			pickupTimer.tick();
			if(pickupTimer.isReady()) {
				canPickup = false;
			}
		}
		else if(!canPickup && cooldownTimer >= cooldown) {
			pickupTimer.resetTimer();;
			triggerText = "Press F to spin for a random blessing: 500";
			blessing = "";
		}
		else if(canPickup) {
			triggerText = "Calling...";
		}
		else {
			triggerText = "";
		}
	}

	@Override
	public void render(Graphics g) {
		g.fillOval((int) (x - 100 - handler.getGameCamera().getxOffset()), (int) (y - 100 - handler.getGameCamera().getyOffset()), 200, 200);
		
	}

}
