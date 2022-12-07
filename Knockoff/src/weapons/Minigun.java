package weapons;

import entities.bullets.Bullet;
import entities.creatures.Player;
import entities.creatures.Zombie;
import main.Handler;
import sounds.Sounds;

public class Minigun extends Gun {
	
	private int windup = 0;
	private int windupMax = 40;

	public Minigun(Handler handler) {
		super(handler, 2500, 2, 0, 0, 0, 1.5f, 50);
		name = "Minigun";
	}
	
	
	boolean playSound = false;
	@Override
	public void shoot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();

		if (readyToFire == true && windup >= windupMax) {
			readyToFire = false;
			//currentClip--;

			Sounds.playClip(Sounds.shootBeta);

			handler.getWorld().getEntityManager().addEntity(new Bullet(handler, player.getX() + player.getWidth() / 2,
					player.getY() + player.getHeight() / 2, range));

			timerToFire = 0;

		}
		else if(readyToFire == true && windup < windupMax   ) {
			playSound = !playSound;
			if(windup % 3 != 1  )
				Sounds.playClip(Sounds.swoosh);
			windup++;
			windup++;
		}
			
	}
	
	public void shootAsTurret(float x, float y) {
		int lowestDistanceSoFar = 2000000;
		Zombie closestEntity = null;
		for (Zombie entity : handler.getWorld().getEntityManager().getZombies().getObjects()) { // This loops through all the entities, setting the variable "entity" to each element.
		    int zombieX = (int) (x - entity.getX());
		    int zombieY = (int) (y - entity.getY());
		    double distance = Math.sqrt((zombieX * zombieX) + (zombieY * zombieY));
		    if (distance < lowestDistanceSoFar) {
		        lowestDistanceSoFar = (int) distance;
		        closestEntity = entity;
		    }
		}
		if(closestEntity != null) {
			if (readyToFire == true) {
				readyToFire = false;

				Sounds.playClip(Sounds.shootBeta);
				handler.getWorld().getEntityManager().addEntity(new Bullet(handler, this, x, y,
						closestEntity.getX() + closestEntity.getWidth()/2, closestEntity.getY() + closestEntity.getHeight()/2, range)); 


				timerToFire = 0;

			}
		}

		
		
	}

	@Override
	public void postTick() {
		if(windup > 0)
			windup--;
		
	}
}
