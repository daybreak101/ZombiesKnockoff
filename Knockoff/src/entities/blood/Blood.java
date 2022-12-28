package entities.blood;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entities.creatures.Player;
import entities.creatures.Zombie;
import entities.creatures.ZombieType;
import graphics.Assets;
import main.Handler;
import utils.Timer;

public class Blood {
	protected int timer, counter;
	protected float x, y;
	protected int width, height;
	protected Handler handler;
	protected int bloodType;
	Rectangle rect;
	BufferedImage bloodImage;
	protected int damageToPlayer;
	protected int damageToZombie;
	Timer damageTimer;

	public Blood(Handler handler, float x, float y, int bloodType) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		timer = 300;
		counter = 0;
		width = 75;
		height = 75;
		damageToPlayer = 0;
		damageToZombie = 0;
		damageTimer = new Timer(5000);
		rect = new Rectangle((int) x, (int) y, width, height);
		this.bloodType = bloodType;
		
		if (bloodType == ZombieType.ZOMBIE) {
			System.out.println("here");
			bloodImage = Assets.zombieBlood;
		} else if (bloodType == ZombieType.LICKER) {
			bloodImage = Assets.lickerBlood;
		} else if (bloodType == ZombieType.TOXEN) {
			bloodImage = Assets.toxenBlood;
			timer = 900;
			damageTimer = new Timer(30);
			damageToPlayer = 5;
			damageToZombie = 500;
		} else if (bloodType == ZombieType.STOKER) {
			bloodImage = Assets.toxenBlood;
			timer = 600;
			damageTimer = new Timer(30);		
			damageToPlayer = 5;
			damageToZombie = 500;
		}
		else {
			timer = 30;
		}
	}
	
	public void damagePlayer() {
		Player player = handler.getPlayer();
		if(rect.intersects(player.getCollisionBounds(0, 0)) && damageTimer.isReady()) {
			if(bloodType == ZombieType.STOKER) {
				handler.getPlayer().setBurn(damageToPlayer);
			}
			handler.getPlayer().takeDamage(damageToPlayer/2);
			damageTimer.resetTimer();
		}			
	}
	
	public void damageZombies() {
		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (e.getCollisionBounds(0, 0).intersects(rect) && e.getZombieType() != bloodType && damageTimer.isReady()) {	
				if(bloodType == ZombieType.STOKER) {
					handler.getPlayer().setBurn(damageToZombie/5);
				}
				e.takeDamage(damageToZombie);
				damageTimer.resetTimer();
			}
		}
	}	
	

	public void tick() {
		rect = new Rectangle((int) x, (int) y, width, height);
		counter++;
		damageTimer.tick();
		damagePlayer();
		damageZombies();
	}

	public void render(Graphics g) {
		g.drawImage(bloodImage, (int) (x - handler.getGameCamera().getxOffset()),
				(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
	}

	public int getCounter() {
		return counter;
	}

	public int getTimer() {
		return timer;
	}
	
	public int getBloodType() {
		return bloodType;
	}
	
	public void moveX() {
		x+=2;
	}
	
	public Rectangle getRect() {
		return rect;
	}
}
