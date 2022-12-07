package weapons;

import entities.bullets.IceBullet;
import entities.bullets.IceStorm;
import entities.creatures.Player;
import main.Handler;

public class IceShotgun extends Gun {
	public IceShotgun(Handler handler) {
		// change values
		super(handler, 0, 100, 140, 5, 10, 0.5f, 7);
		this.name = "Teeth of the Yeti";
		originalName = name;
		upgradedName = "Wings of Pamola";
	}

	int heldShot = 0;
	int heldShotMax = 180;

	//guess i figured out how to work semi-auto guns
	public void postTick() {
		//System.out.println("HeldShot:" + heldShot);
		if (handler.getMouseManager().isLeftPressed() && !isReloading) {
			System.out.println("charge");
			heldShot++;
		}
		else if (!handler.getMouseManager().isLeftPressed() && heldShot >= heldShotMax && !isReloading) {
			heldShot = 0;
			System.out.println("shot charged shot");
			shootChargedShot();
		}
		else if(!handler.getMouseManager().isLeftPressed() && heldShot < heldShotMax && heldShot > 0 && !isReloading) {
			shootSingleShot();
			System.out.println("shot single shot");
			heldShot = 0;
		}

	}

	public void shootChargedShot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();

		if (isUpgraded && currentClip == clip && !isReloading) {

			currentClip = 0;
			handler.getWorld().getEntityManager().addEntity(new IceStorm(handler,
					player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2));
			timerToFire = 0;
			readyToFire = false;
		}
	}

	public void shootSingleShot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();

		if ( currentClip > 0 && !isReloading) {
			readyToFire = false;

			// Sounds.SHOOT.play();

			currentClip--;
			handler.getWorld().getEntityManager().addEntity(new IceBullet(handler,
					player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, range));

			timerToFire = 0;
		}

	}
}
