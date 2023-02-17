package entities.powerups;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import main.Handler;
import perks.DeadShot;
import perks.DoubleTap;
import perks.Juggernaut;
import perks.MuleKick;
import perks.Perk;
import perks.PhD;
import perks.SleightOfHand;
import perks.StaminUp;
import perks.Stronghold;
import perks.Vampire;

public class PerkBag extends PowerUps{

	public PerkBag(Handler handler, float x, float y) {
		super(handler, x, y);
		name = "Perk Bag";
		icon = null;
		floatingAsset = null;
	}
	
	public void tick() {
		cooldownTimer++;
		trigger = new Rectangle((int) (x), (int) (y), width, height);
		
		if(cooldownTimer >= cooldown) {
			handler.getWorld().getEntityManager().getPowerups().remove(this);
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}
		else if(pickedUp) {
			fulfillInteraction();
			handler.getWorld().getEntityManager().getPowerups().remove(this);
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}
		else if(cooldownTimer >= cooldown) {
			handler.getWorld().getEntityManager().getPowerups().remove(this);
			handler.getWorld().getEntityManager().getEntities().remove(this);
		}
		else if(!pickedUp) {
			checkPickedUp();
		}
	}
	
	@Override
	public void fulfillInteraction() {
		Perk perk = getRandomPerk();
		while(handler.getPlayer().getInv().checkPerks(perk)) {
			perk = getRandomPerk();
		}
		handler.getPlayer().getInv().givePerk(perk);
		
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
	public void render(Graphics g) {
		if(!pickedUp) {
			g.setColor(Color.CYAN);
			g.drawOval((int) (x - handler.getGameCamera().getxOffset()) , (int) (y - handler.getGameCamera().getyOffset()), width, height);
		}
	}
}
