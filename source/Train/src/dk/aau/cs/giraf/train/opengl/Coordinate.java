package dk.aau.cs.giraf.train.opengl;

/**
 * Simple class holding a {@code x}, {@code y}, and {@code z} coordinate.
 * @author Jesper
 */
public class Coordinate {
    public float x;
    public float y;
    public float z;
    
    /**
     * Creates a new coordinate container with the specified coordinate.
     * @param x
     * @param y
     * @param z
     */
    public Coordinate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
