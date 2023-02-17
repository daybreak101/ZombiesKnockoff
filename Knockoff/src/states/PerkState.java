package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Handler;
import ui.ClickListener;
import ui.TextButton;
import ui.UIManager;

public class PerkState extends State{
	
	private UIManager uiManager;
	private BufferedImage chipBag;
	private String perkName, jokeDesc, basedesc, lvl1desc, lvl2desc, lvl3desc;

	public PerkState(Handler handler, String perkName, BufferedImage chipBag, String jokeDesc, String basedesc, String lvl1desc, String lvl2desc, String lvl3desc) {
		super(handler);
		this.perkName = perkName;
		this.chipBag = chipBag;
		this.jokeDesc = jokeDesc;
		this.basedesc = basedesc;
		this.lvl1desc = lvl1desc;
		this.lvl2desc = lvl2desc;
		this.lvl3desc = lvl3desc;
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);

		uiManager.addObject(new TextButton(handler, 100, 500, 100, 50, "Upgrade", new ClickListener() {

			@Override
			public void onClick() {

			}

		}));
		
		uiManager.addObject(new TextButton(handler, 100, 550, 100, 50, "Upgrade", new ClickListener() {

			@Override
			public void onClick() {

			}

		}));
		
		uiManager.addObject(new TextButton(handler, 100, 600, 100, 50, "Upgrade", new ClickListener() {

			@Override
			public void onClick() {

			}

		}));
		
		uiManager.addObject(new TextButton(handler, 500, 700, 100, 50, "Back", new ClickListener() {

			@Override
			public void onClick() {
				handler.getSettings().writeToFile();
				handler.getMouseManager().setUIManager(null);
					State.setState(new UpgradesState(handler));

			}

		}));
	}

	@Override
	public void tick() {
		uiManager.tick();		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		uiManager.render(g);
		g.setColor(handler.getSettings().getHudColor());
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 40));
		g.drawString(perkName, 500, 200);
		
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		
		
		g.drawImage(chipBag, 150, 150, 200, 200, null);
		
		//g.drawString(jokeDesc, 500, 250);
		String row = "";
		int j = 0;
		int i = 0;
		while(i < jokeDesc.length()) {
			row = "";
			row += jokeDesc.charAt(i);
			i++;
			while(i % 40 != 0 && i < jokeDesc.length()) {
				row += jokeDesc.charAt(i);
				i++;
			}
			//if(jokeDesc.charAt(i) != ' ') {
				while(i < jokeDesc.length() - 1) {
					//System.out.print(jokeDesc.charAt(i));
					if(jokeDesc.charAt(i) != ' ') {
						row += jokeDesc.charAt(i);
						i++;
					}
					else {
						i++;
						break;
					}
					
				}
			//}
			
			g.drawString(row, 500, 250 + (j * 25));
			j++;
		} 
	

		int x = 250;
		int y = 480;
		g.drawString("Base: " + basedesc, x, y);
		g.drawString("      1: " + lvl1desc, x, y + 50);
		g.drawString("      2: " + lvl2desc, x, y + 100);
		g.drawString("      3: " + lvl3desc, x, y + 150);
		
		g.setColor(handler.getSettings().getLaserColor());
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);
		
	}

}
