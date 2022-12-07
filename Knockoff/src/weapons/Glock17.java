package weapons;

import entities.bullets.Bullet;
import entities.creatures.Player;
import main.Handler;
import sounds.Sounds;



public class Glock17 extends Gun{
	
	//File file = new File("res/shootBeta.wav");
	
	public Glock17(Handler handler) {
		super(handler, 140, 20, 100, 17, 170, 0.1f, 100);
		this.name = "Glock17";
		originalName = name;
		upgradedName = "This isn't COD bro";
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

