package com.egemen.framework.implementation;

import java.io.IOException;

import com.egemen.framework.Audio;
import com.egemen.framework.Music;
import com.egemen.framework.Sound;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class AndroidAudio implements Audio {
	AssetManager assets;
	SoundPool soundPool;

	// soundpool is used for loading and compressing sound objects into audio
	// before playing them.
	// This saves up CPU time during playback (since it is already prepared
	// before playing)

	public AndroidAudio(Activity activity) {
		// Line below provides us to determine which control scheme will be
		// applied to our activity (our game)
		// We set its volume control to stream music so incoming calls or other
		// system sounds won't be affected by our settings during this activity
		// cycle
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	// WHY DO WE USE TWO DIFFERENT AUDIO CLASSES:
	// The main problem with Android is that it is a cheap, greedy creature who
	// reluctantly gives you a little bit of memory
	// For this reason we have to do the best we can with the memory we have,
	// hence deciding on whether to load a file into memory or not is crucial
	// Music takes a lot of memory space = let's play it from the file, a little
	// bit of a loading time but whatevs.
	// Sounds are simple, fast and short = let's load 'em up to make it fast

	@Override
	public Music createMusic(String file) {
		try {
			// Line below returns a file descriptor that points to our current
			// file (in this case the audio file)
			// we can use this descriptor to change its values (like offset
			// values or sound length)
			AssetFileDescriptor assetDescriptor = assets.openFd(file);
			return new AndroidMusic(assetDescriptor);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load music from: " + file);
		}
	}

	@Override
	public Sound createSound(String file) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(file);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load sound from: " + file);
		}
	}

}
