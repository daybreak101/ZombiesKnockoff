package perks;

import graphics.Assets;
import main.Handler;

public class MuleKick extends Perk {

	public MuleKick(Handler handler, int level) {
		super(handler, level);
		this.name = "Mule Kick";
		this.icon = Assets.mule;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setMule(level);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setMule(-1);
		handler.getPlayer().getInv().setCurrentGun(0);
		if(level != 3)
			handler.getPlayer().getInv().getArsenal()[2] = null;
			
		
	}

}
