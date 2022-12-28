package perks;

import entities.creatures.Creature;
import graphics.Assets;
import main.Handler;

public class StaminUp extends Perk {

	public StaminUp(Handler handler) {
		super(handler);
		this.name = "StaminUp!";
		this.icon = Assets.stam;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setStaminup(true);
		handler.getPlayer().setDefaultSpeed(4.5f);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setStaminup(false);
		handler.getPlayer().setDefaultSpeed(4.0f);
	}

}
