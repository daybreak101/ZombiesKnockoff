package perks;

import graphics.Assets;
import main.Handler;

public class SleightOfHand extends Perk {
	
	//increased reload speed

	public SleightOfHand(Handler handler, int level) {
		super(handler, level);
		this.name = "Sleight Of Hand";
		this.icon = Assets.fasthand;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setSpeedcola(level);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setSpeedcola(-1);
		
	}
}
