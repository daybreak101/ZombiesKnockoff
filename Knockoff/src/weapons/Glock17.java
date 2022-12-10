package weapons;

import entities.bullets.Bullet;
import entities.bullets.IceBullet;
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
	
	public void shoot() {}
	
	int heldShot = 0;
	//guess i figured out how to work semi-auto guns
	public void postTick() {
		//System.out.println("HeldShot:" + heldShot);
		if (handler.getMouseManager().isLeftPressed() && !isReloading) {
			System.out.println("charge");
			heldShot++;
		}
		else if(!handler.getMouseManager().isLeftPressed() && heldShot > 0 && !isReloading) {
			shootSingleShot();
			System.out.println("shot single shot");
			heldShot = 0;
		}

	}
	
	public void shootSingleShot() {
		Player player = handler.getWorld().getEntityManager().getPlayer();

		if ( currentClip > 0 && !isReloading) {
			readyToFire = false;

			Sounds.playClip(Sounds.shootBeta);

			currentClip--;
			handler.getWorld().getEntityManager().addEntity(new Bullet(handler,
					player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, range));

			timerToFire = 0;
		}

	}
	
}

