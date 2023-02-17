package perks;

import graphics.Assets;
import main.Handler;

public class Bandolier extends Perk{
	public Bandolier(Handler handler, int level) {
		super(handler, level);
		this.name = "Bandolier";
		this.icon = Assets.bandolier;
		
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setBandolier(level);
		
		for(int i = 0; i < handler.getPlayer().getInv().getArsenal().length; i++) {
			if(handler.getPlayer().getInv().getArsenal()[i] != null)
				handler.getPlayer().getInv().getArsenal()[i].activateBandolier();
		}
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setBandolier(-1);
		
		for(int i = 0; i < handler.getPlayer().getInv().getArsenal().length; i++) {
			if(handler.getPlayer().getInv().getArsenal()[i] != null)
				handler.getPlayer().getInv().getArsenal()[i].deactivateBandolier();
		}
	}

}
