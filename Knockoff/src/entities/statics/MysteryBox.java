package entities.statics;

import java.awt.Graphics;
import java.util.Random;

import graphics.Assets;
import main.Handler;
import weapons.*;
import weapons.Gun;

public class MysteryBox extends InteractableStaticEntity {
	
	private boolean isOpened;
	private Gun gun;
	private int isOpenedTimer, isOpenedTime;
	private boolean cantAfford = false;

	public MysteryBox(Handler handler, float x, float y) {
		super(handler, x, y, 150, 75);
		triggerText = "Press F to spin for a random weapon";
		isOpened = false;
		isOpenedTime = 1000;
	}

	//spin for random weapon
	@Override
	public void fulfillInteraction() {
		//spin for weapon
		if(isOpened == false && cooldownTimer >= cooldown) {
			
			if(handler.getPlayer().purchase(950)) {
				isOpened = true;
				cantAfford = false;
				cooldownTimer = 0;
				gun = getRandomWeapon();
				handler.getGlobalStats().addBoxSpin();
				
				//don't give a weapon player already has
				while(handler.getPlayer().checkArsenal(gun)) {
					gun = getRandomWeapon();
				}
			}
			else {
				cantAfford = true;
				cooldownTimer = 0;
			}
			
		}
		//grab weapon
		else if(isOpened == true && cooldownTimer >= cooldown && isOpenedTimer < isOpenedTime) {
			cooldownTimer = 0;
			isOpened = false;
			isOpenedTimer = 0;
			handler.getPlayer().setGun(gun);
			handler.getGlobalStats().addBoxPull();
		}
		
	}
	
	public Gun getRandomWeapon() {
		Random rand = new Random();
		int rng = rand.nextInt(11);
		
		switch(rng) {
		case 1:
			return new AK47(handler);
		case 2:
			return new P90(handler);
		case 3:
			return new M4(handler);
		case 4:
			return new RPD(handler);
		case 5:
			return new RPG(handler);
		case 6:
			return new Winchester1876(handler);
		case 7:
			return new AWP(handler);
		case 8:
			return new AA12(handler);
		case 9:
			return new Flamethrower(handler);
		case 10: 
			return new GrenadeLauncher(handler);
		default:
			return new Glock17(handler);
		}
	}
	
	@Override
	public void postTick() {
		if(cantAfford == true && cooldownTimer < cooldown) {
			isOpenedTimer = 0;
			triggerText = "              Not enough points!";
		}	
		else if(isOpened == true && cooldownTimer >= cooldown) {
			triggerText = "Press F to trade weapon for " + gun.getName();
			isOpenedTimer++;
			if(isOpenedTimer >= isOpenedTime) {
				isOpened = false;
			}
		}
		else if(isOpened == false && cooldownTimer >= cooldown) {
			isOpenedTimer = 0;
			triggerText = "Press F to spin for a random weapon: 950";
			gun = null;
		}
		else if(isOpened == true) {
			triggerText = "            Spinning...";
		}
		else {
			triggerText = "";
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Assets.mysteryBox, (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null );
		
	}

}
