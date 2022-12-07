package graphics;

import java.awt.image.BufferedImage;

public class Assets {
	
	private static final int width = 100, height = 100;
	
	//static means it can be accessed from anywhere
	public static BufferedImage player[], shadow, dirt, grass, stone, 
								tree, mysteryBox, ammoBox, toxen, perkvendor, 
								zombieBlood, lickerBlood, toxenBlood, stokerBlood,
								mule, jugg, fasthand, doubletap, deadshot, phd, stam, vamp,
								crawler;
	public static BufferedImage factoryMap;
	public static BufferedImage[] zombieAnim, btn_start, btn_quit, lickerAnim;

	public static void init() {
		SpriteSheet sheet2 = new SpriteSheet(ImageLoader.loadImage("/textures/Map.png"));
		factoryMap = sheet2.crop(0, 0, 3400, 1700);
		
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/entities.png"));
	
		jugg = sheet.crop(0, 7 * height, width, height);
		fasthand = sheet.crop(width, 7*  height, width, height);
		doubletap = sheet.crop(2 * width, 7 * height, width, height);
		deadshot = sheet.crop(3 * width, 7 * height, width, height);
		phd = sheet.crop(4 * width, 7 * height, width, height);
		stam = sheet.crop(5 * width, 7 * height, width, height);
		vamp = sheet.crop(6 * width, 7 * height, width, height);
		mule = sheet.crop(7 * width, 7 * height, width, height);
		
		zombieAnim = new BufferedImage[2];
		zombieAnim[0] = sheet.crop(0, height, width, height);
		zombieAnim[1] = sheet.crop(width, height, width, height);
		crawler = sheet.crop(3 * width, height, width, height);
		
		lickerAnim = new BufferedImage[2];
		lickerAnim[0] = sheet.crop(0, 2 * height, width, height);
		lickerAnim[1] = sheet.crop(width, 2 * height, width, height);
		
		toxen = sheet.crop(0, 3 * height, width, height);
		
		btn_start = new BufferedImage[2];
		btn_start[0] = sheet.crop(0, height * 6, width * 2, height);
		btn_start[1] = sheet.crop(0, height * 5, width * 2, height);
		
		btn_quit = new BufferedImage[2];
		btn_quit[0] = sheet.crop(width * 3, height * 6, width * 2, height);
		btn_quit[1] = sheet.crop(width * 3, height * 5, width * 2, height);
		
		player = new BufferedImage[4];
		player[0] = sheet.crop(0,  0, width, height); //healthy
		player[1] = sheet.crop(width,  0, width, height); //hurt
		player[2] = sheet.crop(width * 2,  0, width, height); //damaged
		player[3] = sheet.crop(width * 3,  0, width, height); //dead
		
		shadow = sheet.crop(width * 4, 0, width, height);
		
		grass = sheet.crop(0, height * 4, width, height);
		dirt = sheet.crop(width, 4 * height, width, height);
		stone = sheet.crop(2 * width, 4 * height, width, height);
		tree = sheet.crop(3 * width, 4 * height, width, height);
		
		mysteryBox = sheet.crop(4 * width, 4 * height, 2 * width, height);
		ammoBox = sheet.crop(6 * width, 4 * height, width, height);
		perkvendor = sheet.crop(2 * width, 5 * height, width, 2 * height);
		
		zombieBlood = sheet.crop(2 * width, height, width, height);
		lickerBlood = sheet.crop(2 * width, 2 * height, width, height);
		toxenBlood = sheet.crop(width, 3 * height, width, height);
		stokerBlood = null;
	}
}
