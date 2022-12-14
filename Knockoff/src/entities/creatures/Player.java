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
import entities.creatures.playerinfo.Stats;
import entities.statics.Barrier;
import entities.statics.InteractableStaticEntity;
import entities.statics.traps.IcyWater;
import graphics.Assets;
import hud.LeaderboardElement;
import main.Handler;
import perks.Perk;
import states.PauseState;
import states.State;
import utils.Timer;
import utils.Utils;
import weapons.Gun;

public class Player extends Creature {

	Inventory inv;
	Stats stats;
	Rectangle cb;
	int timer = 0;
	private float weight;
	private float defaultSpeed;
	private int armor;

	private boolean justTookDamage = false;

	// sprint variables
	private float isSprinting = 1;
	private int currentStamina, maxStamina;
	private int staminaTicker, staminaCooldown;
	private boolean inWater = false;
	private boolean isFrozen = false;

	int strongholdArmor = 0;
	float strongholdDamageMultiplier = 0.0f;

	public Player(Handler handler, float x, float y) {
		super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
		inv = new Inventory(handler, this);
		stats = new Stats(handler);
		bounds.x = 0;
		bounds.y = 0;
		bounds.width = 75;
		bounds.height = 75;

		currentStamina = 200;
		maxStamina = 200;
		staminaTicker = 0;
		staminaCooldown = 180;

		speed = 4.0f;
		defaultSpeed = speed;
		health = 100;
	}
	
	Timer tookDamageTimer = new Timer(40);

	@Override
	public void tick() {

		if (health <= 0 && inv.isRevive()) {
			reviving();
		} else if (health <= 0) {
			die();
		} else {
			checkIfInIcyWater();
			freezing();
			tookDamageTimer.tick();
			if(tookDamageTimer.isReady()) {
				justTookDamage = false;
				tookDamageTimer.resetTimer();
			}
			if (isFrozen)
				getInput();
			breakCooldown.tick();
			if (!isFrozen) {
				isSprinting = 1;

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

				sprinting();
				burn();
			}
		}
	}

	int iceCounter = 0, iceMax = 300;

	public void freezing() {
		if (inWater) {
			iceCounter++;
		} else {
			iceCounter--;
		}
		if (iceCounter >= iceMax) {
			isFrozen = true;
		}
	}

	public void checkIfInIcyWater() {
		boolean found = false;
		for (Areas e : handler.getWorld().getEntityManager().getAreas()) {
			if (((IcyWater) e).checkIfEntityIsContained(getHitbox())) {
				inWater = true;
				found = true;
			}
		}
		if (!found) {
			inWater = false;
		}
	}

	Timer breakCooldown = new Timer(60);
	private int breakCounter = 0;

	public void breakFreeFromIce() {
		if (breakCooldown.isReady()) {
			breakCooldown.resetTimer();
			breakCounter++;
		}
		if (breakCounter >= 3) {
			breakCounter = 0;
			isFrozen = false;
			iceCounter = 0;
		}
	}

	Timer overallBurnTimer = new Timer(300);
	Timer tickBurnTimer = new Timer(60);
	boolean isBurning = false;
	int burnDamage = 0;

	public void burn() {
		if (isBurning) {
			overallBurnTimer.tick();
			tickBurnTimer.tick();
			if (tickBurnTimer.isReady()) {
				takeDamage(burnDamage);
				System.out.println("BURN");
				tickBurnTimer.resetTimer();
			}
			if (overallBurnTimer.isReady()) {
				isBurning = false;
				overallBurnTimer.resetTimer();
			}
		}
	}

	public void setBurn(int damage) {
		isBurning = true;
		overallBurnTimer.resetTimer();
		burnDamage = damage;
	}

	public void reviving() {
		inv.wipePerksWhenDowned();
		setHealth();
	}

	public void sprinting() {
		if (isSprinting == 1) {
			staminaTicker++;
			if (staminaTicker >= staminaCooldown && currentStamina < maxStamina) {
				currentStamina++;
				currentStamina++;
				if (inv.isStaminup())
					currentStamina++;
			}
		}
	}

	public Rectangle getHitbox() {
		return new Rectangle((int) (x + bounds.x + 5), (int) (y + bounds.y + 5), 
				bounds.width - 10,
				bounds.height - 10);

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
		health = health - damage;
		if (isFrozen) {
			isFrozen = false;
			iceCounter = 0;
		}
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
		if (!isFrozen) {
			if (inWater) {
				slowdown += 1;
			}

			if (handler.getKeyManager().sprint && (handler.getKeyManager().w || handler.getKeyManager().a
					|| handler.getKeyManager().s || handler.getKeyManager().d)) {
				sprint();
				moved = true;
			}

			if (handler.getKeyManager().w && handler.getKeyManager().a) {
				yMove = (float) (-speed * isSprinting * Math.sqrt(2) / 2 / slowdown);
				xMove = (float) (-speed * isSprinting * Math.sqrt(2) / 2 / slowdown);
				moved = true;
			} else if (handler.getKeyManager().s && handler.getKeyManager().d) {
				yMove = (float) (speed * isSprinting * Math.sqrt(2) / 2 / slowdown);
				xMove = (float) (speed * isSprinting * Math.sqrt(2) / 2 / slowdown);
				moved = true;
			} else if (handler.getKeyManager().s && handler.getKeyManager().a) {
				yMove = (float) (speed * isSprinting * Math.sqrt(2) / 2 / slowdown);
				xMove = (float) (-speed * isSprinting * Math.sqrt(2) / 2 / slowdown);
				moved = true;
			} else if (handler.getKeyManager().w && handler.getKeyManager().d) {
				xMove = (float) (speed * isSprinting * Math.sqrt(2) / 2 / slowdown);
				yMove = (float) (-speed * isSprinting * Math.sqrt(2) / 2 / slowdown);
				moved = true;
			} else if (handler.getKeyManager().w) {
				yMove = -speed * isSprinting / slowdown;
				moved = true;
			} else if (handler.getKeyManager().s) {
				yMove = speed * isSprinting / slowdown;
				moved = true;
			} else if (handler.getKeyManager().a) {
				xMove = -speed * isSprinting / slowdown;
				moved = true;
			} else if (handler.getKeyManager().d) {
				xMove = speed * isSprinting / slowdown;
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
				throwGrenade();
			if (handler.getMouseManager().isMouseScrolled()) {
				inv.switchWeapon();
				handler.getMouseManager().setMouseScrolled(false);
			}
			if (handler.getKeyManager().melee) {
				inv.getKnife().damageNearbyZombie();
			}
		}
		if (isFrozen) {
			if (handler.getKeyManager().melee) {
				breakFreeFromIce();
			}
		}
		if (handler.getKeyManager().escape) {
			handler.getHud().setInvisible();
			State.setState(new PauseState(handler));
		}
		if (handler.getKeyManager().capslock) {
			handler.getHud().getGameplayHUD().setVisible(false);
			handler.getHud().getScoreboard().setVisible(true);
			//handler.getHud().getObjects().clear();
			//handler.getHud().getObjects().add(new Scoreboard(handler));
		} else {
			handler.getHud().getScoreboard().setVisible(false);
			handler.getHud().getGameplayHUD().setVisible(true);
			//handler.getHud().getObjects().clear();
			//handler.getHud().getObjects().add(new GameplayElement(handler));
		}

	}
	
	public boolean checkEntityCollisions(float xOffset, float yOffset) {
		for(Entity e: handler.getWorld().getEntityManager().getEntities()) {
			if(e.equals(this))
				continue;
			
			if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		for(Barrier e: handler.getWorld().getEntityManager().getBarriers()) {
			if(e.getPlayerBarrier().intersects(getCollisionBounds(xOffset, yOffset))) {
				return true;
			}
		}
		return false;
	}

	public void sprint() {
		if (currentStamina > 0) {
			currentStamina--;
			staminaTicker = 0;
			if (inv.isStaminup())
				isSprinting = 1.8f;
			else
				isSprinting = 1.5f;
		}

	}

	public void throwGrenade() {
		if (inv.throwGrenade()) {
			handler.getWorld().getEntityManager().addEntity(new Grenade(handler, x, y));
		}
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
		
		if(closestInteract != null) {
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

		if (isBurning) {
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
			if (isFrozen) {
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

	public void maxAmmo() {
		inv.maxAmmo();
	}

	public void infiniteAmmo() {
		inv.infiniteAmmo();
	}

	public void purchaseAmmo() {
		inv.purchaseAmmo();
	}

	public void roundReplenishGrenades() {
		inv.roundReplenishGrenades();
	}

	public void gainPoints(int add) {
		inv.gainPoints(add);
	}

	public boolean purchase(int price) {
		if (inv.purchase(price)) {
			return true;
		}
		return false;
	}

	public void gainArmor(int dArmor) {
		if (armor < 50) {
			armor += dArmor;
		}
		if (armor > 50) {
			armor = 50;
		}
	}

	public void gainStrongholdDamageMultiplier(float dDamageMultiplier) {
		if (strongholdDamageMultiplier < 1f) {
			strongholdDamageMultiplier += dDamageMultiplier;
		}
		if (strongholdDamageMultiplier > 1f) {
			strongholdDamageMultiplier = 1f;
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
		if (inv.isJugg() && !(health > 150))
			health = 150;
		else if (!(health > 125)) {
			health = 100;
		}
	}

	public void incrementHealth() {
		if (inv.isJugg() && health < 175)
			health++;
		if (health < 125)
			health++;
	}

	public void setGun(Gun gun) {
		inv.setGun(gun);
	}

	public boolean checkArsenal(Gun gun) {
		if (inv.checkArsenal(gun)) {
			return true;
		}
		return false;
	}

	public boolean checkPerks(Perk perk) {
		if (inv.checkPerks(perk)) {
			return true;
		}
		return false;
	}

	public void addPerk(Perk perk) {
		inv.addPerk(perk);
	}

	public boolean checkPerkEmptySpot() {
		if (inv.checkPerkEmptySpot()) {
			return true;
		}
		return false;
	}

	public void setDefaultSpeed(float defaultSpeed) {
		this.defaultSpeed = defaultSpeed;
	}

	public void removeGunForUpgrade() {
		inv.removeGunForUpgrade();
	}

	public int getCurrentStamina() {
		return currentStamina;
	}

	public void setCurrentStamina(int currentStamina) {
		this.currentStamina = currentStamina;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}

	public void justTookDamage() {
		justTookDamage = true;
	}

	public Inventory getInv() {
		return inv;
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
	

}
