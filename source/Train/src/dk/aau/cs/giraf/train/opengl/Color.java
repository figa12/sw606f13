package dk.aau.cs.giraf.train.opengl;

/**
 * A simple color class that holds RGBA values.
 * This is designed for OpenGL, which uses values ranging between 0f and 1f.
 * @author Jesper
 */
public class Color {
    public float red;
    public float green;
    public float blue;
    public float alpha;
    
    /** Sets color to white, without transparency */
    public Color() {
        this.setColor(1f, 1f, 1f, 1f); // White
    }
    
    /**
     * RGBA values ranging between 0f and 1f.
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public Color(float red, float green, float blue, float alpha) {
        this.setColor(red, green, blue, alpha);
    }
    
    /**
     * RGBA values ranging between 0 and 255. The RGBA value is converted to a value between 0f and 1f.
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public Color(int red, int green, int blue, int alpha) { //TODO test this constructor
        this.setColor( (float) red   / 255f,
                       (float) green / 255f,
                       (float) blue  / 255f,
                       (float) alpha / 255f );
    }
    
    /**
     * Set the color. Values are between 0f and 1f.
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public void setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
}
