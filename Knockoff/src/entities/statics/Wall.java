package entities.statics;

import java.awt.Color;
import java.awt.Graphics;

import main.Handler;

public class Wall extends InteractableStaticEntity {
	
	int whatWall;

	// 0 = horizontal up
	// 1 = horizontal down
	// 2 = vertical right
	// 3 = vertical left
	public Wall(Handler handler, float x, float y, int length, int whatWall) {
		super(handler, x, y, 100, 100);
		this.whatWall = whatWall;
		if (whatWall == 0) {
			bounds.x = 0;
			bounds.y = 75;
			bounds.width = length;
			bounds.height = 25;
		} 
		else if (whatWall == 1) {
			bounds.x = 0;
			bounds.y = 0;
			bounds.width = length;
			bounds.height = 25;
		} 
		else if (whatWall == 2) {
			bounds.x = 75;
			bounds.y = 0;
			bounds.width = 25;
			bounds.height = length;
		}
		else if (whatWall == 3) {
			bounds.x = 0;
			bounds.y = 0;
			bounds.width = 25;
			bounds.height = length;
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(30, 50, 40));
			g.fillRect((int) (x + bounds.x - handler.getGameCamera().getxOffset()),
				(int) (y + bounds.y - handler.getGameCamera().getyOffset()), bounds.width, bounds.height);
		
	}

}
