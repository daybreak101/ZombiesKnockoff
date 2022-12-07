package weapons;

import entities.bullets.Bullet;
import entities.creatures.Player;
import main.Handler;

public class AK47 extends Gun {

	public AK47(Handler handler) {
		super(handler, 400, 10, 140, 30, 300, 0.4f, 100);
		this.name = "AK-47";
		originalName = name;
		upgradedName = "Async Kombat 94";
	}
	
	public void shoot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();
		
		if(readyToFire == true && currentClip > 0 && isReloading == false) {
			readyToFire = false;
			currentClip--;
			
			
			//Sounds.SHOOT.play();
			
			handler.getWorld().getEntityManager().addEntity(new Bullet(handler, 
					player.getX() + player.getWidth()/2,
					player.getY() + player.getHeight()/2,
					range));
			
			timerToFire = 0;
		}
	}

}
