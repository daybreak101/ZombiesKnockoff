package perks;

import graphics.Assets;
import main.Handler;

public class DeadShot extends Perk{
	
	//increased range, increase crit chance and crit damage

	public DeadShot(Handler handler, int level) {
		super(handler, level);
		this.name = "DeadShot";
		this.icon = Assets.deadshot;
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setDeadshot(level);
		
		
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setDeadshot(-1);
		
	}


}
