package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public abstract class Renderable {
    
    public interface Texture {
        public void loadTexture(GL10 gl, Context context, int resourcePointer);
    }
    
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
    
    public void move(float moveX, float moveY, float moveZ) {
        for (Coordinate coordinate : this.coordinates) {
            coordinate.x += moveX;
            coordinate.y += moveY;
            coordinate.z += moveZ;
        }
    }
    
    public abstract void draw(GL10 gl);
    
    public abstract void draw(GL10 gl, Color color);
}
