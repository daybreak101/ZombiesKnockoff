package entities.bullets;

import java.awt.Graphics;
import java.util.ArrayList;

import main.Handler;
import utils.Timer;

public class IceBullet extends Bullet{
	ArrayList<Bullet> pellets;
	Handler handler;

	public IceBullet(Handler handler, float x, float y, int range) {
		super(handler, x, y, range);
	
		pellets = new ArrayList<Bullet>(9);
		pellets.add(new IcePellet(handler, x, y, range, 0));
		pellets.add(new IcePellet(handler, x, y, range, (float) -Math.PI/4));
		pellets.add(new IcePellet(handler, x, y, range, (float) Math.PI/4));
		pellets.add(new IcePellet(handler, x, y, range, (float) -Math.PI/8));
		pellets.add(new IcePellet(handler, x, y, range, (float) Math.PI/8));
		pellets.add(new IcePellet(handler, x, y, range, (float) Math.PI/16));
		pellets.add(new IcePellet(handler, x, y, range, (float) -Math.PI/16));
		pellets.add(new IcePellet(handler, x, y, range, (float) Math.PI/32));
		pellets.add(new IcePellet(handler, x, y, range, (float) -Math.PI/32));
		
	}
	
	Timer heldShot = new Timer(180);
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
