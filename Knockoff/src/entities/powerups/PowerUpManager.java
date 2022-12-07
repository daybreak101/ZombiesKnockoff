package entities.powerups;

import java.util.Random;

import main.Handler;

public class PowerUpManager {
	private Handler handler;
	private int powerupsLeft, powerupsSpawned, powerupsRound;
	private boolean maxAmmo, nuke, doublePoints, instakill, infiniteAmmo, deathMachine;
	
	private boolean doublePointsActive = false;
	private boolean instakillActive = false;
	private boolean infiniteAmmoActive = false;
	private boolean deathMachineActive = false;

	public PowerUpManager(Handler handler) {
		this.handler = handler;
		powerupsLeft = 0;
		maxAmmo = false;
		nuke = false;
		doublePoints = false;
		instakill = false;
		deathMachine = false;
	}
	
	public void resetManager() {
		int currentRound = handler.getRoundLogic().getCurrentRound();
		
		if(currentRound < 8) {
			powerupsLeft = 1;
		}
		else if(currentRound < 14) {
			powerupsLeft = 2;
		}
		else if(currentRound < 21) {
			powerupsLeft = 3;
		}
		else {
			powerupsLeft = 4;
		}
		
		powerupsSpawned = 1;
		powerupsRound = powerupsLeft;
		
		
	}
	
	public boolean isPowerUpReady() {
		Random rand = new Random();
		int rng = rand.nextInt(0, 200);
		
		if(powerupsLeft == 0) {
			return false;
		}
		
		int zombiesLeft = handler.getRoundLogic().getZombiesLeft();
		int zpr = handler.getRoundLogic().getZpr();
		int checkpoint = (zpr/powerupsRound * powerupsSpawned) - 10;
		
		if(checkpoint >= (zpr - zombiesLeft) && rng < 20) {
			powerupsLeft--;
			return true;
		}
		if(checkpoint < (zpr - zombiesLeft)) {
			powerupsSpawned++;
		}
		
		
		return false;
	}
	
	public PowerUps generatePowerUp(float x, float y) {
		Random rand = new Random();
		int rng = rand.nextInt(7);
		if(maxAmmo && nuke && doublePoints && instakill && infiniteAmmo) {
			maxAmmo = false;
			nuke = false;
			doublePoints = false;
			instakill = false;
			infiniteAmmo = false;
			deathMachine = false;
		}
		switch(rng) {
		case 0:
			if(maxAmmo) {
				return generatePowerUp(x, y);
			}
			else {
				maxAmmo = true;
				powerupsSpawned++;
				return new MaxAmmo(handler, x, y);
			}
		case 1:
			if(nuke) {
				return generatePowerUp(x, y);
			}
			else {
				nuke = true;
				powerupsSpawned++;
				return new Nuke(handler, x, y);
			}
		case 2:
			if(doublePoints) {	
				return generatePowerUp(x, y);
			}
			else {
				doublePoints = true;
				powerupsSpawned++;
				return new DoublePoints(handler, x, y);
			}
		case 3:
			if(instakill) {
				return generatePowerUp(x, y);
			}
			else {
				instakill = true;
				powerupsSpawned++;
				return new InstaKill(handler, x, y);
			}
		case 4:
			if(infiniteAmmo) {
				return generatePowerUp(x, y);
			}
			else {
				infiniteAmmo = true;
				powerupsSpawned++;
				return new InfiniteAmmo(handler, x, y);
			}
		case 5:
			if(deathMachine) {
				return generatePowerUp(x, y);
			}
			else {
				deathMachine = true;
				powerupsSpawned++;
				return new DeathMachine(handler, x, y);
			}
		case 6:
			return new HealthUp(handler, x, y);
		}
		return null;
	}

	public boolean isDoublePointsActive() {
		return doublePointsActive;
	}

	public void setDoublePointsActive(boolean doublePointsActive) {
		this.doublePointsActive = doublePointsActive;
	}

	public boolean isInstakillActive() {
		return instakillActive;
	}

	public void setInstakillActive(boolean instakillActive) {
		this.instakillActive = instakillActive;
	}

	public boolean isInfiniteAmmoActive() {
		return infiniteAmmoActive;
	}

	public void setInfiniteAmmoActive(boolean infinteAmmoActive) {
		this.infiniteAmmoActive = infinteAmmoActive;
	}

	public boolean isDeathMachineActive() {
		return deathMachineActive;
	}

	public void setDeathMachineActive(boolean deathMachineActive) {
		this.deathMachineActive = deathMachineActive;
	}
	
}
