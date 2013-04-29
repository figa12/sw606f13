package dk.aau.cs.giraf.train.opengl;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class Text extends Texture {
    
    private float textSize;
    private Align align;
    
    public Text(float width, float height, float textSize) {
        this(width, height, textSize, Align.CENTER);
    }
    
    public Text(float width, float height, float textSize, Align align) {
        super(width, height);
        this.textSize = textSize;
        this.align = align;
    }
    
    public void loadText(GL10 gl, Context context, String text) {
        //Create paint for drawing text
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setARGB(255, 255, 255, 255); //White. Set text color in draw method
        textPaint.setTextSize(this.textSize);
        textPaint.setTextAlign(Align.CENTER); //Note: We do not use this.align here
        
        super.setHeight(this.textSize);
        super.setWidth(textPaint.measureText(text));
        
        Bitmap textBitmap = Bitmap.createBitmap((int) textPaint.measureText(text), (int) this.textSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(textBitmap);
        
        //Draw the text. The y-coordinate specifies the vertical bottom of the text
        canvas.drawText(text, textBitmap.getWidth()/2f, textBitmap.getHeight()-1, textPaint); //Minus 1 because implementation is wierd
        
        super.loadTexture(gl, context, textBitmap);
        textBitmap.recycle();
    }
    
    @Override
    public void draw(GL10 gl, Coordinate coordinate) {
        this.draw(gl, coordinate, Color.White);
    }
    
    @Override
    public void draw(GL10 gl, Coordinate coordinate, Color color) {
        //Draw according to alignment option. Do a temporary alignment here.
        switch(this.align) {
        case CENTER:
            gl.glTranslatef(-super.getWidth()/2f, 0f, 0f);
            super.draw(gl, coordinate, color);
            gl.glTranslatef(super.getWidth()/2f, 0f, 0f);
            break;
        case LEFT:
            super.draw(gl, coordinate, color);
            break;
        case RIGHT:
            gl.glTranslatef(-super.getWidth(), 0f, 0f);
            super.draw(gl, coordinate, color);
            gl.glTranslatef(super.getWidth(), 0f, 0f);
            break;
        default:
            super.draw(gl, coordinate, color);
            break;
        }
    }
}
