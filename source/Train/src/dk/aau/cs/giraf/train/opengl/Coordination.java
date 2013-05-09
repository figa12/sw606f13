package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;

/**
 * Implements methods for getting/setting coordinates and moving coordinates.
 * @author Jesper Riemer Andersen
 * @see Coordinate
 */
public class Coordination {
    
    /** The coordinates where this should be drawn. */
    private ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    
    /** Get the {@link #coordinates}.
     * @return List of coordinates. */
    public ArrayList<Coordinate> getCoordinates() {
        return this.coordinates;
    }
    
    /**
     * Add a coordinate.
     * @param coordinate
     * @see Coordinate
     * @see #addCoordinate(float, float, float)
     */
    public void addCoordinate(Coordinate coordinate) {
        this.coordinates.add(coordinate);
    }
    
    /**
     * Add a coordinate.
     * @param x
     * @param y
     * @param z
     * @see #addCoordinate(Coordinate)
     */
    public void addCoordinate(float x, float y, float z) {
        this.coordinates.add(new Coordinate(x, y, z));
    }
    
    /**
     * Get the coordinate at the specified index.
     * @param index of the coordinate.
     * @return The coordinate at the given index.
     */
    public Coordinate getCoordinate(int index) {
        return this.coordinates.get(index);
    }
    
    /**
     * Move all the {@link #coordinates} by the specified amount.
     * @param moveX
     * @param moveY
     * @see Coordinate
     */
    public void move(float moveX, float moveY) {
        for (int i = 0; i < this.coordinates.size(); i++) {
            this.getCoordinate(i).moveX(moveX);
            this.getCoordinate(i).moveY(moveY);
        }
    }
    
    /**
     * Move all the {@link #coordinates} by the specified amount.
     * @param moveX
     * @param moveY
     * @param moveZ
     * @see Coordinate
     */
    public void move(float moveX, float moveY, float moveZ) {
        for (int i = 0; i < this.coordinates.size(); i++) {
            this.getCoordinate(i).moveX(moveX);
            this.getCoordinate(i).moveY(moveY);
            this.getCoordinate(i).moveZ(moveZ);
        }
    }
}
