package hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import entities.creatures.Player;
import graphics.Assets;
import main.Handler;
import perks.Perk;
import weapons.Gun;

public class GameplayElement extends HudElement {
		
	private int grenades, points, health, round, currentStamina, maxStamina, zombiesLeft, armor;
	private String gunText = "", interactText = "";
	private Perk[] perks;
	private Gun gun;// = handler.getWorld().getEntityManager().getPlayer().getInv().getGun();	
	
	public GameplayElement(Handler handler) {
		super(0, 0, 0, 0, handler);
		maxStamina = 1;
		grenades = 0;
		round = 0;
		zombiesLeft = 0;
	}

	@Override
	public void tick() {
		Player player = handler.getWorld().getEntityManager().getPlayer();
		
		grenades = player.getInv().getGrenades();
		health = player.getHealth();
		interactText = player.interactableText();
		perks = player.getInv().getPerks();
		points = player.getInv().getPoints();
		currentStamina = player.getCurrentStamina();
		maxStamina = player.getMaxStamina();
		round = handler.getRoundLogic().getCurrentRound();
		zombiesLeft = handler.getRoundLogic().getZombiesLeft();
		armor = handler.getPlayer().getArmor();
		
		gun = player.getInv().getGun();
		if(gun == null) 
			gunText = "";
		else
			gunText = gun.getName() + "         " + gun.getCurrentClip() + " / " + gun.getCurrentReserve();
		

	}

	@Override
	public void render(Graphics g) {
		
		//render grenades
		g.setColor(Color.green);

		for(int i = 0; i < 4; i++) {
			g.drawOval((int) (handler.getWidth() - 300 + i*25), (int) (handler.getHeight() - 80), 20, 20);
		}
		for(int i = 0; i < grenades; i++) {
			g.fillOval((int) (handler.getWidth() - 300 + i*25), (int) (handler.getHeight() - 80), 20, 20);
		}
		
		//render gun
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20)); 
		g.drawString(gunText, (int) handler.getWidth() - 300, (int) handler.getHeight() - 100);
		
		if(gun == null) {
			
		}
		else if(gun.getIsReloading()) 
		{
			g.setColor(Color.black);
			g.fillRect((int) handler.getWidth() - 300, (int) handler.getHeight() - 100, 100, 10);
			g.setColor(Color.green);
			g.fillRect((int) handler.getWidth() - 300, (int) handler.getHeight() - 100,(int) (gun.getReloadProgress() * 100), 10);
		}
		
		//render health
		g.setColor(Color.RED);
		g.fillRect((int) 100, (int) handler.getHeight() - 100, 100, 50);
		
		g.setColor(Color.GREEN);
		g.fillRect((int) 100, (int) handler.getHeight() - 100, health, 50);
		
		g.setColor(Color.BLUE);
		g.fillRect((int) 100, (int) handler.getHeight() - 60, armor, 10);
		
		//render interact
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		g.setColor(Color.green);
		g.drawString(interactText, (int) handler.getWidth() / 2 - 190, (int) handler.getHeight() / 2 + 200);
		
		//render perks
		if (perks != null) 
			for (int i = 0; i < perks.length; i++) 
				if (perks[i] != null)
					g.drawImage(perks[i].getIcon(), (int) (80 + (i * 60)), (int) handler.getHeight() - 200, 50, 50, null);
		
		//render points
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30)); 
		g.drawString(Integer.toString(points), (int) 40, (int) handler.getHeight()/2 + 150);
		
		//render powerups
		if(handler.getRoundLogic().getPowerups().isDoublePointsActive()) {
			g.drawString("x2", (int) handler.getWidth()/2 - 20, (int) handler.getHeight() - 100);
		}
		if(handler.getRoundLogic().getPowerups().isInstakillActive()) {
			g.fillOval((int) handler.getWidth()/2 - 20 + 10, (int) handler.getHeight() - 100, 20, 20);
		}
		if(handler.getRoundLogic().getPowerups().isInfiniteAmmoActive()) {
			g.drawString("8", (int) handler.getWidth()/2 - 20 + 20, (int) handler.getHeight() - 100);
		}
		
		//render round
		g.drawImage(Assets.zombieBlood, (int) 20, (int) 25, 100, 100, null);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 60)); 
		g.drawString(Integer.toString(round), (int) 50, (int) 100);
		
		//render stamina
		g.setColor(Color.BLACK);
		g.fillRect((int) 100, (int) handler.getHeight() - 120, 100, 10);
		
		g.setColor(Color.green);
		if(maxStamina != 0)
			g.fillRect((int) 100, (int) handler.getHeight() - 120, (int) ((currentStamina * 100/maxStamina)), 10);
		
		//render zombiesleft
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20)); 
		g.drawString("Zombies Left: " + Integer.toString(zombiesLeft), (int) handler.getWidth() - 300, (int) 250);
		
		
	    Point2D center = new Point2D.Float(500, 500);
	     float radius = 250;
	     float[] dist = {0.0f, 0.2f, 1.0f};
	     Color[] colors = {Color.RED, Color.WHITE, Color.BLUE};
	     RadialGradientPaint p =
	         new RadialGradientPaint(center, radius, dist, colors);
		Graphics2D g2d = (Graphics2D) g;
			AffineTransform old = g2d.getTransform();
		//	old.
	     //g2d.(p);
	     g2d.setTransform(old);

	}

}
