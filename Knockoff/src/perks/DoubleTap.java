package perks;

import graphics.Assets;
import main.Handler;

public class DoubleTap extends Perk{
	
	//increase fire rate

	public DoubleTap(Handler handler, int level) {
		super(handler, level);
		this.name = "Double Tap";
		this.icon = Assets.doubletap;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setDoubletap(level);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setDoubletap(-1);
		
	}

}
