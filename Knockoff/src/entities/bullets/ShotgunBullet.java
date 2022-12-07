package entities.bullets;

import java.awt.Graphics;
import java.util.ArrayList;
import main.Handler;

public class ShotgunBullet extends Bullet{
	
	ArrayList<Bullet> pellets;
	Handler handler;

	public ShotgunBullet(Handler handler, float x, float y, int range) {
		super(handler, x, y, range);
	
		pellets = new ArrayList<Bullet>(5);
		pellets.add(new Bullet(handler, x, y, range));
		pellets.add(new Bullet(handler, x, y, range, (float) Math.PI/16));
		pellets.add(new Bullet(handler, x, y, range, (float) -Math.PI/16));
		pellets.add(new Bullet(handler, x, y, range, (float) Math.PI/32));
		pellets.add(new Bullet(handler, x, y, range, (float) -Math.PI/32));
		
	}
	
	public void tick() {
		for(int i = 0; i < pellets.size(); i++) {
			pellets.get(i).tick();
			if(pellets.get(i).checkForImpact() == true) {
				pellets.remove(i);
			}
		}
		die();
		
	}

	@Override
	public void render(Graphics g) {
		for(int i = 0; i < pellets.size(); i++) {
			pellets.get(i).render(g);
			
		}
		
	}

}
