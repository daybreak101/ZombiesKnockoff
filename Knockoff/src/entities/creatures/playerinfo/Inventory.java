package entities.creatures.playerinfo;

import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D.Float;

import entities.Entity;
import entities.creatures.Player;
import entities.creatures.Zombie;
import entities.statics.InteractableStaticEntity;
import hud.PointGainElement;
import input.MouseManager;
import main.Handler;
import perks.Perk;
import utils.Timer;
import weapons.Glock17;
import weapons.Gun;
import weapons.Knife;
import weapons.Minigun;

public class Inventory {
	private Handler handler;
	private Player player;

	private int currentGun;
	private Gun[] arsenal;
	private Perk[] perks;
	private int points;
	private int grenades;
	private Knife knife;
	private GasMask gasMask;

	private Timer switchWeaponTimer;
	private Timer grenadeReadyTimer;

	// perk variables
	private boolean jugg, doubletap, speedcola, deadshot, staminup, phd, vamp, mule, bandolier, stronghold, revive,
			luna;
	public boolean strongholdActivation = false;

	public Inventory(Handler handler, Player player) {
		this.handler = handler;
		this.player = player;
		switchWeaponTimer = new Timer(30);
		grenadeReadyTimer = new Timer(30);

		arsenal = new Gun[4];
		arsenal[0] = new Glock17(handler);
		arsenal[1] = null;
		arsenal[2] = null;
		arsenal[3] = new Minigun(handler);
		currentGun = 0;

		perks = new Perk[4];
		perks[0] = null;
		perks[1] = null;
		perks[2] = null;
		perks[3] = null;

		knife = new Knife(handler);
		gasMask = new GasMask(handler);
		gasMask.setCurrentDurability(0);

		grenades = 0;

		points = 500;

		jugg = false;
		doubletap = false;
		speedcola = false;
		deadshot = false;
		staminup = false;
		phd = false;
		vamp = false;
		mule = false;
		bandolier = false;
		stronghold = false;
		revive = false;
		luna = false;
	}

	public void tick() {
		switchWeaponTimer.tick();
		grenadeReadyTimer.tick();
		knife.tick();
		for (Perk i : perks) {
			if (i != null) {
				i.tick();
			}
		}
	}

	public void render(Graphics g) {
		drawLaser(g);
		knife.render(g);
		for (Perk i : perks) {
			if (i != null) {
				i.render(g);
			}
		}
	}

	public void drawLaser(Graphics g) {
		MouseManager mouse = handler.getMouseManager();
		int size = 7;
		g.setColor(handler.getSettings().getLaserColor());

		g.fillOval(mouse.getMouseX() - size / 2, mouse.getMouseY() - size / 2, size, size);
	}

	public void wipePerksWhenDowned() {
		for (Perk p : perks) {
			if (p != null)
				p.debuff();
		}
		/*
		 * perks[0].debuff(); perks[1].debuff(); perks[2].debuff(); perks[3].debuff();
		 */
		perks[0] = null;
		perks[1] = null;
		perks[2] = null;
		perks[3] = null;
		jugg = false;
		doubletap = false;
		speedcola = false;
		deadshot = false;
		staminup = false;
		phd = false;
		vamp = false;
		mule = false;
		bandolier = false;
		stronghold = false;
		revive = false;
		luna = false;
	}

	public boolean throwGrenade() {
		if (grenadeReadyTimer.isReady() && grenades > 0) {
			grenades--;
			return true;
		}
		return false;
	}

	public void switchWeapon() {
		if (switchWeaponTimer.counter >= switchWeaponTimer.limit) {
			// switchedWeapon = true;
			switchWeaponTimer.counter = 0;

			// cancel reload progress when switching weapons
			if (getGun() != null) {
				if (getGun().getIsReloading() == true) {
					getGun().setReloading(false);
					getGun().setReloadTimer(0);
				}
			}

			// if on primary, switch to secondary, if it exists
			if (currentGun == 0) {
				if (arsenal[1] != null)
					currentGun = 1;
				else if (arsenal[2] != null && mule)
					currentGun = 2;
			}
			// if on secondary, switch to primary, if it exists
			else if (currentGun == 1) {
				if (arsenal[2] != null && mule)
					currentGun = 2;
				else if (arsenal[0] != null)
					currentGun = 0;
			} else if (currentGun == 2) {
				if (arsenal[0] != null)
					currentGun = 0;
				else if (arsenal[1] != null)
					currentGun = 1;
			} else if (currentGun == 3) {
				if (arsenal[0] != null)
					currentGun = 0;
				else if (arsenal[1] != null)
					currentGun = 1;
				else if (arsenal[2] != null && mule)
					currentGun = 2;
				else
					currentGun = 0;
			}
		}
	}

	public void maxAmmo() {
		if (arsenal[0] != null) {
			if (arsenal[0].getOriginalName() == "Flamethrower")
				arsenal[0].setCurrentClip(arsenal[0].getClip());
			else
				arsenal[0].setCurrentReserve(arsenal[0].getMaxReserve());
		}
		if (arsenal[1] != null) {
			if (arsenal[1].getOriginalName() == "Flamethrower")
				arsenal[1].setCurrentClip(arsenal[1].getClip());
			else
				arsenal[1].setCurrentReserve(arsenal[1].getMaxReserve());

		}
		if (arsenal[2] != null) {
			if (arsenal[2].getOriginalName() == "Flamethrower")
				arsenal[2].setCurrentClip(arsenal[2].getClip());
			else
				arsenal[2].setCurrentReserve(arsenal[2].getMaxReserve());

		}
		grenades = 4;
	}

	public void infiniteAmmo() {
		arsenal[currentGun].setCurrentClip(arsenal[currentGun].getClip());
		arsenal[currentGun].setReloading(false);
		grenades = 4;
	}

	public void purchaseAmmo() {
		if (arsenal[currentGun].getOriginalName() == "Flamethrower")
			arsenal[currentGun].setCurrentClip(arsenal[currentGun].getClip());
		else
			arsenal[currentGun].setCurrentReserve(arsenal[currentGun].getMaxReserve());
	}

	public void roundReplenishGrenades() {
		grenades = grenades + 2;
		if (grenades > 4) {
			grenades = 4;
		}
	}

	public void gainPoints(int add) {
		if (handler.getRoundLogic().getPowerups().isDoublePointsActive()) {
			points += (add * 2);
			player.getStats().gainScore(add * 2);
			handler.getHud().addObject(new PointGainElement(handler, add * 2, true));
		} else {
			points += add;
			player.getStats().gainScore(add);
			handler.getHud().addObject(new PointGainElement(handler, add, true));
		}
	}

	public boolean purchase(int price) {
		if (price <= points) {
			points -= price;
			handler.getHud().addObject(new PointGainElement(handler, price, false));
			return true;
		}
		return false;
	}

	public void setGun(Gun gun) {
		if (arsenal[0] == null) {
			arsenal[0] = gun;
			currentGun = 0;
		} else if (arsenal[1] == null) {
			arsenal[1] = gun;
			currentGun = 1;
		} else if (arsenal[2] == null && mule) {
			arsenal[2] = gun;
			currentGun = 2;
		} else
			arsenal[currentGun] = gun;
	}

	public boolean checkArsenal(Gun gun) {
		for (int i = 0; i < 3; i++) {
			if (arsenal[i] != null) {
				if (arsenal[i].getOriginalName() == gun.getName()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkPerks(Perk perk) {
		for (int i = 0; i < 4; i++) {
			if (perks[i] != null) {
				if (perks[i].getName() == perk.getName()) {
					return true;
				}
			}
		}
		return false;
	}

	public void addPerk(Perk perk) {
		for (int i = 0; i < 4; i++) {
			if (perks[i] == null) {
				perks[i] = perk;
				perks[i].buff();
				break;
			}
		}
	}

	public boolean checkPerkEmptySpot() {
		for (int i = 0; i < 4; i++) {
			if (perks[i] == null) {
				return true;
			}
		}
		return false;
	}

	public void removeGunForUpgrade() {
		arsenal[currentGun] = null;
		switchWeapon();
	}

	public Gun[] getArsenal() {
		return arsenal;
	}

	public Knife getKnife() {
		return knife;
	}

	public void setKnife(Knife newMelee) {
		knife = newMelee;
	}

	public void setGasMask(GasMask gasMask) {
		this.gasMask = gasMask;
	}

	public GasMask getGasMask() {
		return gasMask;
	}

	public int getGrenades() {
		return grenades;
	}

	public int getPoints() {
		return points;
	}

	public Gun getGun() {
		return arsenal[currentGun];
	}

	public Perk[] getPerks() {
		return perks;
	}

	public boolean isDoubletap() {
		return doubletap;
	}

	public void setDoubletap(boolean doubletap) {
		this.doubletap = doubletap;
	}

	public boolean isVamp() {
		return vamp;
	}

	public void setVamp(boolean vamp) {
		this.vamp = vamp;
	}

	public boolean isMule() {
		return mule;
	}

	public void setMule(boolean mule) {
		this.mule = mule;
	}

	public boolean isStronghold() {
		return stronghold;
	}

	public void setStronghold(boolean stronghold) {
		this.stronghold = stronghold;
	}

	public boolean isRevive() {
		return revive;
	}

	public void setRevive(boolean revive) {
		this.revive = revive;
	}

	public int getCurrentGun() {
		return currentGun;
	}

	public void setCurrentGun(int currentGun) {
		this.currentGun = currentGun;
	}

	public boolean isJugg() {
		return jugg;
	}

	public void setJugg(boolean jugg) {
		this.jugg = jugg;
		player.setHealth();
	}

	public boolean isSpeedcola() {
		return speedcola;
	}

	public void setSpeedcola(boolean speedcola) {
		this.speedcola = speedcola;
	}

	public boolean isDeadshot() {
		return deadshot;
	}

	public void setDeadshot(boolean deadshot) {
		this.deadshot = deadshot;
	}

	public boolean isStaminup() {
		return staminup;
	}

	public void setStaminup(boolean staminup) {
		this.staminup = staminup;
	}

	public boolean isPhd() {
		return phd;
	}

	public void setBandolier(boolean bandolier) {
		this.bandolier = bandolier;
	}

	public boolean isBandolier() {
		return bandolier;
	}

	public void setPhd(boolean phd) {
		this.phd = phd;
	}

	public boolean isLuna() {
		return luna;
	}

	public void setLuna(boolean luna) {
		this.luna = luna;
	}

}
