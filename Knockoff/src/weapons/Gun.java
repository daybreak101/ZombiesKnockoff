package weapons;

import main.Handler;

public abstract class Gun {
	protected String name, upgradedName, originalName;
	protected int damage, fireRate, clip, maxReserve, reloadSpeed;
	protected Handler handler;
	protected int currentClip;
	protected int currentReserve;
	protected float weight;
	protected int range;

	protected boolean readyToFire = true;
	protected boolean isReloading = false;
	protected int timerToFire = 0;
	protected int reloadTimer = 0;

	protected boolean doubletap = false;
	protected boolean speedcola = false;

	protected boolean isUpgraded = false;

	// add weight that would be subtracted from player speed
	public Gun(Handler handler, int damage, int fireRate, int reloadSpeed, int clip, int maxReserve, float weight,
			int range) {
		this.handler = handler;
		this.damage = damage;
		this.fireRate = fireRate;
		this.reloadSpeed = reloadSpeed;
		this.clip = clip;
		if (handler.getPlayer() != null) {
			if (handler.getPlayer().getInv().isBandolier()) {
				this.maxReserve = maxReserve + clip;
			} 
			else {
				this.maxReserve = maxReserve;
			}
		}
		else {
			this.maxReserve = maxReserve;
		}

		this.currentClip = clip;
		this.currentReserve = maxReserve;
		this.weight = weight;
		this.range = range;
	}

	public void shoot() {}

	public void postTick() {}

	public void giveClip() {
		if (currentReserve + clip > maxReserve) {
			currentReserve = maxReserve;
		} else {
			currentReserve = currentReserve + clip;
		}
	}

	public void activateBandolier() {
		maxReserve = maxReserve + clip;
		giveClip();
	}

	public void deactivateBandolier() {
		maxReserve = maxReserve - clip;

		if (currentReserve - clip > 0) {
			currentReserve = currentReserve - clip;
		}
	}

	public void upgradeWeapon() {
		if (!isUpgraded) {
			isUpgraded = true;
			damage = damage * 2;
			clip = clip + clip / 2;
			maxReserve = maxReserve + maxReserve / 2;
			currentClip = clip;
			currentReserve = maxReserve;
			name = upgradedName;
		}
	}

	public void reloadFinish() {
		if (currentReserve < (clip - currentClip)) {
			currentClip += currentReserve;
			currentReserve = 0;
		} else {
			currentReserve = currentReserve - (clip - currentClip);
			currentClip = clip;
		}
	}

	// public abstract void render();
	public void tick() {
		doubletap = handler.getPlayer().getInv().isDoubletap();
		speedcola = handler.getPlayer().getInv().isSpeedcola();

		if (isReloading) {
			reloadTimer++;
			if (speedcola && reloadTimer >= reloadSpeed / 2) {
				reloadFinish();
				isReloading = false;
				reloadTimer = 0;
			} else if (reloadTimer >= reloadSpeed) {
				reloadFinish();
				isReloading = false;
				reloadTimer = 0;
			}
		} else if (doubletap && timerToFire >= fireRate / 2) {
			readyToFire = true;
			timerToFire = 0;
		} else if (timerToFire >= fireRate) {
			readyToFire = true;
			timerToFire = 0;
		}
		// autoreload when clip is empty
		if (currentClip == 0) {
			reload();
		}
		timerToFire++;
		postTick();
	}

	public void reload() {
		// dont do reload animation when there is no reloading being done
		if ((currentClip != clip) && (currentReserve > 0)) {
			isReloading = true;
		}

	}

	public float getReloadProgress() {
		return (float) reloadTimer / (float) reloadSpeed;
	}

	public boolean getIsReloading() {
		return isReloading;
	}

	public void setReloading(boolean isReloading) {
		this.isReloading = isReloading;
	}

	public int getDamage() {
		return damage;
	}

	public String getName() {
		return name;
	}

	public int getFireRate() {
		return fireRate;
	}

	public int getClip() {
		return clip;
	}

	public int getMaxReserve() {
		return maxReserve;
	}

	public Handler getHandler() {
		return handler;
	}

	public int getCurrentClip() {
		return currentClip;
	}

	public int getCurrentReserve() {
		return currentReserve;
	}

	public float getWeight() {
		return weight;
	}

	public void setCurrentReserve(int currentReserve) {
		this.currentReserve = currentReserve;
	}

	public void setReadyToFire(boolean readyToFire) {
		this.readyToFire = readyToFire;
	}

	public void setReloadTimer(int reloadTimer) {
		this.reloadTimer = reloadTimer;
	}

	public void setClip(int clip) {
		this.clip = clip;
	}

	public void setCurrentClip(int currentClip) {
		this.currentClip = currentClip;
	}

	public boolean isUpgraded() {
		return isUpgraded;
	}

	public void setUpgraded(boolean isUpgraded) {
		this.isUpgraded = isUpgraded;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

}
