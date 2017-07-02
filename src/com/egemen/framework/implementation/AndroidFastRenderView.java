package com.egemen.framework.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	// SurfaceView = Think of UI, a portion of the screen has info (like hp,
	// ammo) and always visible
	AndroidGame game;
	Bitmap frameBuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	// holder = for locking canvas while accessing from other threads
	volatile boolean running = false;

	// volatile is used to keep running visible to all threads

	public AndroidFastRenderView(AndroidGame game, Bitmap frameBuffer) {
		super(game);
		this.game = game;
		this.frameBuffer = frameBuffer;
		this.holder = getHolder();
	}

	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}

	@Override
	public void run() {
		Rect dstRect = new Rect();
		long startTime = System.nanoTime();
		while (running) {
			if (!holder.getSurface().isValid())
				continue;

			float deltaTime = (System.nanoTime() - startTime) / 10000000.000f; // for
																				// leaving
																				// last
																				// 3
																				// digits
			startTime = System.nanoTime();

			if (deltaTime > 3.15)
				deltaTime = (float) 3.15;
			// WHY 3.15
			// Basically, what I did with the pc applet was to fix fps at 60,
			// PCs can handle it, especially with a simple applet so no worries
			// there
			// But since we agreed on the fact that Android is a cheap OS, we
			// need to set update functions (character movement etc...)
			// throughout the game depending on the deltaTime
			// So even if fps drops below 60, our movement won't depend on the
			// fps and will be fluid as ever
			// But if it goes above a certain level (3.15 was enough to prevent
			// the game from breaking) some calculations (like collision) would
			// be misled

			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().paint(deltaTime);

			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(frameBuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void pause() {
		running = false;
		while (true) {
			try {
				renderThread.join();
				break;
			} catch (InterruptedException e) {
				// retry;
			}
		}
	}

}
