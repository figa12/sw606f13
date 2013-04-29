package dk.aau.cs.giraf.train.opengl;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint.Align;

import dk.aau.cs.giraf.pictogram.Pictogram;


public class GlPictogram extends Texture {
    
    private Text pictogramText;
    private boolean containsImage = false;
    
    public GlPictogram(float width, float height) {
        super(width, height);
    }
    
    public void loadPictogram(GL10 gl, Context context, Pictogram pictogram) {
        Bitmap canvasBitmap = null;
        Canvas canvas = null;
        
        if(pictogram.getImagePath() != null) {
            this.containsImage = true;
            int pictogramSize = 150; //150x150 pixels used for one pictogram
            
            //Create bitmap from pictogram image path
            Bitmap originalBitmap = BitmapFactory.decodeFile(pictogram.getImagePath());
            Bitmap pictogramBitmap;
            
            //Scale the bitmap where the biggest side equal to #pictogramSize. Maintian aspect ratio
            if(originalBitmap.getWidth() <= originalBitmap.getHeight()) {
                pictogramBitmap = Bitmap.createScaledBitmap(originalBitmap, pictogramSize * originalBitmap.getWidth() / originalBitmap.getHeight(), pictogramSize, true);
            } else {
                pictogramBitmap = Bitmap.createScaledBitmap(originalBitmap, pictogramSize, pictogramSize * originalBitmap.getHeight() / originalBitmap.getWidth(), true);
            }
            
            canvasBitmap = Bitmap.createBitmap(pictogramSize, pictogramSize, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(canvasBitmap);
            
            //Offset image to center in the available space
            float xOffset = (pictogramSize - pictogramBitmap.getWidth())  / 2f;
            float yOffset = (pictogramSize - pictogramBitmap.getHeight()) / 2f;
            
            //Then draw the pictogram image
            canvas.drawBitmap(pictogramBitmap, xOffset, yOffset, null);
            pictogramBitmap.recycle();
        }
        
        if (pictogram.getTextLabel() != null) {
            //If the pictogram contains text
            this.pictogramText = new Text(1f, 1f, 14f, Align.CENTER);
            this.pictogramText.loadText(gl, context, pictogram.getTextLabel());
        } 
        
        if (pictogram.getImagePath() == null && pictogram.getTextLabel() == null) {
            //If image and text wasn't present then create empty bitmap
            canvasBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
        
        super.loadTexture(gl, context, canvasBitmap);
        canvasBitmap.recycle();
    }
    
    @Override
    public void draw(GL10 gl, Coordinate coordinate) {
        this.draw(gl, coordinate, Color.White);
    }
    
    @Override
    public void draw(GL10 gl, Coordinate coordinate, Color color) {
        if (this.containsImage) {
            super.draw(gl, coordinate, color);
        }
        
        if (this.pictogramText != null) {
            if (this.containsImage) {
                float x = super.getWidth() / 2f;
                float y = -(super.getHeight() - this.pictogramText.getHeight());
                
                gl.glTranslatef(x, y, 0f);
                this.pictogramText.draw(gl, coordinate, Color.Gray); //FIXME why gray?
                gl.glTranslatef(-x, -y, 0f);
            } else {
                float x = super.getWidth() / 2f;
                float y = -((super.getHeight() / 2f) - this.pictogramText.getHeight() / 2f);
                
                gl.glTranslatef(x, y, 0f);
                this.pictogramText.draw(gl, coordinate, Color.Gray); //FIXME why gray?
                gl.glTranslatef(-x, -y, 0f);
            }
        }
    }
}
