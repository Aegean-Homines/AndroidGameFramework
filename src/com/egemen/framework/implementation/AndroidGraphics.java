package com.egemen.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import com.egemen.framework.Graphics;
import com.egemen.framework.Image;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class AndroidGraphics implements Graphics {
	AssetManager assets;
	// assetmanager is used for reading raw data (in resources). It is like a
	// file manager (open, read, write, close)
	Bitmap frameBuffer;
	// bitmap = for creating image objects
	Canvas canvas;
	// canvas = we draw images on it
	Paint paint;
	// paint = for style of drawings
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}

	@Override
	public Image newImage(String fileName, ImageFormat format) {
		Config config = null;
		// configuration for bitmap (color depth, quality, #of pixels stored
		// etc...)
		//RGB565 = least memory but no transparency
		//ARGB4444 = use this when you need transparency
		//ARGB8888 = double depth of 4444, but takes up a lot of memory
		if (format == ImageFormat.RGB565)
			config = Config.RGB_565;
		else if (format == ImageFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;

		Options options = new Options();
		options.inPreferredConfig = config;

		InputStream in = null;
		Bitmap bitmap = null;
		try {
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			// BitmapFactory is used to create bitmap from resources
			// decodeStream takes an input and decodes it into bitmap w.r.t.
			// options. If no input, returns null
			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset: "
						+ fileName);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset: "
					+ fileName);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}

		if (bitmap.getConfig() == Config.RGB_565)
			format = ImageFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = ImageFormat.ARGB4444;
		else
			format = ImageFormat.ARGB8888;

		return new AndroidImage(bitmap, format);
	}

	@Override
	public void clearScreen(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
				(color & 0xff));
		// This is not my code so I have to check this one out specifically.
		// What it does is that it applies logical AND between color and some
		// bit sequence, and than right shifts it by 16 bits.
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, int color) {
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}

	@Override
	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}

	@Override
	public void drawImage(Image image, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.right = srcX + srcWidth;
		srcRect.top = srcY;
		srcRect.bottom = srcY + srcHeight;

		dstRect.left = x;
		dstRect.right = x + srcWidth;
		dstRect.top = y;
		dstRect.bottom = y + srcHeight;

		canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);

	}

	@Override
	public void drawImage(Image image, int x, int y) {
		canvas.drawBitmap(((AndroidImage) image).bitmap, x, y, null);

	}

	@Override
	public void drawString(String text, int x, int y, Paint paint) {
		canvas.drawText(text, x, y, paint);
	}

	@Override
	public void drawARGB(int i, int j, int k, int l) {
		paint.setStyle(Style.FILL);
		canvas.drawARGB(i, j, k, l);
	}

	public void drawScaledImage(Image image, int x, int y, int width,
			int height, int srcX, int srcY, int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.right = srcX + srcWidth;
		srcRect.top = srcY;
		srcRect.bottom = srcY + srcHeight;

		dstRect.left = x;
		dstRect.right = x + width;
		dstRect.top = y;
		dstRect.bottom = y + height;

		canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
	}

	@Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	@Override
	public int getHeight() {
		return frameBuffer.getHeight();
	}

}
