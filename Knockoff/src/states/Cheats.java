package states;

import main.Handler;
import perks.DeadShot;
import perks.DoubleTap;
import perks.Juggernaut;
import perks.PhD;
import perks.StaminUp;
import perks.Stronghold;
import perks.Vampire;
import weapons.AA12;
import weapons.Flamethrower;
import weapons.GrenadeLauncher;
import weapons.Gun;
import weapons.IceShotgun;
import weapons.Minigun;
import weapons.P90;
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
		// handler.getPlayer().getInv().addPerk(new Bandolier(handler));
		//handler.getPlayer().getInv().addPerk(new DeadShot(handler));
		//handler.getPlayer().getInv().addPerk(new DoubleTap(handler));
		//handler.getPlayer().getInv().addPerk(new Juggernaut(handler));
		//handler.getPlayer().getInv().addPerk(new Luna(handler));
		// handler.getPlayer().getInv().addPerk(new MuleKick(handler));
		//handler.getPlayer().getInv().addPerk(new PhD(handler));
		// handler.getPlayer().getInv().addPerk(new Revive(handler));
		//handler.getPlayer().getInv().addPerk(new SleightOfHand(handler));
		//handler.getPlayer().getInv().addPerk(new StaminUp(handler));
		//handler.getPlayer().getInv().addPerk(new Stronghold(handler));
		//handler.getPlayer().getInv().addPerk(new Vampire(handler));

		// points
		int points = 0;
		handler.getPlayer().gainPoints(points);

		// set round
		int round = 0;
		handler.getRoundLogic().setCurrentRound(round);

		// give gun
		Gun gun =
				new GrenadeLauncher(handler);
				//new Winchester1876(handler);
				//new AA12(handler);
				//new RPG(handler);
				//new Flamethrower(handler);
				//new IceShotgun(handler);
				//new P90(handler);
				//new Minigun(handler);
		gun.upgradeWeapon();
		handler.getPlayer().getInv().setGun(gun);

	}
}
