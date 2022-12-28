package hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import main.Handler;
import utils.Timer;

public class DamageElement extends HudElement{

	private int alpha, fontSize;
	private Timer fontTick;
	private int r, gr, b;
	private int damage;
	
	public DamageElement(Handler handler, float x, float y, int damage) {
		super(x, y, 25, 25, handler);
		alpha = 255;
		fontSize = 20;
		fontTick = new Timer(5);
		this.damage = damage;
	}

	@Override
	public void tick() {
		Color setColor = handler.getSettings().getHudColor();
		r = setColor.getRed();
		gr = setColor.getGreen();
		b = setColor.getBlue();
		y -= 1;
		alpha -= 4;

		fontTick.tick();
		if (fontTick.isReady()) {
			fontTick.resetTimer();
			fontSize--;
		}

		if (alpha <= 0 || fontSize <= 0)
			isActive = false;
	}

	@Override
	public void render(Graphics g) {
		//g.setColor(new Color(r, gr, b, alpha));
		
		double zoomLevel = handler.getSettings().getZoomLevel();
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		
		old.scale(zoomLevel, zoomLevel);
		g2d.setTransform(old);
		
		g.setColor(Color.white);
		g.setFont(new Font(Font.DIALOG, Font.BOLD, fontSize));
		g.drawString(Integer.toString(damage), (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()));
	
		old.scale(1/zoomLevel, 1/zoomLevel);
		g2d.setTransform(old);
		
		
	}

}
