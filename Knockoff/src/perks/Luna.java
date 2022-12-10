package perks;

import graphics.Assets;
import main.Handler;
import utils.Timer;

public class Luna extends Perk {

	public Luna(Handler handler) {
		super(handler);
		this.name = "Luna";
		this.icon = Assets.luna;
	}
	
	Timer refresh = new Timer(10 * 60);
	Timer duration = new Timer(60 * 60);
	boolean activated = false;
	boolean ready = true;
	public void tick() {
		if(activated == false && ready) {
			activated = true;
			ready = false;
			handler.getWorld().getEntityManager().activateLuna();
		}
		if(activated) {
			duration.tick();
		}
		else if(!activated) {
			refresh.tick();
		}
		
		if(duration.isReady()) {
			duration.resetTimer();
			activated = false;
			ready = false;
			handler.getWorld().getEntityManager().deactivateLuna();
		}
		else if(refresh.isReady()) {
			refresh.resetTimer();
			ready = true;
		}
		
	}
	
	@Override
	public void buff() {
		handler.getPlayer().getInv().setLuna(true);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setLuna(false);
		
	}

}
