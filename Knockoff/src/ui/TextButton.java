package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import main.Handler;

public class TextButton extends UIObject{
	
	private String text;
	private ClickListener clicker;
	protected boolean isSelected;
	protected int fontSize;
	
	public TextButton(Handler handler, float x, float y, int width, int height, String text, ClickListener clicker) {
		super(handler, x, y, width, height);
		this.text = text;
		this.fontSize = 20;
		this.clicker = clicker;
	}
	
	public TextButton(Handler handler, float x, float y, int width, int height, String text, int fontSize, ClickListener clicker) {
		super(handler, x, y, width, height);
		this.text = text;
		this.fontSize = fontSize;
		this.clicker = clicker;
	}

	int r;
	int gr;
	int b;
	int rMin, gMin, bMin;
	boolean increase = true;
	@Override
	public void tick() {
		Color setColor = handler.getSettings().getHudColor();
		rMin = setColor.getRed();
		gMin = setColor.getGreen();
		bMin = setColor.getBlue();
		int incrementBy = 4;
		if(hovering) {
			if(increase) {
				if(r >= 255)
					r = 255;
				else
					r+=incrementBy;
				if(gr >= 255)
					gr = 255;
				else
					gr+=incrementBy;
				if(b >= 255)
					b = 255;
				else
					b+=incrementBy;
				
				if(r >=255 && gr >= 255 && b >= 255)
					increase = false;
			}
			else {
				if(r <= 0)
					r = 0;
				else if(r <= rMin)
					r = rMin;
				else
					r-=incrementBy;
				if(gr <= 0)
					gr = 0;
				else if(gr <= gMin)
					gr = gMin;
				else
					gr-=incrementBy;
				if(b <= 0)
					b = 0;
				else if(b <= bMin)
					b = bMin;
				else
					b-=incrementBy;
				
				if(r <= rMin && gr <= gMin && b <= bMin)
					increase = true;
				else if(r <= 0 && gr <= 0 && b <= 0)
					increase = true;
			}

		}
		else {
			r = rMin;
			gr = gMin;
			b = bMin;
		}

	}

	@Override
	public void render(Graphics g) {
		if(r<=0)
			r = 0;
		if(r>=255)
			r = 255;
		if(gr<=0)
			gr = 0;
		if(gr>=255)
			gr = 255;
		if(b<=0)
			b = 0;
		if(b>=255)
			b = 255;
		if(isSelected) {	
			g.setColor(Color.black);
			g.fillRect((int) x, (int) y, width, height);
			
			Graphics2D g2 = (Graphics2D) g;
			float thickness = 2;
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(thickness));
			g2.setColor(handler.getSettings().getHudColor());
			g2.drawRect((int) x, (int) y, width, height);
			g2.setStroke(oldStroke);
		}
		if (hovering) {
			g.setColor(new Color(r, gr, b));

		}
		else {
			g.setColor(handler.getSettings().getHudColor());
		}
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize));
		g.drawString(text, (int) x + 10, (int) y + height/2 + 10);
	}

	@Override
	public void onClick() {
		clicker.onClick();
		isSelected = true;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
