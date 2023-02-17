package entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.Entity;
import entities.blood.Blood;
import entities.creatures.zombieinfo.BurnStatus;
import entities.creatures.zombieinfo.FreezeStatus;
import entities.creatures.zombieinfo.SlownessStatus;
import entities.powerups.MaxAmmo;
import entities.statics.Barrier;
import entities.statics.InteractableStaticEntity;
import entities.statics.Wall;
import graphics.Animation;
import graphics.Assets;
import hud.CritElement;
import hud.DamageElement;
import hud.ZombieHealthElement;
import main.Handler;
import utils.Node;
import utils.Timer;

public class Zombie extends Creature {

	protected Animation zombieAnim, zombieAttackAnim, crawlerAnim, crawlerAttackAnim, enhancedZombieAnim,
			enhancedZombieAttackAnim;
	protected boolean justAttacked = false;
	private int attackDamage;
	protected Random rand = new Random();
	private float angley = rand.nextInt(handler.getWorld().getHeight() * 100),
			anglex = rand.nextInt(handler.getWorld().getWidth() * 100);
	Node goTo = null;
	private int source, player;
	boolean moving = false;	
	
	protected int zombieType;
	protected boolean isCrawler = false;

	protected int maxHealth;

	protected BurnStatus burnStatus;
	protected FreezeStatus freezeStatus;
	protected SlownessStatus slownessStatus;
	
	protected Timer attackCooldown = new Timer(100);



	protected Rectangle hitbox;
	protected ZombieHealthElement healthBar;

	public Zombie(Handler handler, float x, float y, float dSpeed, int health) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		zombieType = ZOMBIE;
		zombieAnim = new Animation(150, Assets.zombieAnim);
		zombieAttackAnim = new Animation(100, Assets.zombieAttackAnim, true);
		crawlerAnim = new Animation(100, Assets.crawlerAnim);
		dSpeed = rand.nextFloat(dSpeed - .3f, dSpeed + .4f);
		speed = 1.8f + dSpeed;
		attackDamage = 10;
		this.health = health;
		maxHealth = health;
		hitbox = new Rectangle(0, 0, width, height);
		bounds = new Rectangle(25, 25, 25, 25);
		healthBar = new ZombieHealthElement(handler, x, y, this);

		burnStatus = new BurnStatus(this);
		freezeStatus = new FreezeStatus(handler, this);
		slownessStatus = new SlownessStatus(this);
	}

	public void setSpeed(float dSpeed) {
		rand = new Random();
		dSpeed = rand.nextFloat(dSpeed - .3f, dSpeed + .4f);
		speed = 1.8f + dSpeed;
	}
	
	public void removeSpeed() {
		speed = 0;
	}

	@Override
	public void tick() {
		freezeStatus.checkIfInIcyWater();
		if (freezeStatus.isFrozen()) {

		} else if (handler.getPlayer().getHealth() <= 0 || (handler.getPlayer().getInv().getBlessings().isRunning()
				&& handler.getPlayer().getInv().getBlessings().getBlessing() == "In Plain Sight")) {
			meander();
			move();
		} else {
			slownessStatus.setSlowness(1);
			if (freezeStatus.inWater()) {
				slownessStatus.addToSlowness(.5f);
			}

			if (justAttacked) {
				attackCooldown.tick();
				if (attackCooldown.isReady()) {
					zombieAttackAnim.resetAnim();
					justAttacked = false;
					attackCooldown.resetTimer();
				}
			} else if (checkForObstacles()) {
				followPath();
			} else if (goTo != null) {
				if (source != goTo.getVertex()) {
					followPath();
				} else {
					followPlayer();
				}
			} else {
				followPlayer();
			}

			burnStatus.burn();
			attack();
			postTick();
			healthBar.tick();
		}

	}

	Timer attacking = new Timer(20);
	boolean isAttacking = false;

	public void attack() {
		Rectangle playerBox = handler.getPlayer().getHitbox();

		if (isAttacking) {
			attacking.tick();
			if (attacking.isReady()) {
				if (this.getHitBox(0, 0).intersects(playerBox) && !handler.getPlayer().getJustTookDamage()) {
					handler.getPlayer().takeDamage(attackDamage);
					handler.getPlayer().justTookDamage();
				}
				dontMove();
				attacking.resetTimer();
				isAttacking = false;
			}
		} else if (this.getHitBox(0, 0).intersects(playerBox) && !justAttacked()) {
			isAttacking = true;
		} else {
			for (Barrier e : handler.getWorld().getEntityManager().getBarriers()) {
				if (e.getCollisionBounds(0, 0).intersects(this.getHitBox(0, 0))) {
					if (justAttacked() == false) {
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

	public boolean isCritical() {
		if (handler.getPlayer().getInv().getBlessings().getBlessing() == "So No Head?"
				&& handler.getPlayer().getInv().getBlessings().isRunning())
			return true;

		int criticalChance = rand.nextInt(100);

		if (handler.getPlayer().getInv().getDeadshot() >= 2) {
			if (criticalChance < 20)
				return true;
		} else {
			if (criticalChance < 10)
				return true;
		}

		return false;
	}

	public void takeDamage(int amount) {
		boolean crit = false;
		if (handler.getRoundLogic().getPowerups().isInstakillActive()) {
			if (handler.getSettings().isToggleDamage())
				handler.getHud().addObject(new DamageElement(handler, x + width / 2 + 10, y + height / 2 + 10, health));
			health = 0;

		} else {
			crit = isCritical();
			if (crit && handler.getPlayer().getInv().getDeadshot() == 3)
				amount = (amount * 3);
			else if (crit)
				amount = (amount * 2);
			if (handler.getSettings().isToggleCrits() && crit)
				handler.getHud().addObject(new CritElement(handler, x + width / 2, y + height / 2));
			if (handler.getSettings().isToggleDamage())
				handler.getHud().addObject(new DamageElement(handler, x + width / 2 + 10, y + height / 2 + 10, amount));
			health -= amount;
		}
		if (freezeStatus.isFrozen()) {
			freezeStatus.freezeNearbyZombies();
			active = false;
			die();
		} else if (health <= 0 && freezeStatus.inWater()) {
			freezeStatus.freeze();
		} else if (health <= 0 && active == true) {
			if (crit) {
				if (handler.getPlayer().getInv().getDeadshot() >= 1)
					handler.getPlayer().getInv().gainPoints(150);
				else
					handler.getPlayer().getInv().gainPoints(100);
				handler.getPlayer().getStats().addHeadshot();
			} else {
				handler.getPlayer().getInv().gainPoints(70);
			}

			active = false;
			die();
		} else {
			handler.getPlayer().getInv().gainPoints(10);
		}
	}

	public void postTick() {
		if (!justAttacked && !freezeStatus.isFrozen())
			zombieAnim.tick();
		else if (justAttacked && !freezeStatus.isFrozen()) {
			zombieAttackAnim.tick();
		}

	}

	public void dontMove() {
		justAttacked = true;
	}

	public void meander() {
		xMove = 0;
		yMove = 0;

		float moveToX = anglex - x;
		float moveToY = angley - y;

		float angle = (float) Math.atan2(moveToY, moveToX);
		xMove = (float) (speed * Math.cos(angle) / slownessStatus.getSlowness());
		yMove = (float) (speed * Math.sin(angle) / slownessStatus.getSlowness());

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

	Line2D z2p;
	boolean intersection = false;

	public boolean checkForObstacles() {
		Player player = handler.getPlayer();
		z2p = new Line2D.Float(x + width / 2, y + height / 2, player.getCenterX(), player.getCenterY());
		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (z2p.intersects(e.getCollisionBounds(0, 0)))
				if (!handler.getWorld().getEntityManager().getBarriers().contains(e))
					return true;
		}
		for (Wall e : handler.getWorld().getEntityManager().getWalls()) {
			if (z2p.intersects(e.getCollisionBounds(0, 0)))
				return true;
		}
		return false;
	}

	public void followPath() {
		source = handler.getWorld().getClosestNode(getCenterX(), getCenterY());
		player = handler.getPlayer().getClosestNode();
		goTo = handler.getWorld().getNextStep(source, player);

		if (handler.getWorld().getNodes().get(source).checkWithinNode(this)) {
			source = goTo.getVertex();
			goTo = handler.getWorld().getNextStep(source, player);
		}
		if (goTo == null) {
			meander();
			move();
		} else if (source == goTo.getVertex() && goTo.checkWithinNode(this)) {
			meander();
			move();
		} else {
			float moveToX = goTo.getX() - getCenterX();
			float moveToY = goTo.getY() - getCenterY();
			float angle = (float) Math.toDegrees(Math.atan2(moveToY, moveToX));

			xMove = (float) (speed * (float) Math.cos(Math.toRadians(angle)) / slownessStatus.getSlowness());
			yMove = (float) (speed * (float) Math.sin(Math.toRadians(angle)) / slownessStatus.getSlowness());

			if (!checkEntityCollisions(xMove, 0f))
				moveX();
			if (!checkEntityCollisions(0f, yMove))
				moveY();

		}
	}

	public void followPlayer() {
		source = -1;
		player = -1;
		goTo = null;

		xMove = 0;
		yMove = 0;

		float moveToX = handler.getPlayer().getX() - x;
		float moveToY = handler.getPlayer().getY() - y;

		float angle = (float) Math.toDegrees(Math.atan2(moveToY, moveToX));
		xMove = (float) (speed * (float) Math.cos(Math.toRadians(angle)) / slownessStatus.getSlowness());
		yMove = (float) (speed * (float) Math.sin(Math.toRadians(angle)) / slownessStatus.getSlowness());

		if (!checkEntityCollisions(xMove, 0f))
			moveX();
		if (!checkEntityCollisions(0f, yMove))
			moveY();
	}

	float rotationAngle;

	@Override
	public void render(Graphics g) {
		float moveToX, moveToY;

		if (xMove == 0 && yMove == 0) {

		} else if (!freezeStatus.isFrozen()) {
			moveToX = x - (x + xMove);
			moveToY = y - (y + yMove);
			rotationAngle = (float) Math.toDegrees(Math.atan2(-moveToX, moveToY));
		}
		if (!isCrawler)
			g.drawImage(Assets.shadow, (int) (getRenderX()), (int) (getRenderY()), width, height, null);

		if (burnStatus.isBurning()) {
			g.setColor(Color.orange);
			g.fillOval((int) (getRenderX()), (int) (getRenderY()), width, height);
		}

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.rotate(Math.toRadians(rotationAngle), getRenderX() + width / 2,
				getRenderY() + height / 2);

		BufferedImage currentImage = Assets.crawler;
		if (freezeStatus.isFrozen())
			currentImage = Assets.frozenZombie;
		 else if (isCrawler) 
			g2d.drawImage(Assets.crawler, (int) (x - (height * 1.25 / 2) - handler.getGameCamera().getxOffset()),
					(int) (y - (width * 1.25 / 2) - handler.getGameCamera().getyOffset()), (int) (width * 1.25),
					(int) (height * 1.25), null);
		 else if (justAttacked)
			currentImage = zombieAttackAnim.getCurrentFrame();
		 else
			currentImage = zombieAnim.getCurrentFrame();

		if(!isCrawler)
			g2d.drawImage(currentImage, (int) (getRenderX()), (int) (getRenderY()), width, height, null);
		g2d.setTransform(old);

		if (handler.getSettings().isHealthBar())
			healthBar.render(g);

	}

	@Override
	public void die() {
		if (handler.getPlayer().getInv().getVamp() >= 0)
			handler.getPlayer().incrementTempHealth(1);
		if (handler.getPlayer().getInv().getStronghold() == 3) {
			if (handler.getPlayer().getStrongholdRadius() != null) {
				if (handler.getPlayer().getStrongholdRadius().intersects(getHitBox(0, 0))) {
					handler.getPlayer().gainArmor(5);
					handler.getPlayer().gainStrongholdDamageMultiplier(.05f);
				}
			}
		}

		handler.getPlayer().getStats().gainKill();
		handler.getWorld().getEntityManager().addBlood(new Blood(handler, x, y, zombieType));

		int vertex = handler.getWorld().getClosestNode((int) (getCenterX()), (int) (getCenterY()));
		// Node node = ;
		if (handler.getWorld().getNodes().get(vertex).withinPlayable()) {
			if (handler.getRoundLogic().getPowerups().isPowerUpReady()) {
				handler.getWorld().getEntityManager()
						.addPowerUp(handler.getRoundLogic().getPowerups().generatePowerUp(x, y));
			}
			if (handler.getRoundLogic().getZombiesLeft() <= 1 && handler.getRoundLogic().isDogRound()) {
				handler.getWorld().getEntityManager().addPowerUp(new MaxAmmo(handler, x, y));
			}
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
			if (e.equals(this))
				continue;
			if (e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			if (e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		for (Wall e : handler.getWorld().getEntityManager().getWalls()) {
			if (e.getCollisionBounds(0, 0).intersects(getCollisionBounds(xOffset, yOffset))) {
				return true;
			}
		}
		return false;
	}

	public boolean justAttacked() {
		return justAttacked;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public int getZombieType() {
		return zombieType;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public BurnStatus getBurnStatus() {
		return burnStatus;
	}

	public FreezeStatus getFreezeStatus() {
		return freezeStatus;
	}
}
