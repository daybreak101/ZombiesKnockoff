package weapons;

import entities.bullets.ShotgunBullet;
import entities.creatures.Player;
import main.Handler;
import sounds.Sounds;

public class Winchester1876 extends Gun{

	public Winchester1876(Handler handler) {
		super(handler, 200, 40, 50, 6, 60, 0.5f, 8);
		this.name = "Winchester 1876";
		originalName = name;
		upgradedName = "Yee chester";
	}
	
	public void shoot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();
		
		if(readyToFire == true && currentClip > 0 && isReloading == false) {
			readyToFire = false;
			currentClip--;
			
			Sounds.playClip(Sounds.shootBeta);
			
			handler.getWorld().getEntityManager().addEntity(new ShotgunBullet(handler, 
					player.getX() + player.getWidth()/2,
					player.getY() + player.getHeight()/2,
					range));
			
			timerToFire = 0;
		}
	}
}
