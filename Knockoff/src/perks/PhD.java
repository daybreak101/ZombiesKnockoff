package perks;

import graphics.Assets;
import main.Handler;

public class PhD extends Perk {
	
	//immunity to explosives

	public PhD(Handler handler, int level) {
		super(handler, level);
		this.name = "PhD Armor";
		this.icon = Assets.phd;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setPhd(level);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setPhd(-1);
		
	}

}
