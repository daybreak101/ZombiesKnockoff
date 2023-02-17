package perks;

import java.awt.Graphics;

import graphics.Assets;
import main.Handler;
import utils.Timer;

public class Stronghold extends Perk{

	public Stronghold(Handler handler, int level) {
		super(handler, level);
		this.name = "Stronghold";
		this.icon = Assets.stronghold;
	}

	Timer activationTimer = new Timer(300);
	Timer strengthTick = new Timer(60);
	boolean activated = false;
	@Override
	public void tick() {

		if(handler.getPlayer().moved == false) {
			activationTimer.tick();
		}
		else {
			activationTimer.resetTimer();
		}
		
		if(activated) {			
			strengthTick.tick();
			if(strengthTick.isReady()) {
				handler.getPlayer().gainArmor(5);
				handler.getPlayer().gainStrongholdDamageMultiplier(.05f);
			}
			
			if(!handler.getPlayer().checkIfInStrongholdCircle()){
				activated = false;
			}
		}
		else if(activationTimer.isReady()) {
			activated = true;
			handler.getPlayer().setStrongholdCircle();
		}
		else {
			activated = false;
			handler.getPlayer().getInv().strongholdActivation = false;
		}
		
	}

	@Override
	public void buff() {
		handler.getPlayer().getInv().setStronghold(level);
		
	}

	@Override
	public void debuff() {
		handler.getPlayer().getInv().setStronghold(-1);
		handler.getPlayer().getInv().strongholdActivation = false;
		handler.getPlayer().removeArmor();
		handler.getPlayer().removeStrongholdDamageMultiplier();
		activated = false;
		
	}

}
