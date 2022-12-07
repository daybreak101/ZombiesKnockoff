package weapons;

import entities.bullets.Bullet;
import entities.creatures.Player;
import main.Handler;
import sounds.Sounds;

public class P90 extends Gun {

	public P90(Handler handler) {
		super(handler, 255, 3, 70, 50, 350, 0.2f, 100);
		this.name = "P90";
		originalName = name;
		upgradedName = "Peashooter 180";
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
