package entities.bullets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import entities.creatures.Player;
import entities.statics.InteractableStaticEntity;
import main.Handler;

public class ToxenBullet extends Bullet {

	public ToxenBullet(Handler handler, float x, float y, int range) {
		super(handler, x, y, range);
		this.speed = 10;
		
		xMove = 0;
		yMove = 0;

		float moveToX = handler.getPlayer().getX() - x;
		float moveToY = handler.getPlayer().getY() - y;

		float angle = (float) Math.atan2(moveToY, moveToX);
		xMove = (float) (speed * Math.cos(angle));
		yMove = (float) (speed * Math.sin(angle));
	}
	
	int color = 0;
	@Override
	public void render(Graphics g) {
		if(color < 10)
			g.setColor(Color.yellow);
		else if(color < 20)
			g.setColor(new Color(144, 238, 144));	
		else if(color < 30)
			g.setColor(new Color(144, 255, 200));
		else if(color < 40)
			g.setColor(Color.green);
		
		g.fillRect((int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height);
		color += 1;
		if(color >= 40) {
			color = 0;
		}
	}
	
	public boolean checkForImpact() {
		cb = new Rectangle((int)(x + bounds.x - 1), (int)(y + bounds.y - 1), bounds.width + 1, bounds.height + 1);
		Player player = handler.getWorld().getEntityManager().getPlayer();
		
		if(player.getCollisionBounds(0, 0).intersects(cb)) {
			player.takeDamage(15);
			handler.getWorld().getEntityManager().getEntities().remove(this);
			return true;
		}
		
		for(InteractableStaticEntity e: handler.getWorld().getEntityManager().getInteractables()){
			if(e.getCollisionBounds(0, 0).intersects(cb)) {
				handler.getWorld().getEntityManager().getEntities().remove(this);
				return true;
			}
		}
		return false;
	}

}
