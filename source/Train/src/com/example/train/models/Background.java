package com.example.train.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.train.models.components.Speed;

public class Background {
	
	private Bitmap bitmap;
	private int x;
	private int y;
	private Speed speed;
	
	public Background(Bitmap bitmap, int x, int y){
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = new Speed();
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public int getX(){
		return x;
	}
	public void setX(int x){
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Speed getSpeed(){
		return speed;
	}
	
	public void setSpeed(Speed speed){
		this.speed = speed;
	}
	
	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, x, y, null);
	}
	
	public void update() {
		x += (speed.getXv() * speed.getxDirection());
	}
	
	

}
