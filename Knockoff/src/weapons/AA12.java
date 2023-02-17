package weapons;

import entities.bullets.ShotgunBullet;
import entities.creatures.Player;
import main.Handler;
import sounds.Sounds;

public class AA12 extends Gun{

	public AA12(Handler handler) {
		super(handler, 120, 7, 140, 12, 120, 0.4f, 7);
		name = "AA12";
		originalName = name;
		upgradedName = "AnarchAnonymous24";
	}


	@Override
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
