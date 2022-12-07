package states;

import main.Handler;
import perks.DoubleTap;
import perks.Stronghold;
import perks.Vampire;
import weapons.Gun;
import weapons.IceShotgun;

public class Cheats {

	Handler handler;

	public Cheats(Handler handler) {
		this.handler = handler;
		applyCheats();
	}

	public void applyCheats() {
		// perks
		// handler.getPlayer().getInv().addPerk(new Bandolier(handler));
		//handler.getPlayer().getInv().addPerk(new DeadShot(handler));
		handler.getPlayer().getInv().addPerk(new DoubleTap(handler));
		// handler.getPlayer().getInv().addPerk(new Juggernaut(handler));
		//handler.getPlayer().getInv().addPerk(new Luna(handler));
		// handler.getPlayer().getInv().addPerk(new MuleKick(handler));
		//handler.getPlayer().getInv().addPerk(new PhD(handler));
		// handler.getPlayer().getInv().addPerk(new Revive(handler));
		//handler.getPlayer().getInv().addPerk(new SleightOfHand(handler));
		// handler.getPlayer().getInv().addPerk(new StaminUp(handler));
		handler.getPlayer().getInv().addPerk(new Stronghold(handler));
		handler.getPlayer().getInv().addPerk(new Vampire(handler));

		// points
		int points = 10000;
		handler.getPlayer().gainPoints(points);

		// set round
		int round = 10;
		handler.getRoundLogic().setCurrentRound(round);

		// give gun
		//Gun gun = new RPG(handler);
		//Gun gun = new Flamethrower(handler);
		Gun gun = new IceShotgun(handler);
		gun.upgradeWeapon();
		handler.getPlayer().getInv().setGun(gun);

	}
}
