package main;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import display.Display;
import graphics.Assets;
import graphics.GameCamera;
import input.KeyManager;
import input.MouseManager;
import states.MenuState;
import states.State;

public class Game implements Runnable {
	private Display display;
	private int width, height;

	public String title;
	
	private boolean running = false;
	private Thread thread;
	
	//buffer strategy creates buffers (hidden screens) to prevent screen flickering
	private BufferStrategy bs;
	private Graphics g;

	//States
	public State gameState, menuState;
	
	//input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//camera
	private GameCamera gameCamera;
	
	//handler
	private Handler handler;
	
	
	public Game(String title, int width, int height) {
		this.width = width;
		this.height = height;
		this.title = title;
		handler = new Handler(this);
		keyManager = new KeyManager();
		mouseManager = new MouseManager(handler);
		start();
		
	}
	
	//initializes graphics
	private void init() {
		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getFrame().addMouseWheelListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseWheelListener(mouseManager);
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		display.getFrame().getContentPane().setCursor(blankCursor);
		
		Assets.init();
		
	
		gameCamera = new GameCamera(handler, 0, 0);
		
		
		//gameState = new GameState(handler);
		menuState = new MenuState(handler);
		State.setState(menuState);
	}
	
	int x = 0;
	
	//update variables, positions of objects, etc.
	private void tick() {
		keyManager.tick();
		
		if(State.getState() != null) {
			State.getState().tick();
		}
	}
	
	//draw all graphics to screen
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3); //you dont need more than 3 buffers
			return;
		}
		
		g = bs.getDrawGraphics();
		
		//clear screen
		g.clearRect(0, 0, width, height);
		
		//draw here
		if(State.getState() != null) {
			State.getState().render(g);
		}
		
		//end drawing
		bs.show();
		g.dispose();
	}

	@Override
	public void run() {
		init();
		
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running) {
			now = System.nanoTime();
			
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1) {
				render();
				tick();
				ticks++;
				delta = delta - (int) delta;
			}
			
			if(timer >= 1000000000) {
				System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
			
			
		}
		
		try {
			stop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public MouseManager getMouseManager() {
		return mouseManager;
	}
	
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public synchronized void start() {
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start(); //calls run method
	}
	
	public synchronized void stop() throws InterruptedException {
		if(!running)
			return;
		thread.join();
	}
}
