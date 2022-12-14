package hud;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import entities.creatures.Player;
import entities.powerups.PowerUps;
import entities.statics.InteractableStaticEntity;
import graphics.Assets;
import main.Handler;
import perks.Perk;
import utils.Utils;
import weapons.Gun;

public class GameplayElement extends HudElement {

	private int grenades, points, health, round, currentStamina, maxStamina, zombiesLeft, armor;
	private String gunText = "", interactText = "";
	private Perk[] perks;
	private Gun gun;// = handler.getWorld().getEntityManager().getPlayer().getInv().getGun();
	private Color hudColor;

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
		hudColor = handler.getSettings().getHudColor();

		grenades = player.getInv().getGrenades();
		health = player.getHealth();

		interactText = "";

		Ellipse2D.Float radius = new Ellipse2D.Float(handler.getPlayer().getX() - 100, handler.getPlayer().getY() - 100,
				200, 200);
		InteractableStaticEntity closestInteract = null;
		float closestDist = 2000000;
		float eDist;
		for (InteractableStaticEntity e : handler.getWorld().getEntityManager().getInteractables()) {
			eDist = Utils.getEuclideanDistance(player.getX(), player.getY(), e.getX(), e.getY());
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
				interactText = closestInteract.getTriggerText();

			}
		}

		perks = player.getInv().getPerks();
		points = player.getInv().getPoints();
		currentStamina = player.getCurrentStamina();
		maxStamina = player.getMaxStamina();
		round = handler.getRoundLogic().getCurrentRound();
		zombiesLeft = handler.getRoundLogic().getZombiesLeft();
		armor = handler.getPlayer().getArmor();

		gun = player.getInv().getGun();
		if (gun == null)
			gunText = "";
		else
			gunText = gun.getName() + "         " + gun.getCurrentClip() + " / " + gun.getCurrentReserve();

	}

	public void renderGrenades(Graphics g) {
		// render grenades
		g.setColor(hudColor);

		for (int i = 0; i < 4; i++) {
			g.drawOval((int) (handler.getWidth() - 300 + i * 25), (int) (handler.getHeight() - 80), 20, 20);
		}
		for (int i = 0; i < grenades; i++) {
			g.fillOval((int) (handler.getWidth() - 300 + i * 25), (int) (handler.getHeight() - 80), 20, 20);
		}
	}

	public void renderGun(Graphics g) {
		// render gun
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		g.drawString(gunText, (int) handler.getWidth() - 300, (int) handler.getHeight() - 100);

		if (gun == null) {

		} else if (gun.getIsReloading()) {
			g.setColor(Color.black);
			g.fillRect((int) handler.getWidth() - 300, (int) handler.getHeight() - 100, 100, 10);
			g.setColor(hudColor);
			g.fillRect((int) handler.getWidth() - 300, (int) handler.getHeight() - 100,
					(int) (gun.getReloadProgress() * 100), 10);
		}
	}

	public void renderHealthAndArmor(Graphics g) {
		// render health

		g.setColor(new Color(128, 0, 0));
		if (handler.getPlayer().getInv().isJugg()) {
			g.fillRect((int) 100, (int) handler.getHeight() - 100, 150, 50);

		} else {
			g.fillRect((int) 100, (int) handler.getHeight() - 100, 100, 50);
		}
		g.setColor(hudColor);
		g.fillRect((int) 100, (int) handler.getHeight() - 100, health, 50);

		g.setColor(Color.BLUE);
		g.fillRect((int) 100, (int) handler.getHeight() - 60, armor, 10);

		g.setColor(new Color(128, 0, 0));
		g.fillOval(49, (int) handler.getHeight() - 105, 60, 60);
		g.setColor(Color.black);

		Graphics2D g2 = (Graphics2D) g;
		float thickness = 5;
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(thickness));
	
		g.drawOval(49, (int) handler.getHeight() - 105, 60, 60);
		g2.setStroke(oldStroke);

		g.setFont(new Font(Font.DIALOG, Font.BOLD, 100));
		g.setColor(Color.white);
		g.drawString("+", 50, (int) handler.getHeight() - 40);

	}

	public void renderInteractionText(Graphics g) {
		// render interact
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		g.setColor(hudColor);
		g.drawString(interactText, (int) handler.getWidth() / 2 - 190, (int) handler.getHeight() / 2 + 200);

	}

	public void renderPerks(Graphics g) {
		// render perks
		if (perks != null)
			for (int i = 0; i < perks.length; i++)
				if (perks[i] != null)
					g.drawImage(perks[i].getIcon(), (int) (80 + (i * 60)), (int) handler.getHeight() - 200, 50, 50,
							null);
	}

	public void renderPoints(Graphics g) {
		// render points
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
		g.drawString(Integer.toString(points), (int) 40, (int) handler.getHeight() / 2 + 150);
	}

	ArrayList<PowerUps> powerups = new ArrayList<PowerUps>(4);
	int oneX = handler.getWidth() / 2 - 25, twoX = handler.getWidth() / 2 - 25, threeX = handler.getWidth() / 2 - 25,
			fourX = handler.getWidth() / 2 - 25;

	public void renderPowerups(Graphics g) {

		// make sure to remove those that are inactive
		for (PowerUps e : handler.getWorld().getEntityManager().getPowerups()) {
			if (e.isPickedUp() && e.getIcon() != null && !powerups.contains(e)) {
				powerups.add(e);
			}
		}
		for (PowerUps e : powerups) {
			if (!handler.getWorld().getEntityManager().getPowerups().contains(e)) {
				powerups.remove(e);
				break;
			}
		}

		int powerupY = (int) handler.getHeight() - 100;
		int square = 50;

		// change powerups .size() to an array that only contains powerups picked up
		if (powerups.size() == 1) {
			if (oneX < handler.getWidth() / 2 - 25) {
				oneX++;
			}
		} else if (powerups.size() == 2) {
			if (oneX > handler.getWidth() / 2 - 55) {
				oneX--;
			} else if (oneX < handler.getWidth() / 2 - 55) {
				oneX++;
			}

			if (twoX > handler.getWidth() / 2 + 5) {
				twoX--;
			} else if (twoX < handler.getWidth() / 2 + 5) {
				twoX++;
			}
		} else if (powerups.size() == 3) {
			if (oneX > handler.getWidth() / 2 - 85) {
				oneX--;
			} else if (oneX < handler.getWidth() / 2 - 85) {
				oneX++;
			}
			if (twoX > handler.getWidth() / 2 - 25) {
				twoX--;
			} else if (twoX < handler.getWidth() / 2 - 25) {
				twoX++;
			}
			if (threeX > handler.getWidth() / 2 + 35) {
				threeX--;
			} else if (threeX < handler.getWidth() / 2 + 35) {
				threeX++;
			}
		} else if (powerups.size() == 4) {
			if (oneX > handler.getWidth() / 2 - 115) {
				oneX--;
			} else if (oneX < handler.getWidth() / 2 - 115) {
				oneX++;
			}
			if (twoX > handler.getWidth() / 2 - 55) {
				twoX--;
			} else if (twoX < handler.getWidth() / 2 - 55) {
				twoX++;
			}
			if (threeX > handler.getWidth() / 2 + 5) {
				threeX--;
			} else if (threeX < handler.getWidth() / 2 + 5) {
				threeX++;
			}
			if (fourX > handler.getWidth() / 2 + 65) {
				fourX--;
			} else if (fourX < handler.getWidth() / 2 + 65) {
				fourX++;
			}
		}

		int[] xs = { oneX, twoX, threeX, fourX };
		int j = 0;
		for (int i = 0; i < powerups.size(); i++) {
			if (powerups.get(i).isPickedUp() && powerups.get(i).getIcon() != null) {
				g.drawImage(powerups.get(i).getIcon(), xs[j], powerupY, square, square, null);
				j++;
			}
		}
	}

	public void renderRound(Graphics g) {
		// render round
		g.drawImage(Assets.zombieBlood, (int) 20, (int) 25, 100, 100, null);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 60));
		g.drawString(Integer.toString(round), (int) 50, (int) 100);
	}

	public void renderStamina(Graphics g) {
		// render stamina
		g.setColor(Color.BLACK);
		g.fillRect((int) 100, (int) handler.getHeight() - 120, 100, 10);

		g.setColor(hudColor);
		if (maxStamina != 0)
			g.fillRect((int) 100, (int) handler.getHeight() - 120, (int) ((currentStamina * 100 / maxStamina)), 10);
	}

	public void renderZombiesLeft(Graphics g) {
		// render zombiesleft
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		g.drawString("Zombies Left: " + Integer.toString(zombiesLeft), (int) handler.getWidth() - 200, (int) 300);
	}

	@Override
	public void render(Graphics g) {
		renderGrenades(g);
		renderGun(g);
		renderHealthAndArmor(g);
		renderInteractionText(g);
		renderPerks(g);
		renderPoints(g);
		renderPowerups(g);
		renderRound(g);
		renderStamina(g);
		if (handler.getSettings().isZombieCounter())
			renderZombiesLeft(g);

		Point2D center = new Point2D.Float(500, 500);
		float radius = 250;
		float[] dist = { 0.0f, 0.2f, 1.0f };
		Color[] colors = { Color.RED, Color.WHITE, Color.BLUE };
		RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		// old.
		// g2d.(p);
		g2d.setTransform(old);

	}

}
