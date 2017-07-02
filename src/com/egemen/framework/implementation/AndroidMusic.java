package com.egemen.framework.implementation;

import java.io.IOException;

import com.egemen.framework.Music;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;

public class AndroidMusic implements Music, OnCompletionListener,
		OnSeekCompleteListener, OnPreparedListener, OnVideoSizeChangedListener {
	MediaPlayer mediaPlayer;
	boolean isPrepared = false;

	public AndroidMusic(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			// setDataSource = which file to get(fileDesc (points at the start
			// of desc - like Unix file system)), offset value to be applied to
			// the current pointer, length of the file
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnSeekCompleteListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnVideoSizeChangedListener(this);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't load music file");
		}
	}

	@Override
	public void play() {
		if(this.mediaPlayer.isPlaying())
			return;
		try{
			synchronized (this) {
				if(!isPrepared)
					mediaPlayer.prepare();
				mediaPlayer.start();
			}
		}catch (IllegalStateException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		if(this.mediaPlayer.isPlaying() == true)
		{
			this.mediaPlayer.stop();
			
			synchronized (this) {
				isPrepared = false;			
			}
		}
	}

	@Override
	public void pause() {
		if(this.mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	@Override
	public void setLooping(boolean looping) {
		this.mediaPlayer.setLooping(looping);
	}

	@Override
	public void setVolume(float volume) {
		this.mediaPlayer.setVolume(volume, volume);
	}

	@Override
	public boolean isPlaying() {
		return this.mediaPlayer.isPlaying();
	}

	@Override
	public boolean isStopped() {
		return !isPrepared;
	}

	@Override
	public boolean isLooping() {
		return this.mediaPlayer.isLooping();
	}

	@Override
	public void dispose() {
		if(this.mediaPlayer.isPlaying()){
			this.mediaPlayer.stop();
		}
		this.mediaPlayer.release();
	}

	@Override
	public void seekBegin() {
		this.mediaPlayer.seekTo(0);
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepared(MediaPlayer player) {
		synchronized (this) {
			isPrepared = true;			
		}
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		synchronized (this) {
			isPrepared = false;			
		}
	}

}
