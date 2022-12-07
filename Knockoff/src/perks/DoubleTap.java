package perks;

import graphics.Assets;
import main.Handler;

public class DoubleTap extends Perk{
	
	//increase fire rate

	public DoubleTap(Handler handler) {
		super(handler);
		this.name = "Double Tap";
		this.icon = Assets.doubletap;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setDoubletap(true);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setDoubletap(false);
		
	}

}
