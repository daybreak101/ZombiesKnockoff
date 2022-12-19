package entities.statics;

import java.awt.Color;
import java.awt.Graphics;

import main.Handler;
import weapons.AA12;
import weapons.AK47;
import weapons.AWP;
import weapons.Flamethrower;
import weapons.Glock17;
import weapons.GrenadeLauncher;
import weapons.Gun;
import weapons.M4;
import weapons.P90;
import weapons.RPD;
import weapons.RPG;
import weapons.Winchester1876;

public class PackAPunch extends InteractableStaticEntity {

	private boolean isUpgrading;
	private Gun gunPacked;
	private int packCounter, packTimer;
	private boolean cantAfford = false;
	private boolean cantUpgrade = false;

	public PackAPunch(Handler handler, float x, float y) {
		super(handler, x, y, 150, 75);
		triggerText = "Press F to Upgrade Weapon: 5000";
		packTimer = 1000;
		isUpgrading = false;
	}

	@Override
	public void fulfillInteraction() {
		if (!isUpgrading && cooldownTimer >= cooldown && !handler.getPlayer().getInv().getGun().isUpgraded()) {
			// can afford
			if (handler.getPlayer().purchase(5000)) {
				isUpgrading = true;
				cantAfford = false;
				cantUpgrade = false;
				cooldownTimer = 0;

				switch (handler.getPlayer().getInv().getGun().getName()) {
				case "AA12":
					gunPacked = new AA12(handler);
					break;
				case "AK-47":
					gunPacked = new AK47(handler);
					break;
				case "AWP":
					gunPacked = new AWP(handler);
					break;
				case "Flamethrower":
					gunPacked = new Flamethrower(handler);
					break;
				case "Glock17":
					gunPacked = new Glock17(handler);
					break;
				case "Grenade Launcher":
					gunPacked = new GrenadeLauncher(handler);
					break;
				case "M4":
					gunPacked = new M4(handler);
					break;
				case "P90":
					gunPacked = new P90(handler);
					break;
				case "RPD":
					gunPacked = new RPD(handler);
					break;
				case "RPG":
					gunPacked = new RPG(handler);
					break;
				case "Winchester 1876":
					gunPacked = new Winchester1876(handler);
					break;
				default:
					System.out.println("oof");
					break;

				}
				if (gunPacked == null) {
					isUpgrading = false;
					cantUpgrade = true;
					handler.getPlayer().gainPoints(5000);
				} else {
					handler.getPlayer().removeGunForUpgrade();
					handler.getGlobalStats().addGunUpgrade();
					gunPacked.upgradeWeapon();
				}
			}
			// can't afford

			else {

				cantAfford = true;
				cooldownTimer = 0;
			}

		} else if (!isUpgrading && cooldownTimer >= cooldown && handler.getPlayer().getInv().getGun().isUpgraded()) {
			cantUpgrade = true;

		}
		// grab weapon
		else if (isUpgrading && cooldownTimer >= cooldown && packCounter < packTimer) {
			cooldownTimer = 0;
			isUpgrading = false;
			packCounter = 0;
			handler.getPlayer().setGun(gunPacked);
		}

	}

	@Override
	public void postTick() {
		if (cantAfford && cooldownTimer < cooldown) {
			packCounter = 0;
			triggerText = "              Not enough points!";
		} else if (cantUpgrade && cooldownTimer < cooldown) {
			triggerText = "     	 Weapon is not upgradable!";
		} else if (isUpgrading && cooldownTimer >= cooldown) {
			triggerText = "Press F to pick up " + gunPacked.getName();
			packCounter++;
			if (packCounter >= packTimer) {
				isUpgrading = false;
			}
		} else if (!isUpgrading && cooldownTimer >= cooldown) {
			packCounter = 0;
			triggerText = "Press F to upgrade weapon: 5000";
			gunPacked = null;
		} else if (isUpgrading) {
			triggerText = "            Upgrading...";
		} else {
			triggerText = "";
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()),
				width, height);
		g.setColor(Color.magenta);
		g.fillRect((int) (x + 10 - handler.getGameCamera().getxOffset()),
				(int) (y + 10 - handler.getGameCamera().getyOffset()), width - 40, height - 40);

	}

}
