package dk.aau.cs.giraf.train.opengl;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.widget.TextView;

/**
 * Possibility to write text in OpenGL.
 * The class is made to show constant text, not to change the text all the time.
 * Text is created by creating texture with text.
 * @author Jesper Riemer Andersen
 * @see Texture
 */
public class Text extends Texture {
    
    private float textSize;
    private Align align;
    
    /**
     * Creates {@link Text} instance, with a default left alignment.
     * @param width    initial width of the object. The width is changed by {@link Text#loadText(GL10, Context, String)}.
     * @param height   initial height of the object. The height is changed by {@link Text#loadText(GL10, Context, String)}.
     * @param textSize is the size of the text.
     * @see Align
     */
    public Text(float width, float height, float textSize) {
        this(width, height, textSize, Align.LEFT);
    }
    
    /**
     * Creates {@link Text} instance.
     * @param width    initial width of the object. The width is changed by {@link Text#loadText(GL10, Context, String)}.
     * @param height   initial height of the object. The height is changed by {@link Text#loadText(GL10, Context, String)}.
     * @param textSize is the size of the text.
     * @param align    is the text alignment.
     * @see Align
     */
    public Text(float width, float height, float textSize, Align align) {
        super(width, height);
        this.textSize = textSize;
        this.align = align;
    }
    
    /**
     * Load the specified text to a texture.
     * @param gl      the {@link GL10} instance.
     * @param context the current activity context.
     * @param text    the text to generate texture of.
     */
    public void loadText(GL10 gl, Context context, String text) {
        //Create paint for drawing text
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setARGB(255, 255, 255, 255); //White. Set text color in draw method
        textPaint.setTextSize(this.textSize);
        textPaint.setTextAlign(Align.CENTER); //Note: We do not use this.align here
        textPaint.setTypeface((new TextView(context)).getTypeface()); //Use default font
        
        super.setSize(textPaint.measureText(text), this.textSize);
        
        Bitmap textBitmap = Bitmap.createBitmap((int) textPaint.measureText(text), (int) this.textSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(textBitmap);
        
        //Draw the text. The y-coordinate specifies the vertical bottom of the text
        canvas.drawText(text, textBitmap.getWidth()/2f, textBitmap.getHeight()-1, textPaint); //Minus 1 because implementation is wierd
        
        super.loadTexture(gl, context, textBitmap);
        textBitmap.recycle();
    }
    
    /** 
     * Draw the text.
     * @param gl    the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @see #draw(GL10, Coordinate, Color)
     */
    @Override
    public void draw(GL10 gl, Coordinate coordinate) {
        this.draw(gl, coordinate, Color.White);
    }
    
    /** 
     * Draw the text with the specified RGBA color.
     * The text is drawn according to the alignment option specified in {@link Text#Text(float, float, float, Align)}.
     * @param gl    the {@link GL10} instance.
     * @param coordinate where the {@link Renderable} is being drawn.
     * @param color a color overlay.
     */
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
