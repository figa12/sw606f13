package dk.aau.cs.giraf.train.opengl;

public class Color {
    public float red;
    public float green;
    public float blue;
    public float alpha;
    
    public Color() {
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
    }
    
    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    //TODO test this constructor
    public Color(int red, int green, int blue, int alpha) {
        this(   (float) red / 255f,
                (float) green / 255f,
                (float) blue / 255f,
                (float) alpha / 255f);
    }
}
