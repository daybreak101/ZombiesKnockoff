package perks;

import graphics.Assets;
import main.Handler;

public class MuleKick extends Perk {

	public MuleKick(Handler handler) {
		super(handler);
		this.name = "Mule Kick";
		this.icon = Assets.mule;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setMule(true);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setMule(false);
		handler.getPlayer().getInv().getArsenal()[2] = null;
		
	}

}
