package weapons;

import entities.bullets.Bullet;
import entities.creatures.Player;
import main.Handler;
import sounds.Sounds;

public class RPD extends Gun{

	public RPD(Handler handler) {
		super(handler, 390, 7, 300, 100, 400, 1.0f, 100);
		this.name = "RPD";
		originalName = name;
		upgradedName = "Death by Reload";
				
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
