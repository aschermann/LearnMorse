package pl.hubot.dev.learn_morse.model;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

class Keyer {
	/**
	 * Transmit dah.
	 * @throws LineUnavailableException sound(...) method
	 * @throws InterruptedException Thread.sleep(...) method
	 */
	static void dah() throws LineUnavailableException, InterruptedException {
		sound(MorseSettings.getDahLength());
		pauseCharElement();
	}

	/**
	 * Transmit dit.
	 * @throws LineUnavailableException sound(...) method
	 * @throws InterruptedException Thread.sleep(...) method
	 */
	static void dit() throws LineUnavailableException, InterruptedException {
		sound(MorseSettings.getDitLength());
		pauseCharElement();
	}

	/**
	 * Make pause before keying.
	 * @throws InterruptedException Thread.sleep(...) method
	 */
	static void pauseBeforeKeying() throws InterruptedException {
		Thread.sleep(MorseSettings.getPauseBeforeKeying());
	}

	/**
	 * Make pause between two char elements.
	 * @throws InterruptedException Thread.sleep(...) method
	 */
	private static void pauseCharElement() throws InterruptedException {
		Thread.sleep(MorseSettings.getLengthOfSpaceBetweenCharElements());
	}

	/**
	 * Make pause between two chars.
	 * @throws InterruptedException Thread.sleep(...) method
	 */
	static void pauseChar() throws InterruptedException {
		Thread.sleep(MorseSettings.getLengthOfSpaceBetweenCharacters());
	}

	/**
	 * Make pause between two words.
	 * @throws InterruptedException Thread.sleep(...) method
	 */
	static void pauseWord() throws InterruptedException {
		Thread.sleep(MorseSettings.getLengthOfSpaceBetweenWords());
	}

	/**
	 * Play sound with defined frequency by MorseSettings object.
	 * @param msecs duration
	 * @throws LineUnavailableException thrown if writing sound buffer to audio source data line fails.
	 */
	private static void sound(int msecs) throws LineUnavailableException {
		byte[] buf = new byte[msecs * Byte.SIZE];
		for (int i = 0; i < buf.length; i++) {
			double angle = 2 * Math.PI * i * MorseSettings.getFrequency() / SAMPLING_FREQUENCY_IN_HZ;
			buf[i] = (byte) (Math.sin(angle) * MorseSettings.getVolume() * Byte.MAX_VALUE);
		}
		for (int i = 0; i < NUMBER_OF_FADE_SAMPLES && i < buf.length / 2; i++) {
			buf[i] = (byte) (buf[i] * i / NUMBER_OF_FADE_SAMPLES);
			buf[buf.length - 1 - i] = (byte) (buf[buf.length - 1 - i] * i / NUMBER_OF_FADE_SAMPLES);
		}
		AudioFormat af = new AudioFormat(SAMPLING_FREQUENCY_IN_HZ, Byte.SIZE, 1, true, false);
		SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		sdl.open(af);
		sdl.start();
		sdl.write(buf, 0, buf.length);
		sdl.drain();
		sdl.close();
	}

	private static final int SAMPLING_FREQUENCY_IN_HZ = 8000;
	private static final int NUMBER_OF_FADE_SAMPLES = 80;
}
