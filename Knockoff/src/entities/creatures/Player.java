package entities.creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import entities.Entity;
import entities.areas.Areas;
import entities.bullets.Grenade;
import entities.creatures.playerinfo.Inventory;
import entities.creatures.playerinfo.PlayerSprint;
import entities.creatures.playerinfo.Stats;
import entities.creatures.zombieinfo.BurnStatus;
import entities.creatures.zombieinfo.FreezeStatus;
import entities.statics.Barrier;
import entities.statics.InteractableStaticEntity;
import entities.statics.Wall;
import entities.statics.traps.IcyWater;
import graphics.Assets;
import hud.LeaderboardElement;
import main.Handler;
import perks.Perk;
import states.PauseState;
import states.State;
import utils.Node;
import utils.Timer;
import utils.Utils;
import weapons.Gun;

public class Player extends Creature {

	Inventory inv;
	Stats stats;
	PlayerSprint playerSprint;
	BurnStatus burnStatus;
	FreezeStatus freezeStatus;
	
	int closestNode;

	Rectangle cb;
	int timer = 0;
	private float weight;
	private float defaultSpeed;

	private int tempHealth = 0;
	private int armor;

	private boolean justTookDamage = false;

	int strongholdArmor = 0;
	float strongholdDamageMultiplier = 0.0f;

	public Player(Handler handler, float x, float y) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		inv = new Inventory(handler, this);
		stats = new Stats(handler);
		playerSprint = new PlayerSprint(this);
		bounds = new Rectangle(0, 0, 75, 75);

		speed = 4.0f;
		defaultSpeed = speed;
		health = 100;
		
		burnStatus = new BurnStatus(this);
		freezeStatus = new FreezeStatus(handler, this);
	}

	Timer tookDamageTimer = new Timer(60);
	Timer tempHealthLossTimer = new Timer(120);

	@Override
	public void tick() {

		if (health <= 0 && inv.getRevive() > -1) {
			reviving();
		} else if (health <= 0) {
			die();
		} else {
			setClosestNode();
			freezeStatus.checkIfInIcyWater();
			freezeStatus.freezing();
			tookDamageTimer.tick();
			if (tookDamageTimer.isReady()) {
				justTookDamage = false;
				tookDamageTimer.resetTimer();
			}
			if(inv.getVamp() != 3 && tempHealth > 0) {
				tempHealthLossTimer.tick();
				if(tempHealthLossTimer.isReady()) {
					tempHealth--;
					tempHealthLossTimer.resetTimer();
				}
			}
			
			if (freezeStatus.isFrozen())
				getInput();
			freezeStatus.getBreakCooldown().tick();
			if (!freezeStatus.isFrozen()) {
				playerSprint.setSprintMultiplier(1);
				move();

				if (inv.getGun() != null) {
					inv.getGun().tick();
					weight = inv.getGun().getWeight();
					speed = defaultSpeed - weight;
				} else {
					speed = defaultSpeed;
				}
				inv.tick();
				getInput();
				handler.getGameCamera().centerOnEntity(this);

				playerSprint.sprinting();
				burnStatus.burn();
			}
		}
	}

	public void reviving() {
		inv.wipePerksWhenDowned();
		setHealth();
	}

	public Rectangle getHitbox() {
		return new Rectangle((int) (x + bounds.x + 15), (int) (y + bounds.y + 15), bounds.width - 30,
				bounds.height - 30);

	}

	public void takeDamage(int damage) {
		if (armor > 0) {
			armor = armor - damage;
			if (armor < 0) {
				damage = -armor;
				armor = 0;
			} else {
				damage = 0;
			}
		}
		if (tempHealth > 0) {
			tempHealth = tempHealth - damage;
			if (tempHealth < 0) {
				damage = -tempHealth;
				tempHealth = 0;
			} else {
				damage = 0;
			}
		}
		health = health - damage;
		freezeStatus.breakPlayerIceWhenHit();
	}

	boolean died = false;

	public void die() {
		if (!died) {
			stats.gainDown();
			handler.getGlobalStats().calculateNewAverageRound(handler.getRoundLogic().getCurrentRound());
			died = true;
			System.out.println("YOU LOSE");
			handler.getHud().getObjects().clear();
			handler.getHud().setInvisible();
			handler.getHud().getObjects().add(new LeaderboardElement(handler));
		}

	}

	public boolean moved = false;

	private void getInput() {
		xMove = 0;
		yMove = 0;
		moved = false;
		float slowdown = 1;
		if (!freezeStatus.isFrozen()) {
			if (freezeStatus.inWater()) {
				slowdown += 1;
			}

			if (handler.getKeyManager().sprint && (handler.getKeyManager().w || handler.getKeyManager().a
					|| handler.getKeyManager().s || handler.getKeyManager().d)) {
				playerSprint.sprint();
				moved = true;
			}

			if (handler.getKeyManager().w && handler.getKeyManager().a) {
				yMove = (float) (-speed * playerSprint.getSprintMultiplier() * Math.sqrt(2) / 2 / slowdown);
				xMove = (float) (-speed * playerSprint.getSprintMultiplier() * Math.sqrt(2) / 2 / slowdown);
				moved = true;
			} else if (handler.getKeyManager().s && handler.getKeyManager().d) {
				yMove = (float) (speed * playerSprint.getSprintMultiplier() * Math.sqrt(2) / 2 / slowdown);
				xMove = (float) (speed * playerSprint.getSprintMultiplier() * Math.sqrt(2) / 2 / slowdown);
				moved = true;
			} else if (handler.getKeyManager().s && handler.getKeyManager().a) {
				yMove = (float) (speed * playerSprint.getSprintMultiplier() * Math.sqrt(2) / 2 / slowdown);
				xMove = (float) (-speed * playerSprint.getSprintMultiplier() * Math.sqrt(2) / 2 / slowdown);
				moved = true;
			} else if (handler.getKeyManager().w && handler.getKeyManager().d) {
				xMove = (float) (speed * playerSprint.getSprintMultiplier() * Math.sqrt(2) / 2 / slowdown);
				yMove = (float) (-speed * playerSprint.getSprintMultiplier() * Math.sqrt(2) / 2 / slowdown);
				moved = true;
			} else if (handler.getKeyManager().w) {
				yMove = -speed * playerSprint.getSprintMultiplier() / slowdown;
				moved = true;
			} else if (handler.getKeyManager().s) {
				yMove = speed * playerSprint.getSprintMultiplier() / slowdown;
				moved = true;
			} else if (handler.getKeyManager().a) {
				xMove = -speed * playerSprint.getSprintMultiplier() / slowdown;
				moved = true;
			} else if (handler.getKeyManager().d) {
				xMove = speed * playerSprint.getSprintMultiplier() / slowdown;
				moved = true;
			}
			if (handler.getKeyManager().reload && inv.getGun() != null)
				inv.getGun().reload();
			if (handler.getKeyManager().switchWeapon2)
				inv.switchWeapon();
			if (handler.getMouseManager().isLeftPressed() && inv.getGun() != null) {
				inv.getGun().shoot();
			}

			if (handler.getKeyManager().use)
				interact();
			if (handler.getKeyManager().grenade)
				inv.throwGrenade();
			if (handler.getKeyManager().q)
				inv.throwSpecialGrenade();
			if (handler.getKeyManager().x)
				inv.getBlessings().activateBlessing();
			if (handler.getMouseManager().isMouseScrolled()) {
				inv.switchWeapon();
				handler.getMouseManager().setMouseScrolled(false);
			}
			if (handler.getKeyManager().melee) {
				inv.getKnife().damageNearbyZombie();
			}
		}
		if (freezeStatus.isFrozen()) {
			if (handler.getKeyManager().melee) {
				freezeStatus.breakFreeFromIce();
			}
		}
		if (handler.getKeyManager().escape) {
			handler.getHud().setInvisible();
			State.setState(new PauseState(handler));
		}
		if (handler.getKeyManager().capslock) {
			handler.getHud().getGameplayHUD().setVisible(false);
			handler.getHud().getScoreboard().setVisible(true);
		} else {
			handler.getHud().getScoreboard().setVisible(false);
			handler.getHud().getGameplayHUD().setVisible(true);
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
		for (Barrier e : handler.getWorld().getEntityManager().getBarriers()) {
			if (e.getPlayerBarrier().intersects(getCollisionBounds(xOffset, yOffset))) {
				return true;
			}
		}
		for (Wall e : handler.getWorld().getEntityManager().getWalls()) {
			if (e.getCollisionBounds(0, 0).intersects(getCollisionBounds(xOffset, yOffset))) {
				return true;
			}
		}
		return false;
	}

	public void interact() {
		Ellipse2D.Float radius = new Ellipse2D.Float(x - 100, y - 100, 200, 200);

		InteractableStaticEntity closestInteract = null;
		float closestDist = 2000000;
		float eDist;
		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			eDist = Utils.getEuclideanDistance(x, y, e.getX(), e.getY());
			if (closestInteract == null) {
				closestInteract = e;
				closestDist = eDist;
			}
			if (eDist < closestDist) {
				closestInteract = e;
				closestDist = eDist;
			}
		}

		if (closestInteract != null) {
			if (radius.intersects(closestInteract.getTriggerRange())) {
				closestInteract.fulfillInteraction();
			}
		}
	}

	public boolean checkIfInStrongholdCircle() {
		if (!strongholdRadius.intersects(getCollisionBounds(0, 0))) {
			getInv().strongholdActivation = false;
			removeArmor();
			removeStrongholdDamageMultiplier();
			return false;
		}
		return true;
	}

	private Ellipse2D strongholdRadius;

	public void setStrongholdCircle() {
		getInv().strongholdActivation = true;
		strongholdRadius = new Ellipse2D.Float(x + width / 2 - 125, y + height / 2 - 125, 250, 250);

	}

	public Ellipse2D getStrongholdRadius() {
		return strongholdRadius;
	}

	@Override
	public void render(Graphics g) {
		inv.render(g);
		float mouseX = handler.getMouseManager().getMouseX();
		float mouseY = handler.getMouseManager().getMouseY();
		float angle = (float) Math
				.toDegrees(Math.atan2(-(x - handler.getGameCamera().getxOffset() - mouseX + width / 2),
						y - handler.getGameCamera().getyOffset() - mouseY + height / 2));

		if (getInv().strongholdActivation) {
			g.setColor(new Color(0, 0, 200, 50));
			g.fillOval((int) (strongholdRadius.getX() - handler.getGameCamera().getxOffset()),
					(int) (strongholdRadius.getY() - handler.getGameCamera().getyOffset()), 250, 250);
			g.setColor(new Color(100, 0, 100));
			g.drawOval((int) (strongholdRadius.getX() - handler.getGameCamera().getxOffset()),
					(int) (strongholdRadius.getY() - handler.getGameCamera().getyOffset()), 250, 250);
		}

		g.drawImage(Assets.shadow, (int) (x - 10 - handler.getGameCamera().getxOffset()),
				(int) (y - 10 - handler.getGameCamera().getyOffset()), width, height, null);

		if (burnStatus.isBurning()) {
			g.setColor(Color.orange);
			g.fillOval((int) (x - 10 - handler.getGameCamera().getxOffset()),
					(int) (y - 10 - handler.getGameCamera().getyOffset()), width + 25, height + 25);
		}

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();

		if (health <= 0) {
			g2d.drawImage(Assets.player[3], (int) (x - handler.getGameCamera().getxOffset()),
					(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
			g2d.setTransform(old);
		} else {
			g2d.rotate(Math.toRadians(angle), x - handler.getGameCamera().getxOffset() + width / 2,
					y - handler.getGameCamera().getyOffset() + height / 2);
			if (freezeStatus.isFrozen()) {
				g2d.drawImage(Assets.player[4], (int) (x - handler.getGameCamera().getxOffset()),
						(int) (y - handler.getGameCamera().getyOffset()), width, height, null);

			} else if (justTookDamage == true) {
				g2d.drawImage(Assets.player[1], (int) (x - handler.getGameCamera().getxOffset()),
						(int) (y - handler.getGameCamera().getyOffset()), width, height, null);

				timer++;
				if (timer == 50) {
					justTookDamage = false;
					timer = 0;
				}
			} else if (health <= 50) {
				g2d.drawImage(Assets.player[2], (int) (x - handler.getGameCamera().getxOffset()),
						(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
			} else {
				g2d.drawImage(Assets.player[0], (int) (x - handler.getGameCamera().getxOffset()),
						(int) (y - handler.getGameCamera().getyOffset()), width, height, null);
			}

			g2d.setTransform(old);
		}
	}
	
	public void setClosestNode() {
		closestNode = handler.getWorld().getClosestNode(getCenterX(), getCenterY());
	}
	
	public int getClosestNode() {
		return closestNode;
	}

	public PlayerSprint getPlayerSprint() {
		return playerSprint;
	}

	public void gainArmor(int dArmor) {
		if (inv.getStronghold() >= 2) {
			if (armor < 50) {
				armor += dArmor;
			}
			if (armor > 50) {
				armor = 50;
			}
		} else if (inv.getStronghold() >= 0) {
			if (armor < 25) {
				armor += dArmor;
			}
			if (armor > 25) {
				armor = 25;
			}
		}

	}

	public void gainStrongholdDamageMultiplier(float dDamageMultiplier) {

		if (inv.getStronghold() >= 2) {
			if (strongholdDamageMultiplier < .5f) {
				strongholdDamageMultiplier += dDamageMultiplier;
			}
			if (strongholdDamageMultiplier > .5f) {
				strongholdDamageMultiplier = .5f;
			}
		} else if (inv.getStronghold() >= 1) {
			if (strongholdDamageMultiplier < .25f) {
				strongholdDamageMultiplier += dDamageMultiplier;
			}
			if (strongholdDamageMultiplier > .25f) {
				strongholdDamageMultiplier = .25f;
			}
		}
	}

	public void removeArmor() {
		if (armor > 0) {
			armor = 0;
		}
	}

	public void removeStrongholdDamageMultiplier() {
		if (strongholdDamageMultiplier > 0) {
			strongholdDamageMultiplier = 0;
		}
	}

	public int getArmor() {
		return armor;
	}

	public float getStrongholdDamageMultiplier() {
		return strongholdDamageMultiplier;
	}

	public void setHealth() {
		if (inv.getJugg() == 0 && !(health > 110)) {
			health = 110;
		} else if (inv.getJugg() == 1 && !(health > 125)) {
			health = 125;
		} else if (inv.getJugg() == 2 && !(health > 150)) {
			health = 150;
		} else if (inv.getJugg() == 3 && !(health > 200)) {
			health = 200;
		} else if (inv.getJugg() == -1 && !(health > 100)) {
			health = 100;
		}
	}

	public void incrementTempHealth(int increment) {
		if (inv.getVamp() >= 2) {
			if (inv.getJugg() == 0 && tempHealth + health < 135) {
				tempHealth += increment;
			} else if (inv.getJugg() == 1 && tempHealth + health < 150) {
				tempHealth += increment;
			} else if (inv.getJugg() == 2 && tempHealth + health < 175) {
				tempHealth += increment;
			} else if (inv.getJugg() == 3 && tempHealth + health < 225) {
				tempHealth += increment;
			} else if (inv.getJugg() == -1 && tempHealth + health < 125) {
				tempHealth += increment;
			}
		}
		else if(inv.getVamp() >= 0) {
			if (inv.getJugg() == 0 && tempHealth + health < 110) {
				tempHealth += increment;
			} else if (inv.getJugg() == 1 && tempHealth + health < 125) {
				tempHealth += increment;
			} else if (inv.getJugg() == 2 && tempHealth + health < 150) {
				tempHealth += increment;
			} else if (inv.getJugg() == 3 && tempHealth + health < 200) {
				tempHealth += increment;
			} else if (inv.getJugg() == -1 && tempHealth + health < 100) {
				tempHealth += increment;
			}
		}
//		if (health + tempHealth < 100)
//			tempHealth += increment;

	}

	public void takeExplosionDamage(int damage) {
		if (inv.getPhd() >= 2) {
			damage = 0;
		} else if (inv.getPhd() >= 0) {
			damage = damage / 2;
		}
		takeDamage(damage);
	}

	public void setDefaultSpeed(float defaultSpeed) {
		this.defaultSpeed = defaultSpeed;
	}

	public void justTookDamage() {
		justTookDamage = true;
	}

	public Inventory getInv() {
		return inv;
	}
	
	public BurnStatus getBurnStatus() {
		return burnStatus;
	}

	public Stats getStats() {
		return stats;
	}

	public void addToMoveX(int dx) {
		xMove += dx;
	}

	public boolean getJustTookDamage() {
		return justTookDamage;
	}

	public int getTempHealth() {
		return tempHealth;
	}
}
