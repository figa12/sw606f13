package dk.aau.cs.giraf.train.opengl;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.widget.TextView;

import dk.aau.cs.giraf.pictogram.Pictogram;


/**
 * An OpenGL {@link Renderable} pictogram.
 * @author Jesper Riemer Andersen
 * @see Texture
 */
public class GlPictogram extends Texture {
    
    /** Use 150x150 as the resolution of the pictogram. */
    protected int pictogramSize = 150;
    
    /** The text associated to the pictogram. */
    private Text pictogramText;
    /** True if the pictogram contains an image. */
    private boolean containsImage = false;
    
    /**
     * Creates a new {@link GlPictogram}.
     * @param width  of the pictogram.
     * @param height of the pictogram.
     */
    public GlPictogram(float width, float height) {
        super(width, height);
    }
    
    /**
     * Creates a {@link Texture} instance of the specified pictogram.
     * The pictogram uses a resolution of {@link #pictogramSize}.
     * @param gl        the {@link GL10} instance.
     * @param context   the current activity context.
     * @param pictogram the pictogram to load.
     */
    public void loadPictogram(GL10 gl, Context context, Pictogram pictogram) {
        Bitmap canvasBitmap = null;
        Canvas canvas = null;
        
        if(pictogram.getImagePath() != null) {
            this.containsImage = true;
            
            //Create bitmap from pictogram image path
            Bitmap originalBitmap = BitmapFactory.decodeFile(pictogram.getImagePath());
            Bitmap pictogramBitmap;
            
            //Scale the bitmap where the biggest side equal to #pictogramSize. Maintian aspect ratio
            if(originalBitmap.getWidth() <= originalBitmap.getHeight()) {
                pictogramBitmap = Bitmap.createScaledBitmap(originalBitmap, this.pictogramSize * originalBitmap.getWidth() / originalBitmap.getHeight(), this.pictogramSize, true);
            } else {
                pictogramBitmap = Bitmap.createScaledBitmap(originalBitmap, this.pictogramSize, this.pictogramSize * originalBitmap.getHeight() / originalBitmap.getWidth(), true);
            }
            
            canvasBitmap = Bitmap.createBitmap(this.pictogramSize, this.pictogramSize, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(canvasBitmap);
            
            //Offset image to center in the available space
            float xOffset = (this.pictogramSize - pictogramBitmap.getWidth())  / 2f;
            float yOffset = (this.pictogramSize - pictogramBitmap.getHeight()) / 2f;
            
            //Then draw the pictogram image
            canvas.drawBitmap(pictogramBitmap, xOffset, yOffset, null);
            pictogramBitmap.recycle();
        }
        
        if (pictogram.getTextLabel() != null) {
            //If the pictogram contains text
            this.pictogramText = new Text(1f, 1f, new TextView(context).getTextSize(), Align.CENTER); //A new TextView can give us the default text size
            this.pictogramText.loadText(gl, context, pictogram.getTextLabel());
        } 
        
        if (pictogram.getImagePath() == null && pictogram.getTextLabel() == null) {
            //If image and text wasn't present then create empty bitmap
            canvasBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
        
        super.loadTexture(gl, context, canvasBitmap);
        canvasBitmap.recycle();
    }
    
    /** 
     * Draw the pictogram.
     * @param gl         the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     */
    @Override
    public void draw(GL10 gl, Coordinate coordinate) {
        this.draw(gl, coordinate, Color.White);
    }
    
    /** 
     * Draw the pictogram with the specified RGBA color.
     * @param gl         the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @param color      a color overlay.
     */
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
                this.pictogramText.draw(gl, coordinate, Color.Black);
                gl.glTranslatef(-x, -y, 0f);
            } else {
                float x = super.getWidth() / 2f;
                float y = -((super.getHeight() / 2f) - this.pictogramText.getHeight() / 2f);
                
                gl.glTranslatef(x, y, 0f);
                this.pictogramText.draw(gl, coordinate, Color.Black);
                gl.glTranslatef(-x, -y, 0f);
            }
        }
    }
}
