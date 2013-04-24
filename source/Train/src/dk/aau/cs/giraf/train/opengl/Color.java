package dk.aau.cs.giraf.train.opengl;

/**
 * A simple color class that holds RGBA values.
 * This is designed for OpenGL, which uses values ranging between 0f and 1f.
 * @author Jesper Riemer Andersen
 */
public class Color {
    /* Already created color objects. */
    public final static Color White = new Color(1f, 1f, 1f, 1f);
    public final static Color Black = new Color(0f, 0f, 0f, 1f);
    public final static Color TransparentBlack = new Color(0f, 0f, 0f, 0.15f);
    public final static Color Red = new Color(1f, 0f, 0f, 1f);
    public final static Color Green = new Color(0f, 1f, 0f, 1f);
    public final static Color Blue = new Color(0f, 0f, 1f, 1f);
    public final static Color Window = new Color(0.5f, 0.5f, 0.5f, 0.5f); // (0.6941f, 0.6902f, 0.8431f, 0.65f);
    public final static Color DarkWeather = new Color(0f, 0f, 0.2f, 0.1f);
    public final static Color BackgroundTopColor = new Color(130, 147, 255, 255);
    public final static Color BackgroundBottomColor = new Color(1f, 1f, 1f, 1f);
    public final static Color PausedOverlay = new Color(0f, 0f, 0f, 0.5f);
    
    /* The color values. */
    public float red;
    public float green;
    public float blue;
    public float alpha;
    
    /** Sets color to white, without transparency */
    public Color() {
        this.setColor(1f, 1f, 1f, 1f);
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
    public Color(int red, int green, int blue, int alpha) {
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
