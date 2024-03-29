package main;

import entities.creatures.Player;
import graphics.GameCamera;
import hud.HudManager;
import input.KeyManager;
import input.MouseManager;
import worlds.World;
import zombieLogic.RoundLogic;

public class Handler {

	private Game game;
	private World world;
	private Player player;
	private RoundLogic rounds;
	private HudManager hud;
	private Settings settings;
	private GlobalStats globalStats;
	private Progression progression;
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Handler(Game game) {
		this.game = game;
		settings = new Settings(this);
		globalStats = new GlobalStats(this);
		progression = new Progression(this);
	}
	
	public GameCamera getGameCamera() {
		return game.getGameCamera();
	}
	
	public KeyManager getKeyManager() {
		return game.getKeyManager();
	}
	
	public MouseManager getMouseManager() {
		return game.getMouseManager();
		}
	
	public int getWidth() {
		return game.getWidth();
	}
	
	public int getHeight() {
		return game.getHeight();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	public RoundLogic getRoundLogic() {
		return rounds;
	}
	public void setRoundLogic(RoundLogic rounds) {
		this.rounds = rounds;
	}

	public HudManager getHud() {
		return hud;
	}

	public void setHud(HudManager hud) {
		this.hud = hud;
	}

	public Settings getSettings() {
		return settings;
	}

	public GlobalStats getGlobalStats() {
		return globalStats;
	}

	public void setGlobalStats(GlobalStats globalStats) {
		this.globalStats = globalStats;
	}

	public Progression getProgression() {
		return progression;
	}

	public void setProgression(Progression progression) {
		this.progression = progression;
	}
	
	

	
}
