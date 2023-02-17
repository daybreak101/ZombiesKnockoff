package perks;

import entities.creatures.Creature;
import graphics.Assets;
import main.Handler;

public class StaminUp extends Perk {

	public StaminUp(Handler handler, int level) {
		super(handler, level);
		this.name = "StaminUp!";
		this.icon = Assets.stam;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setStaminup(level);
		handler.getPlayer().setDefaultSpeed(4.5f);
		if(level >= 1) {
			handler.getPlayer().getPlayerSprint().setStaminaCooldown(100);
		}
		if (level == 3) {
			handler.getPlayer().getPlayerSprint().setMaxStamina(300);
			handler.getPlayer().getPlayerSprint().setCurrentStamina(300);
		}
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setStaminup(-1);
		handler.getPlayer().setDefaultSpeed(4.0f);
		handler.getPlayer().getPlayerSprint().setStaminaCooldown(180);
		handler.getPlayer().getPlayerSprint().setMaxStamina(200);
		handler.getPlayer().getPlayerSprint().setCurrentStamina(200);
		
	}

}
