package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public abstract class Positionable {
    
    private ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    
    public ArrayList<Coordinate> getCoordinates() {
        return this.coordinates;
    }
    
    public void addCoordinate(Coordinate coordinate) {
        this.coordinates.add(coordinate);
    }
    
    public void addCoordinate(float x, float y, float z) {
        this.coordinates.add(new Coordinate(x, y, z));
    }
    
    public abstract void draw(GL10 gl);
    
    public abstract void draw(GL10 gl, Color color);
    
    public interface Texture {
        public void loadTexture(GL10 gl, Context context, int resourcePointer);
    }
}
