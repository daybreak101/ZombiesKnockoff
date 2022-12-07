package weapons;

import entities.bullets.Rocket;
import entities.creatures.Player;
import main.Handler;

public class RPG extends Gun{

	public RPG(Handler handler) {
		super(handler, 2500, 10, 140, 1, 10, 1.1f, 10000);
		this.name = "RPG";
		originalName = name;
		upgradedName = "Get sum protection (;";
	}
	
	public void shoot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();
		
		if(readyToFire == true && currentClip > 0 && isReloading == false) {
			readyToFire = false;
			currentClip--;
			handler.getWorld().getEntityManager().addEntity(new Rocket(handler, 
					player.getX() + player.getWidth()/2,
					player.getY() + player.getHeight()/2
					));
			
			timerToFire = 0;
		}
	}

}
