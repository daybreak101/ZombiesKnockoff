package main;

import entities.creatures.Zombie;
import entities.statics.Wall;
import perks.Bandolier;
import perks.DeadShot;
import perks.DoubleTap;
import perks.Juggernaut;
import perks.Luna;
import perks.MuleKick;
import perks.PhD;
import perks.Revive;
import perks.SleightOfHand;
import perks.StaminUp;
import perks.Stronghold;
import perks.Vampire;
import weapons.AA12;
import weapons.AK47;
import weapons.AWP;
import weapons.Flamethrower;
import weapons.GrenadeLauncher;
import weapons.Gun;
import weapons.IceShotgun;
import weapons.Minigun;
import weapons.P90;
import weapons.RPD;
import weapons.RPG;
import weapons.Winchester1876;

public class Cheats {

	Handler handler;

	public Cheats(Handler handler) {
		this.handler = handler;
		applyCheats();
	}

	public void applyCheats() {
		// perks
		//handler.getPlayer().getInv().givePerk(new Bandolier(handler));
		//handler.getPlayer().getInv().givePerk(new DeadShot(handler, 3));
		handler.getPlayer().getInv().givePerk(new DoubleTap(handler, 3));
		handler.getPlayer().getInv().givePerk(new Juggernaut(handler, 3));
		//handler.getPlayer().getInv().givePerk(new Luna(handler));
		//handler.getPlayer().getInv().givePerk(new MuleKick(handler, 3));
		//handler.getPlayer().getInv().givePerk(new PhD(handler, 3));
		//handler.getPlayer().getInv().givePerk(new Revive(handler, 0));
		//handler.getPlayer().getInv().givePerk(new SleightOfHand(handler, 3));
		handler.getPlayer().getInv().givePerk(new StaminUp(handler, 3));
		//handler.getPlayer().getInv().givePerk(new Stronghold(handler, 3));
		handler.getPlayer().getInv().givePerk(new Vampire(handler, 3));

		// points
		int points = 0000;
		handler.getPlayer().getInv().gainPoints(points);

		// set round
		int round = 0;
		handler.getRoundLogic().setCurrentRound(round);

		// give gun
		Gun gun =
				//new AWP(handler);
				//new AK47(handler);
				//new RPD(handler);
				new GrenadeLauncher(handler);
				//new Winchester1876(handler);
				//new AA12(handler);
				//new RPG(handler);
				//new Flamethrower(handler);
				//new IceShotgun(handler);
				//new P90(handler);
				//new Minigun(handler);
		//gun.upgradeWeapon();
		handler.getPlayer().getInv().setGun(gun);
			
		//invisibleWalls();
		//nodesVisible();
	}
	
	public void tick() {
		//freezeZombies();	
	}
	
	public void freezeZombies() {
		for(Zombie z : handler.getWorld().getEntityManager().getZombies()) {
			z.getFreezeStatus().freeze();
		}
	}
	
	public void invisibleWalls() {
		for(Wall w : handler.getWorld().getEntityManager().getWalls()) {
			w.setVisible(false);
		}
	}
	
	public void nodesVisible() {
		handler.getWorld().showNodesAndEdges();
	}
}
