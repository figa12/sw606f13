package dk.aau.cs.giraf.train.opengl;

/**
 * Simple class holding static {@link Color} objects.
 * Instead of creating new instances of colors all the time, simply reuse the same color from here.
 * @author Jesper
 * @see Color
 */
public class Colors {
    public final static Color White = new Color(1f, 1f, 1f, 1f);
    
    public final static Color Black = new Color(0f, 0f, 0f, 1f);
    
    public final static Color TransparentBlack = new Color(0, 0, 0, 25);
    
    public final static Color Red = new Color(1f, 0f, 0f, 1f);
    
    public final static Color Green = new Color(0f, 1f, 0f, 1f);
    
    public final static Color Blue = new Color(0f, 0f, 1f, 1f);
}
