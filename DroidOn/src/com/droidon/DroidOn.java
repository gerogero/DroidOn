package com.droidon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class DroidOn extends WallpaperService {

	@Override
	public Engine onCreateEngine() {
		return new MyEngine();
	}

	class MyEngine extends Engine{

		DroidThread dt;
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			// TODO Auto-generated method stub
			super.onSurfaceChanged(holder, format, width, height);
			dt.sh=holder;
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			super.onSurfaceCreated(holder);
			dt=new DroidThread(holder);
			dt.start();
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			dt.onDestroy();
			if(dt!=null){
				dt=null;
			}
		
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				dt.setPosition(event.getX(),event.getY());
			}
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if(visible){
				pause=false;
			}else{
				pause=true;
			}
		}
	}

	Boolean pause;
	
	
	
	class DroidThread extends Thread{
		
		SurfaceHolder sh;
		float x,y;
		Boolean destroy=false;
		Bitmap bmp;
		
		public DroidThread(SurfaceHolder holder){
			sh=holder;
			bmp=BitmapFactory.decodeResource(getResources(), R.drawable.sample);
		}
		
		
		@Override
		public void run() {
			
			while(!destroy){
				
				if(!pause){
					Canvas cas=sh.lockCanvas();
					cas.drawColor(Color.BLACK);
					if(cas!=null){
						cas.drawBitmap(bmp, x, y,null);
					}
					sh.unlockCanvasAndPost(cas);
				}else{
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		
		}
		
		
		
		public void onDestroy(){
			synchronized(this){
				destroy=true;
			}
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		public void setPosition(float x, float y){
			this.x=x;
			this.y=y;
		}
		
		
		
	}
	
}
