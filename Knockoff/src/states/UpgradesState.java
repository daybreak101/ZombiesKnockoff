package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import graphics.Assets;
import main.Handler;
import ui.ClickListener;
import ui.TextButton;
import ui.UIManager;

public class UpgradesState extends State {

	private UIManager uiManager;
	
	public UpgradesState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUIManager(uiManager);
		uiManager.addObject(new TextButton(handler, 300, 700, 100, 50, "Back", new ClickListener() {

			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new LobbyState(handler));
			}}));
		
		//perks
		int x = handler.getWidth() - 360;
		int y = 100;
		int i = 0;
		int dy = 40;
		int fontSize = 20;
		int height = 30;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "BeefPump Protein!", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler, 
						"BeefPump Protein!",
						Assets.jugg,
						"These are the chips your trainer tells you not to take, even though he takes them himself... (They're roids)",
						"Increase health by 10 hp",
						"Increase health by 25 hp",
						"Increase health by 50 hp",
						"Increase health by 100 hp"	
						));

			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Speed Cola",fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Speed Cola",
						Assets.fasthand,
						"These addictive chips contain 12 servings of 10000mg of caffeine each. But do not worry! These are FDA approved! (We didn't bribe them)",
						"Reload faster by 10%",
						"Reload faster by 25%",
						"Reload faster by 50%",
						"Reload faster by 70%"
						));
			}}));
		i++;
//		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Cardiac Resurrect", fontSize,new ClickListener() {
//			@Override
//			public void onClick() {
//				handler.getMouseManager().setUIManager(null);
//				State.setState(new PerkState(handler,
//						"Cardiac Resurrect",
//						Assets.revive,
//						"These taste like the ones they give out at church... except saltier.",
//						"Revive yourself",
//						"Go into a downed state where enemies ignore you",
//						"WHile downed, use your most recent pistol",
//						"While downed, destroy everything... temporarily"
//						));
//			}}));
//		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Double STUFF3D", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Double STUFF3D",
						Assets.doubletap,
						"A chip within a chip. These bags run out twice as fast! (Choking hazard)",
						"Slightly increase fire rate",
						"Increase bullet penetration",
						"Increase fire rate",
						"Double damage"
						));
			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Keto Kardio", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Keto Kardio",
						Assets.stam,
						"Best weight-loss solution on the planet. Helps cardiovascular health. Just ignore the sodium content... (We had to make it taste good somehow)",
						"Increase sprint speed and walk speed",
						"Decrease sprint cooldown",
						"Increase stamina regeneration",
						"Greatly increase sprint duration"
						));
			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Bomb-B-Q's", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Bomb-B-Q's",
						Assets.phd,
						"An explosion of barbeque flavor in your mouth! Will hurt at first but you'll adapt to its explosiveness. (Must have a strong jaw before eating)",
						"Increase explosive damage resistance by 50%",
						"Equip cluster grenades",
						"Immune to all explosive damage",
						"Increase damage of explosives"
						));
			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Sharpshooter Chili", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Sharpshooter Chili",
						Assets.deadshot,
						"A flavor that never misses. Unlike my ex-wife... (I miss her)",
						"Increased range",
						"Headshots give more points",
						"Higher chance of headshots",
						"Increased headshot damage"
						));
			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Party Size Classic", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Party Size Classic",
						Assets.mule,
						"Contains ONE extra chip per bag. Perfect for large gatherings.",
						"Carry a third weapon",
						"Chance to regain grenade after throwing one",
						"Chance to regain special grenade after throwing one",
						"Retrieve your lost weapon after buying perk again"
						));
			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Fortified Kettle", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Fortified Kettle",
						Assets.stronghold,
						"Stand your ground. This is your favorite flavor. You'll feel more confident defending your bad opinions. (Look how confident you are!)",
						"Gain armor when standing still and remaining within the circle",
						"Gain damage as well",
						"Max armor and damage is increased",
						"Gain armor and damage for every zombie killed within the circle"
						));
			}}));
		i++;
//		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Bandolier", fontSize, new ClickListener() {
//			@Override
//			public void onClick() {
//				handler.getMouseManager().setUIManager(null);
//				State.setState(new PerkState(handler,
//						"Bandolier",
//						Assets.bandolier,
//						"Only way to eat these, is if you stole another one. Life is better with more at your disposal.",
//						"Gain one more clip of reserve ammo",
//						"Ammo restock price is reduced",
//						"Gain a extra clip of reserve ammo",
//						"Reload by bullet when wheapon is stocked"
//						));
//			}}));
//		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Ghost Pupper", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Ghost Pupper",
						Assets.luna,
						"There's no such things as ghosts. But if you see one while eating these, youre probably hallucinating from the spice. (Pet him. He's your best friend)",
						"Summon Ghost, a dog that aids you in battle",
						"Ghost now drops ammo clips and points",
						"Ghost is eager to help more often",
						"Ghost can drop powerups"
						));
			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Draculup", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler,
						"Draculup",
						Assets.vamp,
						"It might taste like iron but trust me, its just heavily oxidized ketchup. Don't think about it too much.",
						"Gain temporary health for every kill.",
						"Boss zombie kills gain bigger temporary health",
						"Temporary health can surpass max health",
						"Temporary health is now permanent"
						));
			}}));
//		i++;
//		uiManager.addObject(new TextButton(handler, x, y + i * dy, 100, height , "Sodium Overload", fontSize, new ClickListener() {
//			@Override
//			public void onClick() {
//				handler.getMouseManager().setUIManager(null);
//				State.setState(new PerkState(handler,
//						"",
//						"",
//						"",
//						"",
//						""
//						));
//			}}));
//		i++;
//		uiManager.addObject(new TextButton(handler, x, y + i * dy, 100, height , "Timeslip", fontSize, new ClickListener() {
//			@Override
//			public void onClick() {
//				handler.getMouseManager().setUIManager(null);
//				State.setState(new PerkState(handler,
//						"",
//						"",
//						"",
//						"",
//						""
//						));
//			}}));
//		i++;
//		uiManager.addObject(new TextButton(handler, x, y + i * dy, 100, height , "Break-A-Chip", fontSize, new ClickListener() {
//			@Override
//			public void onClick() {
//				handler.getMouseManager().setUIManager(null);
//				State.setState(new PerkState(handler,
//						"",
//						"",
//						"",
//						"",
//						""
//						));
//			}}));
//		i++;
//		uiManager.addObject(new TextButton(handler, x, y + i * dy, 100, height , "Frosty Ranch Tortilla", fontSize, new ClickListener() {
//			@Override
//			public void onClick() {
//				handler.getMouseManager().setUIManager(null);
//				State.setState(new PerkState(handler,
//						"",
//						"",
//						"",
//						"",
//						""
//						));
//			}}));
//		i++;
//		uiManager.addObject(new TextButton(handler, x, y + i * dy, 100, height , "Samurai Wasabi", fontSize, new ClickListener() {
//			@Override
//			public void onClick() {
//				handler.getMouseManager().setUIManager(null);
//				State.setState(new PerkState(handler,
//						"",
//						"",
//						"",
//						"",
//						""
//						));
//			}}));
//		i++;
		
		
		x = handler.getWidth()/4 - 110;
		y = 100;
		i = 0;
		dy = 40;
		fontSize = 20;
		height = 30;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Mystery Box", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler, 
						"Mystery Box",
						Assets.mysteryBox,
						"A magical box of unknown origin. Always seem to give the worst weapons.",
						
						"Spawns a random weapon",
						"When box moves, you are awarded extra points for compensation",
						"Box is less likely to move",
						"Box is less likely to give a weapon you picked up recently"	
						));

			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Perk Vendor", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler, 
						"Perk Vendor",
						Assets.perkvendor,
						"Delicious flavored chips that is totally safe for consumption! Don't look at the nutrition facts nor at the ingredients. They work for a reason. Please don't sue us.",
						
						"Gives random perk",
						"Able to dispense level 1 perks",
						"Able to dispense level 2 perks",
						"Able to dispense level 3 perks as modifier"	
						));

			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Barricades", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler, 
						"Barricades",
						Assets.dirt,

						"The only thing stopping the zombies from entering your \"safe\" space. Good luck!",
						"Block zombie entrance",
						"Repairs are free",
						"Earn points for repairing",
						"Steel barricades"	
						));

			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Deep Frier", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler, 
						"Deep Frier",
						Assets.instakill,
						"Sponsored by Super Serum Chips. We are proud of this creation.",
						
						"Upgrade your weapons",
						"Larger magazines for upgraded weapons",
						"More ammo reserve for upgraded weapons",
						"Increase more damage"	
						));

			}}));
		i++;
		uiManager.addObject(new TextButton(handler, x, y + i * dy, 200, height , "Blessings", fontSize, new ClickListener() {
			@Override
			public void onClick() {
				handler.getMouseManager().setUIManager(null);
				State.setState(new PerkState(handler, 
						"Blessings",
						Assets.tree,
						"A magical entity grants you a wish of your choosing. She seems stingy. I don't trust her.",
						"Gives blessings",
						"Increase odds of getting Fair blessings",
						"Increase odds of getting Astral blessings",
						"Increase odds of getting Immortal blessings"						));

			}}));
		i++;
		
	}

	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		uiManager.render(g);
		
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
		g.setColor(handler.getSettings().getHudColor());
		g.drawString("PERKS", handler.getWidth() - 350, 70);
		
		g.drawString("INTERACTABLES", handler.getWidth()/4 - 100, 70);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		
	
		g.setColor(handler.getSettings().getLaserColor());
		g.fillRect(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY(), 8, 8);
		
	}

}
