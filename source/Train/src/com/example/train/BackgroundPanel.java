package com.example.train;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.train.models.Background;

public class BackgroundPanel extends SurfaceView implements SurfaceHolder.Callback {

	private MainThread thread;
	private Background backGroundArray[];
	private Background displayedBackground[];
	
	public BackgroundPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}	
	
	public BackgroundPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public BackgroundPanel(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		getHolder().addCallback(this);
		backGroundArray = new Background[2];
		backGroundArray[0] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundpink), 1 , 1);
		backGroundArray[1] = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundmaroon), backGroundArray[0].getBitmap().getWidth() , 0);
		thread = new MainThread(getHolder(), this);
		setFocusable(false);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while(retry){
			try{	
				thread.join();
				retry = false;
			} catch(InterruptedException e){
				
			}
		}
	}
	
	private int exiting = 0;
	private int entering = 1;
	public void update(){		
		if (backGroundArray[exiting].getX() + backGroundArray[exiting].getBitmap().getWidth() < 0){
			backGroundArray[exiting].setX((backGroundArray[entering].getX() + backGroundArray[entering].getBitmap().getWidth()));
			exiting = 1;
			entering = 0;
		}
		
		backGroundArray[exiting].update();
		backGroundArray[entering].update();
	}
	
	public void render(Canvas canvas){
		for(int i = 0; i < 2; i++){
			backGroundArray[i].draw(canvas);
		}
	}

}
