package perks;

import graphics.Assets;
import main.Handler;

public class Revive extends Perk{

	public Revive(Handler handler) {
		super(handler);
		this.name = "Revive";
		this.icon = Assets.revive;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setRevive(true);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setRevive(false);
		
	}

}
