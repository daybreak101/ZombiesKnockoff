package entities.statics;

import java.awt.Graphics;
import java.util.Random;

import graphics.Assets;
import main.Handler;
import perks.Bandolier;
import perks.DeadShot;
import perks.DoubleTap;
import perks.Juggernaut;
import perks.Luna;
import perks.MuleKick;
import perks.Perk;
import perks.PhD;
import perks.Revive;
import perks.SleightOfHand;
import perks.StaminUp;
import perks.Stronghold;
import perks.Vampire;

public class RandomPerk extends InteractableStaticEntity{

	private boolean isSpun;
	private Perk perk;
	private int isSpunTimer, isSpunTime;
	private boolean cantAfford = false;
	private boolean fullPerks = false;
	
	public RandomPerk(Handler handler, float x, float y) {
		super(handler, x, y, 75, 150);
		triggerText = "Press F to spin for a random perk: 2000";
		isSpun = false;
		isSpunTime = 1000;
	}

	@Override
	public void fulfillInteraction() {
		//spin for perk
		
		if(isSpun == false && cooldownTimer >= cooldown) {
			if(isSpun == false && !handler.getWorld().getEntityManager().getPlayer().getInv().checkPerkEmptySpot()) {
				fullPerks = true;
			}
			else if(handler.getWorld().getEntityManager().getPlayer().getInv().purchase(2000)) {
				isSpun = true;
				cantAfford = false;
				cooldownTimer = 0;
				perk = getRandomPerk();
				handler.getGlobalStats().addPerkSpin();
				
				
				//don't give a perk player already has
				while(handler.getWorld().getEntityManager().getPlayer().getInv().checkPerks(perk)) {
					perk = getRandomPerk();
				}
			}
			else {
				cantAfford = true;
				cooldownTimer = 0;
			}		
		}
		//grab perk
		else if(isSpun == true && cooldownTimer >= cooldown && isSpunTimer < isSpunTime) {
			cooldownTimer = 0;
			isSpun = false;
			isSpunTimer = 0;
			handler.getGlobalStats().addPerk();
			handler.getWorld().getEntityManager().getPlayer().getInv().addPerk(perk);
		}
		
	}
	
	public Perk getRandomPerk() {
		Random rand = new Random();
		int rng = rand.nextInt(12);
		
		switch(rng) {
		case 0:
			return new Juggernaut(handler, 0);
		case 1:
			return new SleightOfHand(handler, 0);
		case 2:
			return new DoubleTap(handler, 0);
		case 3:
			return new DeadShot(handler, 0);
		case 4:
			return new PhD(handler,0);
		case 5:
			return new StaminUp(handler,0);
		case 6:
			return new Vampire(handler,0);
		case 7:
			return new MuleKick(handler,0);
		//case 8:
		//	return new Bandolier(handler,0);
		//case 9:
			//return new Revive(handler,0);
		//case 10: 
		//	return new Luna(handler,0);
		case 11: 
			return new Stronghold(handler,0);
		}
		return new MuleKick(handler,3);
	}
	

	@Override
	public void postTick() {
	
		
		if(cantAfford == true && cooldownTimer < cooldown) {
			isSpunTimer = 0;
			triggerText = "Not enough points!";
		}
		else if(fullPerks == true && cooldownTimer < cooldown) {
			isSpunTimer = 0;
			triggerText = "Can only have four perks!";
		}	
		else if(isSpun == true && cooldownTimer >= cooldown) {
			triggerText = "Press F to pick up " + perk.getName();
			isSpunTimer++;
			if(isSpunTimer >= isSpunTime) {
				isSpun = false;
			}
		}
		else if(isSpun == false && cooldownTimer >= cooldown) {
			isSpunTimer = 0;
			triggerText = "Press F to spin for a random perk: 2000";
			perk = null;
		}
		else if(isSpun == true) {
			triggerText = "Spinning...";
		}
		else {
			triggerText = "";
		}
	}
	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.perkvendor, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null );
		
	}

}
