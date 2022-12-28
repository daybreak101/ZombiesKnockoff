package weapons;

import entities.bullets.Bullet;
import entities.creatures.Player;
import main.Handler;
import sounds.Sounds;

public class M4 extends Gun {

	public M4(Handler handler) {
			super(handler, 270, 8, 100, 30, 300, 0.4f, 100);
			this.name = "M4";
			originalName = name;
			upgradedName = "Massacre '64";
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
