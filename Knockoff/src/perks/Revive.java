package perks;

import graphics.Assets;
import main.Handler;

public class Revive extends Perk{

	public Revive(Handler handler, int level) {
		super(handler, level);
		this.name = "Revive";
		this.icon = Assets.revive;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setRevive(level);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setRevive(-1);
		
	}

}
