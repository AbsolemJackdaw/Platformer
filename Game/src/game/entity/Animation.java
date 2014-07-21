package game.entity;

import game.content.Images;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] frames;
	private int currentFrame;

	private long startTime;
	private long delay;

	private boolean playedOnce;
	
	public static final int NONE = -1;

	public Animation() {
		playedOnce = false;
		//default animation image so no null pointer occurs when first initializing an animation
		setFrames(Images.instance.defaultAnim);
		setDelay(100);
	}

	public long getDelay(){
		return delay;
	}

	public int getFrame() {
		return currentFrame;
	}

	public BufferedImage getImage() {
		return frames[currentFrame];
	}

	public boolean hasPlayedOnce() {
		return playedOnce;
	}

	public void setDelay(long d) {
		delay = d;
	}

	public void setFrame(int i){
		currentFrame = i;
	}

	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}

	@Override
	public String toString() {
		return "Animation : current frame = " + currentFrame + " number of frames = "+ frames.length + " delay = " + delay + " has played once = "+ hasPlayedOnce();
	}

	public void update() {

		if (delay == NONE)
			return;

		final long elapsed = (System.nanoTime() - startTime) / 1000000;

		if (elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}

		if (currentFrame == frames.length){
			currentFrame = 0;
			playedOnce = true;
		}

	}

}
