package perks;

import graphics.Assets;
import main.Handler;

public class Juggernaut extends Perk{
	
	//increase health

	public Juggernaut(Handler handler) {
		super(handler);
		this.name = "Juggernaut";
		this.icon = Assets.jugg;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setJugg(true);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setJugg(false);
	}

}
