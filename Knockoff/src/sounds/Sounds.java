package sounds;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import utils.Utils;

public class Sounds {
	
//	@SuppressWarnings({ "deprecation", "removal" })
//	public static final AudioClip SHOOT = Applet.newAudioClip(Sounds.class.getResource("shootBeta.wav"));
	
	
	//public static boolean isPlaying = false;
	
	public static URL shootBeta;
	public static URL swoosh;
	public static URL yum;
	
	public static void init() {
		shootBeta = Utils.class.getResource("/sounds/shootBeta.wav");
		swoosh = Utils.class.getResource("/sounds/swoosh.wav");
		yum = Utils.class.getResource("/sounds/yum.wav");
	}

	public static void playClip(URL clipFile) {
		new Thread(new Runnable() {
		
		class AudioListener implements LineListener {
			private boolean done = false;

			@Override
			public synchronized void update(LineEvent event) {
				Type eventType = event.getType();
				if (eventType == Type.STOP || eventType == Type.CLOSE) {
					done = true;
					//notifyAll();
				}
			}

			public synchronized void waitUntilDone() throws InterruptedException {
				while (!done) {
					wait();
				}
			}
		}
		
	
		@Override
		public void run() {
			AudioListener listener = new AudioListener();
			AudioInputStream audioInputStream = null;
			try {
				audioInputStream = AudioSystem.getAudioInputStream(clipFile);
			} catch (UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				try {
			Clip clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
		
				clip.start();
				//isPlaying = true;
				listener.waitUntilDone();
				//isPlaying = false;
				clip.close();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//}
	finally {
		try {
			audioInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
				
				}
	}).start();
	}
}