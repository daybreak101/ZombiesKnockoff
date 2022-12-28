package entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Random;

import entities.Entity;
import entities.areas.Areas;
import entities.blood.Blood;
import entities.bullets.SniperBullet;
import entities.powerups.MaxAmmo;
import entities.statics.Barrier;
import entities.statics.InteractableStaticEntity;
import entities.statics.traps.IcyWater;
import graphics.Animation;
import graphics.Assets;
import hud.CritElement;
import hud.DamageElement;
import hud.ZombieHealthElement;
import main.Handler;
import utils.Graph;
import utils.Node;

public class Zombie extends Creature {

	protected Animation zombieAnim, zombieAttackAnim, crawlerAnim, crawlerAttackAnim, enhancedZombieAnim,
			enhancedZombieAttackAnim;
	protected boolean justAttacked = false;
	protected long timer = 0;
	protected int attackTicker = 0, attackTimer = 100;
	private int attackDamage;
	Random rand = new Random();
	private float angley = rand.nextInt(handler.getWorld().getHeight() * 100),
			anglex = rand.nextInt(handler.getWorld().getWidth() * 100);
	Graph graph;
	Node goTo = null;
	private int source, player;
	boolean moving = false;

	private SniperBullet sniperBullet;

	// flamethrower vars
	protected boolean isBurning = false;
	private int burnDamage = 0;
	private int burnTicker = 0;
	private int burnMax = 300;

	protected int zombieType;
	protected boolean isCrawler = false;
	protected boolean isFrozen = false;

	protected int maxHealth;

	protected Rectangle hitbox;
	protected ZombieHealthElement healthBar;

	public Zombie(Handler handler, float x, float y, float dspeed, int health) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		isZombie = true;
		zombieType = ZOMBIE;
		zombieAnim = new Animation(150, Assets.zombieAnim);
		zombieAttackAnim = new Animation(100, Assets.zombieAttackAnim, true);
		crawlerAnim = new Animation(100, Assets.crawlerAnim);
		speed = 1.8f + dspeed;
		attackDamage = 10;
		this.health = health;
		maxHealth = health;
		hitbox = new Rectangle(0, 0, width, height);
		//bounds.x = 0;
		//bounds.y = 0;
		//bounds.width = 0;
		//bounds.height = 0 ;
		bounds.x = 65 / 2;
		bounds.y = 65 / 2;
		bounds.width = 10;
		bounds.height = 10;
		graph = handler.getWorld().getGraph();
		healthBar = new ZombieHealthElement(handler, x, y, this);
	}

	public void setSpeed(float dSpeed) {
		speed = 1.8f + dSpeed;
	}

	int movementType = 0;
	int moveCounter = 0;
	int moveTimer = 60;

	float slowdown = 1;
	boolean inWater = false;

	@Override
	public void tick() {
		checkIfInIcyWater();
		if (isFrozen) {

		} else if (handler.getPlayer().getHealth() <= 0) {
			meander();
			move();
		} else {

			slowdown = 1;
			if (inWater) {
				slowdown += .5;
			}

			if (checkForObstacles()) {
				movementType = 1;
				followPath();
			} 
			else if (goTo != null) {
				if (source != goTo.getVertex()) {

					followPath();
				}
				else {
				//movementType = 0;
				//moveCounter = 0;
				//moving = false;
				followPlayer();
			
				}
			}
			else {
				followPlayer();
			}

		burn();
		attack();
		postTick();
		healthBar.tick();
		}

	}

	public void attack() {
		Rectangle playerBox = handler.getPlayer().getHitbox();
		if (this.getHitBox(0, 0).intersects(playerBox)) {
			if (isFrozen) {

			} else if (!justAttacked() && !handler.getPlayer().getJustTookDamage()) {
				handler.getPlayer().takeDamage(attackDamage);
				dontMove();
				handler.getPlayer().justTookDamage();
			}
		} else {
			for (Barrier e : handler.getWorld().getEntityManager().getBarriers()) {
				if (e.getCollisionBounds(0, 0).intersects(this.getHitBox(0, 0))) {
					if (isFrozen) {

					} else if (justAttacked() == false) {
						e.takeDamage(attackDamage);
						dontMove();
					}
				}
			}
		}
	}

	public Rectangle getHitBox(float xOffset, float yOffset) {
		return new Rectangle((int) (x + hitbox.x + xOffset), (int) (y + hitbox.y + yOffset), hitbox.width,
				hitbox.height);
	}

	public void turnToCrawler() {
		isCrawler = true;
		this.speed = 1.0f;
	}

	public void checkIfInIcyWater() {
		boolean found = false;
		for (Areas e : handler.getWorld().getEntityManager().getAreas()) {
			if (((IcyWater) e).checkIfEntityIsContained(getHitBox(0, 0))) {
				inWater = true;
				found = true;
			}
		}
		if (!found) {
			inWater = false;
		}
	}

	public void takeDamage(int amount) {
		boolean crit = false;
		if (handler.getRoundLogic().getPowerups().isInstakillActive()) {
			if (handler.getSettings().isToggleDamage())
				handler.getHud().addObject(new DamageElement(handler, x + width / 2 + 10, y + height / 2 + 10, health));
			health = 0;

		} else {
			crit = isCritical();
			if (crit && handler.getPlayer().getInv().isDeadshot()) {
				health -= (amount * 3);
				if (handler.getSettings().isToggleDamage())
					handler.getHud()
							.addObject(new DamageElement(handler, x + width / 2 + 10, y + height / 2 + 10, amount * 3));
				if (handler.getSettings().isToggleCrits())
					handler.getHud().addObject(new CritElement(handler, x + width / 2, y + height / 2));
				System.out.println("critical");
			} else if (crit) {
				health -= (amount * 2);
				if (handler.getSettings().isToggleDamage())
					handler.getHud()
							.addObject(new DamageElement(handler, x + width / 2 + 10, y + height / 2 + 10, amount * 2));
				if (handler.getSettings().isToggleCrits())
					handler.getHud().addObject(new CritElement(handler, x + width / 2, y + height / 2));
				System.out.println("critical");
			} else {
				if (handler.getSettings().isToggleDamage())
					handler.getHud()
							.addObject(new DamageElement(handler, x + width / 2 + 10, y + height / 2 + 10, amount));
				health -= amount;
			}
		}
		if (isFrozen) {
			freezeNearbyZombies();
			active = false;
			die();
		} else if (health <= 0 && inWater) {
			freeze();
		} else if (health <= 0 && active == true) {
			if (crit) {
				handler.getPlayer().gainPoints(100);
				handler.getPlayer().getStats().addHeadshot();
			} else {
				handler.getPlayer().gainPoints(70);
			}

			active = false;
			die();
		} else {
			handler.getPlayer().gainPoints(10);
		}
	}

	public void freezeNearbyZombies() {
		Ellipse2D freezeRadius = new Ellipse2D.Float(x + width / 2 - 100, y + height / 2 - 100, 200, 200);
		for (Zombie e : handler.getWorld().getEntityManager().getZombies()) {
			if (freezeRadius.intersects(e.getHitBox(0, 0)) && e != this) {
				e.freeze();
			}
		}
	}

	public void freeze() {
		if ((zombieType != ZombieType.ICE_ENHANCED_ZOMBIE || zombieType != ZombieType.WHITE_WALKER)) {
			if (isFrozen) {
				active = false;
				die();
			} else {
				isFrozen = true;
				speed = 0;
				health = 1;
			}
		}

	}

	int eachBurnTicker = 0, eachBurnMax = 20;

	public void burn() {
		if (isBurning) {
			burnTicker++;
			eachBurnTicker++;
			if (eachBurnTicker >= eachBurnMax) {
				takeDamage(burnDamage);
				eachBurnTicker = 0;
			}
			if (burnTicker >= burnMax) {
				isBurning = false;
				burnTicker = 0;
			}
		}
	}

	public void postTick() {
		if (!justAttacked && !isFrozen)
			zombieAnim.tick();
		else if (justAttacked && !isFrozen) {
			zombieAttackAnim.tick();
		}

	}

	public void dontMove() {
		justAttacked = true;
	}

	public void setBurn(int damage) {
		isBurning = true;
		burnTicker = 0;
		burnDamage = damage;
	}

	public void meander() {
		xMove = 0;
		yMove = 0;

		float moveToX = anglex - x;
		float moveToY = angley - y;

		float angle = (float) Math.atan2(moveToY, moveToX);
		xMove = (float) (speed * Math.cos(angle) / slowdown);
		yMove = (float) (speed * Math.sin(angle) / slowdown);

		if (5 >= moveToX && 0 <= moveToX) {
			anglex = rand.nextInt(handler.getWorld().getWidth() * 100);
		}
		if (5 >= moveToY && 0 <= moveToY) {
			angley = rand.nextInt(handler.getWorld().getHeight() * 100);
		}

	}

	public void moveX() {
		x += xMove;
	}

	public void moveY() {
		y += yMove;
	}

	Line2D[] z2p = new Line2D[9];
	boolean intersection = false;

	public boolean checkForObstacles() {
		Player player = handler.getPlayer();
		z2p[0] = new Line2D.Float(x + width/2, y + height/2, player.getX() + 2, player.getY() + 2);
		z2p[1] = new Line2D.Float(x + width/2, y + height/2, player.getX() + 2, player.getY() + player.getHeight() / 2);
		z2p[2] = new Line2D.Float(x + width/2, y + height/2, player.getX() + 2, player.getY() + player.getHeight() - 2);
		z2p[3] = new Line2D.Float(x + width/2, y + height/2, player.getX() + player.getWidth() / 2, player.getY() + 2);
		z2p[4] = new Line2D.Float(x + width/2, y + height/2, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
		z2p[5] = new Line2D.Float(x + width/2, y + height/2, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() - 2);
		z2p[6] = new Line2D.Float(x + width/2, y + height/2, player.getX() + player.getWidth() - 2, player.getY() + 2);
		z2p[7] = new Line2D.Float(x + width/2, y + height/2, player.getX() + player.getWidth() - 2, player.getY() + player.getHeight() / 2);
		z2p[8] = new Line2D.Float(x + width/2, y + height/2, player.getX() + player.getWidth() - 2, player.getY() + player.getHeight() - 2);

		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			for (int i = 0; i < 9; i++) {
				if (z2p[i].intersects(e.getCollisionBounds(0, 0))) {
					if (!handler.getWorld().getEntityManager().getBarriers().contains(e)) {
						return true;
					}

				}
			}
		}
		return false;
	}

	int lastPlayer, lastSource;
	float xOffset, yOffset;

	public void followPath() {

		if (justAttacked) {
			timer++;
			if (timer == 100) {
				zombieAttackAnim.resetAnim();
				justAttacked = false;
				timer = 0;
			}
		} else {

			//if(source == -1)
				source = handler.getWorld().getClosestNode(x + width / 2, y + height / 2);

			player = handler.getWorld().getClosestNode(handler.getPlayer().getX() + handler.getPlayer().getWidth() / 2,
					handler.getPlayer().getY() + handler.getPlayer().getHeight() / 2);

			
			goTo = handler.getWorld().getNextStep(source, player);
			//if (goTo != null)
				//System.out.println("source: " + source + " , goTo: " + goTo.getVertex() + " , player: " + player);

	
			
			
			if (handler.getWorld().getNodes().get(source).checkWithinNode(this)) {
				System.out.println("source: " + source + " , goTo: " + goTo.getVertex() + " , player: " + player);
				source = goTo.getVertex();
				goTo = handler.getWorld().getNextStep(source, player);
			}
			if (goTo == null) {
				meander();
				move();
			}else if(source == goTo.getVertex() && goTo.checkWithinNode(this)) {
				//System.out.print("okay");
				meander();
				move();
			} else {

				float moveToX = goTo.getX() - x - width / 2;
				float moveToY = goTo.getY() - y - height / 2;

				float angle = (float) Math.toDegrees(Math.atan2(moveToY, moveToX));
				xMove = (float) (speed * (float) Math.cos(Math.toRadians(angle)) / slowdown);
				yMove = (float) (speed * (float) Math.sin(Math.toRadians(angle)) / slowdown);

				// System.out.println("xMove: " + xMove + " , yMove: " + yMove);

				if (!checkEntityCollisions(xMove, 0f)) {
					moveX();
				}

				if (!checkEntityCollisions(0f, yMove)) {

					moveY();
				}

			}
		}
	}

	public void followPlayer() {
		source = -1;
		player = -1;
		goTo = null;

		xMove = 0;
		yMove = 0;

		if (justAttacked == true) {
			timer++;
			if (timer == 100) {
				zombieAttackAnim.resetAnim();
				justAttacked = false;
				timer = 0;
			}
		} else {
			float moveToX = handler.getPlayer().getX() - x;
			float moveToY = handler.getPlayer().getY() - y;

			float angle = (float) Math.toDegrees(Math.atan2(moveToY, moveToX));
			xMove = (float) (speed * (float) Math.cos(Math.toRadians(angle)) / slowdown);
			yMove = (float) (speed * (float) Math.sin(Math.toRadians(angle)) / slowdown);

			if (!checkEntityCollisions(xMove, 0f)) {
				moveX();
			}

			if (!checkEntityCollisions(0f, yMove)) {

				moveY();
			}
		}
	}

	float rotationAngle;

	@Override
	public void render(Graphics g) {
		float moveToX, moveToY;
		if (!isFrozen && handler.getPlayer().getHealth() <= 0) {
			moveToX = x - anglex; // - handler.getGameCamera().getxOffset();
			moveToY = y - angley;// - handler.getGameCamera().getyOffset();;

			rotationAngle = (float) Math.toDegrees(Math.atan2(-moveToX + width / 2, moveToY + height / 2));
		} else if (!isFrozen && !justAttacked) {

			moveToX = handler.getPlayer().getX() - handler.getGameCamera().getxOffset()
					+ handler.getPlayer().getWidth() / 2;
			moveToY = handler.getPlayer().getY() - handler.getGameCamera().getyOffset()
					+ handler.getPlayer().getHeight() / 2;
			rotationAngle = (float) Math
					.toDegrees(Math.atan2(-(x - handler.getGameCamera().getxOffset() - moveToX + width / 2),
							y - handler.getGameCamera().getyOffset() - moveToY + height / 2));

		}

		if (!isCrawler)
			g.drawImage(Assets.shadow, (int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width, height, null);

		if (isBurning) {
			g.setColor(Color.orange);
			g.fillOval((int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width, height);
		}

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.rotate(Math.toRadians(rotationAngle), x - handler.getGameCamera().getxOffset() + width / 2,
				y - handler.getGameCamera().getyOffset() + height / 2);

		if (isFrozen) {
			g2d.drawImage(Assets.frozenZombie, (int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
		} else if (isCrawler) {
			g2d.drawImage(Assets.crawler, (int) (x - (height * 1.5 / 2) - handler.getGameCamera().getxOffset()),
					(int) (y - (width * 1.5 / 2) - handler.getGameCamera().getyOffset()), (int) (width * 1.5),
					(int) (height * 1.5), null);
		} else if (justAttacked) {
			g2d.drawImage(zombieAttackAnim.getCurrentFrame(), (int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
		} else {
			g2d.drawImage(zombieAnim.getCurrentFrame(), (int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
		}

		g2d.setTransform(old);

		if (handler.getSettings().isHealthBar())
			healthBar.render(g);

	}

	@Override
	public void die() {
		if (handler.getPlayer().getInv().isVamp())
			handler.getPlayer().incrementHealth();

		handler.getPlayer().getStats().gainKill();
		handler.getWorld().getEntityManager().addBlood(new Blood(handler, x, y, zombieType));

		if (handler.getRoundLogic().getPowerups().isPowerUpReady()) {
			handler.getWorld().getEntityManager()
					.addPowerUp(handler.getRoundLogic().getPowerups().generatePowerUp(x, y));
		}
		if (handler.getRoundLogic().getZombiesLeft() <= 1 && handler.getRoundLogic().isDogRound()) {
			handler.getWorld().getEntityManager().addPowerUp(new MaxAmmo(handler, x, y));
		}
	}

	public void dieByTrap() {
		handler.getWorld().getEntityManager().addBlood(new Blood(handler, x, y, zombieType));
		active = false;
	}

	public void damageByTrap(int damage) {
		health -= damage;
		if (health <= 0 && active == true) {
			handler.getWorld().getEntityManager().addBlood(new Blood(handler, x, y, zombieType));

			active = false;
		}
	}

	public boolean checkEntityCollisions(float xOffset, float yOffset) {
		for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
			if (e.equals(this) || e.isZombie())
				continue;
			if (e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		return false;
	}

	public boolean justAttacked() {
		return justAttacked;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public SniperBullet getSniperBullet() {
		return sniperBullet;
	}

	public void setSniperBullet(SniperBullet sniperBullet) {
		this.sniperBullet = sniperBullet;
	}

	public int getZombieType() {
		return zombieType;
	}

	public boolean isFrozen() {
		return isFrozen;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

}
