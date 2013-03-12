package com.example.train;

import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread{

	private boolean running;
	private SurfaceHolder surfaceHolder;
	private BackgroundPanel backPanel;
	
	private final static int MAX_FPS = 50;
	private final static int MAX_FRAME_SKIPS = 5;
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;
	
	//stuff for FPS readings
	private DecimalFormat df = new DecimalFormat("0.##");
	//stat interval
	private final static int STAT_INTERVAL = 1000;
	private final static int FPS_HISTORY_NR = 10;
	private long lastStatusStore = 0;
	private long statusIntervalTimer = 0l;
	private long totalFramesSkipped = 0l;
	private long framesSkippedPerStatCycle = 0l;
	
	private int frameCountPerStatCycle = 0;
    private long totalFrameCount = 0l;
    // the last FPS values
    private double fpsStore[];
    // the number of times the stat has been read
    private long statsCount = 0l;
    // the average FPS since the game started
    private double  averageFps = 0.0;
	
    private static final String TAG = MainThread.class.getSimpleName();
    
	public MainThread(SurfaceHolder surfaceHolder, BackgroundPanel backPanel){
		super();
		this.surfaceHolder = surfaceHolder;
		this.backPanel = backPanel;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
	
	public void run(){
		Canvas canvas;
		Log.d(TAG, "Starting the Game");
		iniTimingElements();
		
		long beginTime;
		long timeDiff;
		int sleepTime;
		int framesSkipped;
		
		while(running){
			canvas = null;
			
			
			try{
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder){
					
					beginTime =System.currentTimeMillis();
					framesSkipped = 0;
					
					this.backPanel.update();
					this.backPanel.render(canvas);
					
					timeDiff = System.currentTimeMillis() - beginTime;
					
					sleepTime = (int) (FRAME_PERIOD - timeDiff);
					
					if(sleepTime > 0){
						try{
							Thread.sleep(sleepTime);
						}catch (InterruptedException e){
							
						}
					}
					
					while(sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						this.backPanel.update();
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
					
					if(framesSkipped > 0) {
						Log.d(TAG, "Skipped: " + framesSkipped);
					}
					//update game state
					framesSkippedPerStatCycle = framesSkippedPerStatCycle + framesSkipped;
					//render state to the screen
					storeStats();
					
				}
			}  finally{
				if(canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	
	private void storeStats() {
		frameCountPerStatCycle++;
		totalFrameCount++;
		double actualFps = 0.0;
		
		statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);
		
		if(statusIntervalTimer >= lastStatusStore + STAT_INTERVAL)	{
			
			actualFps = (double)(frameCountPerStatCycle / (STAT_INTERVAL / 1000));
			
			//store the lastest fps in the array
			fpsStore[(int)statsCount % FPS_HISTORY_NR] = actualFps;
			
			statsCount++;
			
			double totalFps = 0.0;
			//sum up the stored fps values
			for(int i = 0; i < FPS_HISTORY_NR; i++){
				totalFps += fpsStore[i];
			}
			
			if(statsCount < FPS_HISTORY_NR){
				// in case the first 10 triggers
				averageFps = totalFps / statsCount;
			} else {
				averageFps = totalFps / FPS_HISTORY_NR;
			}
			
            // saving the number of total frames skipped
            totalFramesSkipped += framesSkippedPerStatCycle;
            // resetting the counters after a status record (1 sec)
            framesSkippedPerStatCycle = 0;
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;
            
            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;
            Log.d(TAG, "Average FPS:" + df.format(averageFps));
//            backPanel.setAvgFps("FPS: " + df.format(averageFps));
		}
	}
	
	private void iniTimingElements(){
		fpsStore = new double[FPS_HISTORY_NR];
		for (int i = 0; i < FPS_HISTORY_NR; i++){
			fpsStore[i] = 0.0;
		}
		Log.d(TAG + ".initTimingElements()", "Timing elements for stats initialised");
	}
	
}
