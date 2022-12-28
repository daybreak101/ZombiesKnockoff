package weapons;

import entities.bullets.Grenade;
import entities.creatures.Player;
import main.Handler;

public class GrenadeLauncher extends Gun{

	public GrenadeLauncher(Handler handler) {
		super(handler, 1000, 50, 300, 6, 60, 0.6f, 6);
		this.name = "Grenade Launcher";
		originalName = name;
		upgradedName = "Rolling Thumper";
	}

	public void shoot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();
		
		if(readyToFire == true && currentClip > 0 && isReloading == false) {
			readyToFire = false;
			currentClip--;
			handler.getWorld().getEntityManager().addEntity(new Grenade(handler, 
					player.getX() + player.getWidth()/2,
					player.getY() + player.getHeight()/2,
					true
					));
			
			timerToFire = 0;
		}
	}

}
