package weapons;

import entities.bullets.Bullet;
import entities.creatures.Player;
import main.Handler;
import sounds.Sounds;

public class AWP extends Gun{

	public AWP(Handler handler) {
		super(handler, 1000, 70, 140, 6, 60, 0.7f, 50);
		this.name = "AWP";
		originalName = name;
		upgradedName = "A Weak Pistol";
	}
	
	public void shoot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();
		
		if(readyToFire == true && currentClip > 0 && isReloading == false) {
			readyToFire = false;
			currentClip--;
			
			Sounds.playClip(Sounds.shootBeta);
			
			handler.getWorld().getEntityManager().addEntity(new Bullet(handler, 
					player.getX() + player.getWidth()/2,
					player.getY() + player.getHeight()/2,
					range));
			
			timerToFire = 0;
		}
	}

}
