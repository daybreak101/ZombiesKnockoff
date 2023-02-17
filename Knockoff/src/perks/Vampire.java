package perks;

import graphics.Assets;
import main.Handler;

public class Vampire extends Perk {
	
	//gain one health per kill, can override health cap by 25

	public Vampire(Handler handler, int level) {
		super(handler, level);
		this.name = "Dracula's Hunger";
		this.icon = Assets.vamp;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setVamp(level);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setVamp(-1);
		
	}

}
