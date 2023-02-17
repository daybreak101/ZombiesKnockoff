package entities.creatures.playerinfo;

import java.awt.Graphics;
import java.util.Random;

import entities.bullets.GasGrenade;
import entities.bullets.Grenade;
import entities.creatures.Player;
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
	private Blessings blessings;

	private int currentGun;
	private Gun[] arsenal;
	private Perk[] perks;
	private int points;
	private int grenades;
	private Knife knife;
	private GasMask gasMask;
	private int specialGrenadeAmt;
	private int specialGrenadeType;

	private Timer switchWeaponTimer;
	private Timer grenadeReadyTimer;

	// perk variables
	// -1 means not equipped, 0-3 represents perk levels with 0 being base level
	private int jugg = -1, doubletap = -1, speedcola = -1, deadshot = -1, staminup = -1, phd = -1, vamp = -1, mule = -1,
			bandolier = -1, stronghold = -1, revive = -1, luna = -1;
	public boolean strongholdActivation = false;

	public Inventory(Handler handler, Player player) {
		this.handler = handler;
		this.player = player;
		this.blessings = new Blessings(handler);
		switchWeaponTimer = new Timer(30);
		grenadeReadyTimer = new Timer(30);

		arsenal = new Gun[4];
		arsenal[0] = new Glock17(handler);
		arsenal[1] = null;
		arsenal[2] = null;
		arsenal[3] = new Minigun(handler);
		currentGun = 0;

		perks = new Perk[8];
		perks[0] = null;
		perks[1] = null;
		perks[2] = null;
		perks[3] = null;
		perks[4] = null;
		perks[5] = null;
		perks[6] = null;
		perks[7] = null;

		knife = new Knife(handler);
		gasMask = new GasMask(handler);
		gasMask.setCurrentDurability(0);

		specialGrenadeType = 0;
		specialGrenadeAmt = 3;

		grenades = 0;

		points = 500;

	}

	public void tick() {
		blessings.tick();
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
		perks[0] = null;
		perks[1] = null;
		perks[2] = null;
		perks[3] = null;
		jugg = -1;
		doubletap = -1;
		speedcola = -1;
		deadshot = -1;
		staminup = -1;
		phd = -1;
		vamp = -1;
		mule = -1;
		bandolier = -1;
		stronghold = -1;
		revive = -1;
		luna = -1;
	}

	public boolean throwGrenade() {
		if (grenadeReadyTimer.isReady() && grenades > 0) {
			if (mule >= 1) {
				Random rand = new Random();
				int rng = rand.nextInt(3);
				if (rng != 1) {
					grenades--;
				}
			} else {
				grenades--;
			}
			handler.getWorld().getEntityManager().addEntity(new Grenade(handler, player.getX() + player.getWidth() / 2,
					player.getY() + player.getHeight() / 2, false,

					handler.getMouseManager().getMouseX() + handler.getGameCamera().getxOffset(),
					handler.getMouseManager().getMouseY() + handler.getGameCamera().getyOffset()
					));
			if (phd >= 1) {
				handler.getWorld().getEntityManager().addEntity(new Grenade(handler,
						player.getX() + player.getWidth() / 2 + 20, player.getY() + player.getHeight() / 2 + 20, false,

						handler.getMouseManager().getMouseX() + handler.getGameCamera().getxOffset(),
						handler.getMouseManager().getMouseY() + handler.getGameCamera().getyOffset()
						));
				handler.getWorld().getEntityManager().addEntity(new Grenade(handler,
						player.getX() + player.getWidth() / 2 - 20, player.getY() + player.getHeight() / 2 - 20, false,

						handler.getMouseManager().getMouseX() + handler.getGameCamera().getxOffset(),
						handler.getMouseManager().getMouseY() + handler.getGameCamera().getyOffset()
						 ));
			}
			return true;
		}
		return false;
	}

	public boolean throwSpecialGrenade() {
		if (grenadeReadyTimer.isReady() && specialGrenadeAmt > 0 && specialGrenadeType != -1) {
			if (mule >= 2) {
				Random rand = new Random();
				int rng = rand.nextInt(5);
				if (rng != 1) {
					specialGrenadeAmt--;
				}
			} else {
				specialGrenadeAmt--;
			}
			switch (specialGrenadeType) {
			case 0:
				handler.getWorld().getEntityManager().addEntity(new GasGrenade(handler,
						player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2,
						handler.getMouseManager().getMouseX() + handler.getGameCamera().getxOffset(),
						handler.getMouseManager().getMouseY() + handler.getGameCamera().getyOffset()));
				break;
			}

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
				else if (arsenal[2] != null && mule > -1)
					currentGun = 2;
			}
			// if on secondary, switch to primary, if it exists
			else if (currentGun == 1) {
				if (arsenal[2] != null && mule > -1)
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
				else if (arsenal[2] != null && mule > -1)
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

		if (specialGrenadeType != -1)
			specialGrenadeAmt = 3;
	}

	public void infiniteAmmo() {
		arsenal[currentGun].setCurrentClip(arsenal[currentGun].getClip());
		arsenal[currentGun].setReloading(false);
		grenades = 4;
		if (specialGrenadeType != -1)
			specialGrenadeAmt = 3;
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
		if (blessings.isRunning() && blessings.getBlessing() == "Extra Change")
			add = add + add / 2;
		if (handler.getRoundLogic().getPowerups().isDoublePointsActive())
			add = add * 2;
		points += add;
		player.getStats().gainScore(add);
		handler.getHud().addObject(new PointGainElement(handler, add, true));
		blessings.addPoints(add);
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
		} else if (arsenal[2] == null && mule > -1) {
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
		for (int i = 0; i < 8; i++) {
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
				if (perk.getLevel() == 3 && i != 3) {
					perk.setLevel(2);
				}
				perks[i] = perk;
				perks[i].buff();
				break;
			}
		}
	}

	public void givePerk(Perk perk) {
		for (int i = 0; i < 8; i++) {
			if (perks[i] == null) {
				if (perk.getLevel() == 3 && i != 3) {
					perk.setLevel(2);
				}
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

	public int getCurrentGun() {
		return currentGun;
	}

	public void setCurrentGun(int currentGun) {
		this.currentGun = currentGun;
	}

	public void setJugg(int jugg) {
		this.jugg = jugg;
		player.setHealth();
	}

	public int getDoubletap() {
		return doubletap;
	}

	public void setDoubletap(int doubletap) {
		this.doubletap = doubletap;
	}

	public int getSpeedcola() {
		return speedcola;
	}

	public void setSpeedcola(int speedcola) {
		this.speedcola = speedcola;
	}

	public int getDeadshot() {
		return deadshot;
	}

	public void setDeadshot(int deadshot) {
		this.deadshot = deadshot;
	}

	public int getStaminup() {
		return staminup;
	}

	public void setStaminup(int staminup) {
		this.staminup = staminup;
	}

	public int getPhd() {
		return phd;
	}

	public void setPhd(int phd) {
		this.phd = phd;
	}

	public int getVamp() {
		return vamp;
	}

	public void setVamp(int vamp) {
		this.vamp = vamp;
	}

	public int getMule() {
		return mule;
	}

	public void setMule(int mule) {
		this.mule = mule;
	}

	public int getBandolier() {
		return bandolier;
	}

	public void setBandolier(int bandolier) {
		this.bandolier = bandolier;
	}

	public int getStronghold() {
		return stronghold;
	}

	public void setStronghold(int stronghold) {
		this.stronghold = stronghold;
	}

	public int getRevive() {
		return revive;
	}

	public void setRevive(int revive) {
		this.revive = revive;
	}

	public int getLuna() {
		return luna;
	}

	public void setLuna(int luna) {
		this.luna = luna;
	}

	public int getJugg() {
		return jugg;
	}

	public int getSpecialGrenadeAmt() {
		return specialGrenadeAmt;
	}

	public int getSpecialGrenadeType() {
		return specialGrenadeType;
	}

	public void setSpecialGrenade(int type) {
		specialGrenadeType = type;
		specialGrenadeAmt = 3;
	}

	public Blessings getBlessings() {
		return blessings;
	}
}
