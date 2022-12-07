package weapons;

import entities.bullets.FlameBullet;
import entities.creatures.Player;
import main.Handler;
import sounds.Sounds;

public class Flamethrower extends Gun{
	
	

	public Flamethrower(Handler handler) {
		super(handler, 250, 3, 0, 500, 0, 0.6f, 50);
		this.name = "Flamethrower";
		originalName = name;
		upgradedName = "HotBox";
	}

	@Override
	public void shoot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();
		float mouseX = player.getX() -  handler.getGameCamera().getxOffset() - (int)handler.getMouseManager().getMouseX();
		float mouseY = player.getY() -   handler.getGameCamera().getyOffset() -  (int)handler.getMouseManager().getMouseY();
		float angle = (float) Math.atan2(-mouseY, -mouseX);
		float xMove= (float) (player.getWidth()/2 * Math.cos(angle));
		float yMove = (float) (player.getHeight()/2 * Math.sin(angle));
		
		
		if(readyToFire == true && currentClip > 0 && isReloading == false) {
			readyToFire = false;
			currentClip--;
			
			
			handler.getWorld().getEntityManager().addEntity(new FlameBullet(handler, 
					player.getX() + xMove,
					player.getY() + yMove,
					range));	
			
			Sounds.playClip(Sounds.swoosh);
			
			
//			handler.getWorld().getEntityManager().addEntity(new FlameBullet(handler, 
//					player.getX() + player.getWidth()/2,
//					player.getY() + player.getHeight()/2,
//					range));
			
			timerToFire = 0;
		}
		
	}

}
