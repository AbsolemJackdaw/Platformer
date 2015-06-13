package base.main.music;

import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {

	private static HashMap<String, Clip> clips;
	private static int gap;
	private static boolean mute = false;

	public static void close(String s) {
		stop(s);
		clips.get(s).close();
	}

	public static int getFrames(String s) {
		return clips.get(s).getFrameLength();
	}

	public static int getPosition(String s) {
		return clips.get(s).getFramePosition();
	}

	public static void init() {
		clips = new HashMap<String, Clip>();
		gap = 0;
		
		load("/sounds/hit_wood_1.mp3", "hit_wood_1");
		load("/sounds/hit_wood_2.mp3", "hit_wood_2");
		load("/sounds/hit_wood_3.mp3", "hit_wood_3");
		load("/sounds/hit_wood_4.mp3", "hit_wood_4");
		load("/sounds/hit_wood_5.mp3", "hit_wood_5");
		load("/sounds/hit_wood_6.mp3", "hit_wood_6");

		load("/sounds/hit_rock_1.mp3", "hit_rock_1");
		load("/sounds/hit_rock_2.mp3", "hit_rock_2");
		load("/sounds/hit_rock_3.mp3", "hit_rock_3");
		load("/sounds/hit_rock_4.mp3", "hit_rock_4");
		load("/sounds/hit_rock_5.mp3", "hit_rock_5");

		load("/sounds/step_1.mp3", "step_1");
		load("/sounds/step_2.mp3", "step_2");
		load("/sounds/step_3.mp3", "step_3");
		load("/sounds/step_4.mp3", "step_4");
		load("/sounds/step_5.mp3", "step_5");
		
		load("/sounds/jump_1.mp3", "jump_1");
		load("/sounds/jump_2.mp3", "jump_2");
		load("/sounds/jump_3.mp3", "jump_3");
		load("/sounds/jump_4.mp3", "jump_4");
		load("/sounds/jump_5.mp3", "jump_5");

		load("/sounds/pig_hurt_1.mp3", "hitpig_1");
		load("/sounds/pig_hurt_2.mp3", "hitpig_2");
		load("/sounds/pig_hurt_3.mp3", "hitpig_3");
		load("/sounds/pig_hurt_4.mp3", "hitpig_4");
		load("/sounds/pig_hurt_5.mp3", "hitpig_5");


	}

	public static void load(String s, String n) {
		if (clips.get(n) != null)
			return;
		Clip clip;
		try {
			final AudioInputStream ais = AudioSystem
					.getAudioInputStream(Music.class.getResourceAsStream(s));
			final AudioFormat baseFormat = ais.getFormat();
			final AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			final AudioInputStream dais = AudioSystem.getAudioInputStream(
					decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			clips.put(n, clip);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void loop(String s) {
		loop(s, gap, gap, clips.get(s).getFrameLength() - 1);
	}

	public static void loop(String s, int frame) {
		loop(s, frame, gap, clips.get(s).getFrameLength() - 1);
	}

	public static void loop(String s, int start, int end) {
		loop(s, gap, start, end);
	}

	public static void loop(String s, int frame, int start, int end) {
		stop(s);
		if (mute)
			return;
		clips.get(s).setLoopPoints(start, end);
		clips.get(s).setFramePosition(frame);
		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void play(String s) {
		if(!clips.containsKey(s)){
			System.out.println("the key " + s + " for sounds does not exist");
			return;
		}
		play(s, gap);
	}

	public static void play(String s, int i) {
		if (mute)
			return;
		final Clip c = clips.get(s);
		if (c == null)
			return;
		if (c.isRunning())
			c.stop();
		c.setFramePosition(i);
		while (!c.isRunning())
			c.start();
	}

	public static void resume(String s) {
		if (mute)
			return;
		if (clips.get(s).isRunning())
			return;
		clips.get(s).start();
	}

	public static void setPosition(String s, int frame) {
		clips.get(s).setFramePosition(frame);
	}

	public static void stop(String s) {
		if (clips.get(s) == null)
			return;
		if (clips.get(s).isRunning())
			clips.get(s).stop();
	}

}