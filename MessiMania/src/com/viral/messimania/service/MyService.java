package com.viral.messimania.service;

import java.util.Random;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.viral.messimania.R;
import com.viral.messimania.Utils;

public class MyService extends WallpaperService {
	
	private static Bitmap bm = null;
	
	private int footballImages[] = { R.drawable.f_1, R.drawable.f_2,
			R.drawable.f_3, R.drawable.f_4, R.drawable.f_6, R.drawable.f_7,
			R.drawable.f_8 };
	
	private int messiImages[] = { R.drawable.messi_1, R.drawable.messi_2,
			R.drawable.messi_3, R.drawable.messi_4, R.drawable.messi_5,
			R.drawable.messi_6, };
	
	private Bitmap backgroundBitmap;
	
	private static final String TOUCH_ENABLED = "TOUCH_ENABLED";
	private static final String REFERSH_RATE = "REFERSH_RATE";
	
	private int refreshRate;
	private boolean isTouchEnabled = false;
	
	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}
	
	private class MyWallpaperEngine extends Engine {
		private final Handler handler = new Handler();
		
		private final Runnable drawRunner = new Runnable() {
			
			@Override
			public void run() {
				draw();
			}
			
		};
		private Paint paint = new Paint();
		private int width;
		int height;
		private boolean visible = true;
		private boolean touchEnabled;
		
		public MyWallpaperEngine() {
			
			try {
				refreshRate = Integer.parseInt(Utils.getPreference(
						getApplicationContext(), REFERSH_RATE));
				isTouchEnabled = Boolean.parseBoolean(Utils.getPreference(
						getApplicationContext(), TOUCH_ENABLED));
			}
			catch (NumberFormatException e) {
				Log.e("format", "NumberFormatException", e);
			}
			
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(MyService.this);
			
			touchEnabled = prefs.getBoolean("touch", isTouchEnabled);
			paint.setAntiAlias(true);
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(10f);
			handler.post(drawRunner);
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			if (visible) {
				handler.post(drawRunner);
			}
			else {
				handler.removeCallbacks(drawRunner);
			}
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}
		
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			this.width = width;
			this.height = height;
			try {
				refreshRate = Integer.parseInt(Utils.getPreference(
						getApplicationContext(), REFERSH_RATE));
				isTouchEnabled = Boolean.parseBoolean(Utils.getPreference(
						getApplicationContext(), TOUCH_ENABLED));
			}
			catch (NumberFormatException e) {
				Log.e("format", "NumberFormatException", e);
			}
			
			super.onSurfaceChanged(holder, format, width, height);
		}
		
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (isTouchEnabled) {
				
				float x = event.getX();
				float y = event.getY();
				SurfaceHolder holder = getSurfaceHolder();
				Canvas canvas = null;
				try {
					canvas = holder.lockCanvas();
					if (canvas != null) {
						switch (event.getAction()) {
						
							case MotionEvent.ACTION_DOWN:
								bm = getRandomPic();
								
								break;
							case MotionEvent.ACTION_MOVE:
								break;
						}
						drawCircles(canvas, bm, x - 125, y - 125);
					}
				}
				finally {
					if (canvas != null)
						holder.unlockCanvasAndPost(canvas);
					
				}
				super.onTouchEvent(event);
			}
		}
		
		private void draw() {
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null) {
					backgroundBitmap = Bitmap.createScaledBitmap(BitmapFactory
							.decodeResource(getResources(),
									messiImages[new Random()
											.nextInt(messiImages.length - 1)]),
							width, height, false);
					canvas.drawBitmap(backgroundBitmap, 0, 0, null);
				}
			}
			finally {
				try {
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}
				}
				catch (IllegalArgumentException e) {
					Log.e("error", "IllegalArgumentException", e);
				}
			}
			handler.removeCallbacks(drawRunner);
			if (visible) {
				handler.postDelayed(drawRunner, refreshRate);
			}
		}
		
		// Surface view requires that all elements are drawn completely
		private void drawCircles(Canvas canvas, Bitmap bm, float x, float y) {
			if (backgroundBitmap != null) {
				canvas.drawBitmap(backgroundBitmap, 0, 0, null);
			}
			if (bm != null) {
				canvas.drawBitmap(bm, x, y, null);
			}
		}
	}
	
	private Bitmap getRandomPic() {
		Bitmap bm = null;
		
		bm = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(),
						footballImages[new Random()
								.nextInt(footballImages.length - 1)]), 250,
				250, false);
		return bm;
	}
}