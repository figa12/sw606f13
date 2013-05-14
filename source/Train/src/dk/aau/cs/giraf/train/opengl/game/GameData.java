package dk.aau.cs.giraf.train.opengl.game;

import java.util.ArrayList;

import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.GameConfiguration;
import dk.aau.cs.giraf.train.opengl.GameActivity;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
import android.os.Bundle;
import android.util.Log;

/**
 * Data relevant for the game. Everything is synchronized and volatile since the GameData can be accessed from OpenGL thread and GUI thread.
 * @author Jesper Riemer Andersen
 */
public class GameData {
    
    private static final float FUNNY_NUMBER = 0f; //TODO delete
    
    public static final float FOREGROUND   = -907.7442994522836f - GameData.FUNNY_NUMBER; //Magic number
    public static final float MIDDLEGROUND = -1800f              - GameData.FUNNY_NUMBER;
    public static final float BACKGROUND   = -3000f              - GameData.FUNNY_NUMBER;
    
    public volatile boolean isPaused = false;
    
    public static final float MAX_TRAIN_SPEED = 0.35f; // pixels per ms // 0.35 is nice
    public volatile float currentTrainVelocity = 0f; // pixels per ms
    
    private float pixelMovementForThisFrame = 0f; // pixels
    public volatile float totalDistanceTraveled = 0f;
    
    public volatile int numberOfStations;
    public static final float DISTANCE_BETWEEN_STATIONS = 12000f; // pixel
    public static final float DISTANCE_TO_DEPOT = 5000f; // pixel 
    public volatile int numberOfStops = 0;
    
    public volatile float timeDifference; // ms
    public volatile long systemTimeLast = System.nanoTime(); // ns
    public volatile long systemTimeNow = 1; // ns
    
    private boolean changingVelocity = false;
    private static final float ACCELERATION_TIME = 5000f; // ms
    private float deltaVelocity = GameData.MAX_TRAIN_SPEED / GameData.ACCELERATION_TIME; // pixels per ms^2
    public volatile float[] nextStoppingPosition;
    
    private GameActivity gameActivity;
    private GameConfiguration gameConfiguration;
    private Station station;
    private Train train;
    
    /**
     * Set up game data for the game.
     * @param gameConfiguration is the current game configurations.
     * @see GameConfiguration
     */
    public GameData(GameActivity gameActivity, GameConfiguration gameConfiguration) {
        this.gameActivity = gameActivity;
        this.gameConfiguration = gameConfiguration;
        this.numberOfStations = gameConfiguration.getStations().size() + 1;
    }
    
    /** Get this game's configuration.
     * @see GameConfiguration */
    public GameConfiguration getGameConfiguration() {
        return this.gameConfiguration;
    }
    
    /**
     * Save the reference of the station and train {@link GameDrawable}s.
     * @param station 
     * @param train
     * @see GameDrawer
     */
    public synchronized final void bindGameDrawables(Station station, Train train) {
        //Bind references to GameData to allow protected access to station and train
        this.station = station;
        this.train = train;
    }
    
    /**
     * Set the pictograms to be drawn on the station.
     * The size of the input array should be either 4 or 6 as a one-to-one correspondence between the slots available.
     * Pictogram indexes where there shouldn't be a pictogram should have null values.
     * @param pictograms the array of pictograms.
     */
    public synchronized final void setStationPictograms(int stationIndex, Pictogram[] pictograms) {
        this.station.loadStationPictograms(stationIndex, pictograms);
    }
    
    /**
     * Set the pictogram to be used as the driver.
     * @param pictogram is the driver pictogram.
     */
    public synchronized final void setDriverPictogram(Pictogram pictogram) {
        this.train.loadTrainDriverPictogram(pictogram);
    }
    
    public synchronized final float getPixelMovement() {
        return this.pixelMovementForThisFrame;
    }
    
    /** Updates all game data. */
    public synchronized final void updateData() {
        this.timeDifference = (this.systemTimeNow - this.systemTimeLast)/1000000.0f;
        
        //Limit timeDifference from game freezes
        if (this.timeDifference > 500f) {
            this.timeDifference = 500f;
        }
        
        //if we are within braking distance of our next stop, then start braking
        if (!this.changingVelocity && this.nextStoppingPosition[this.numberOfStops] - this.brakingDistance() <= this.totalDistanceTraveled && !this.isPaused) {
            this.decelerateTrain();
        }
        
        this.updateVelocity();
    }
    
    /** Initiate train acceleration. */
    public synchronized final void accelerateTrain() {
        if (!this.changingVelocity) {
            this.changingVelocity = true;
            this.deltaVelocity = Math.abs(this.deltaVelocity);
        }
    }
    
    /** Initiate train deceleration. */
    public synchronized final void decelerateTrain() {
        if (!this.changingVelocity) {
            this.changingVelocity = true;
            this.deltaVelocity = -Math.abs(this.deltaVelocity);
        }
    }
    
    /** Get the braking distance for the train at max velocity. */
    private synchronized final float brakingDistance() {
        return (float) (-Math.pow(GameData.MAX_TRAIN_SPEED, 2.0)) / (2 * -Math.abs(this.deltaVelocity));
    }
    
    /** Change the velocity, and update data accordingly. */
    private synchronized final void updateVelocity() {
        this.performAcceleration();
        
        this.pixelMovementForThisFrame = -this.currentTrainVelocity * this.timeDifference;
        
        //if we have reached our next stop or are about to go too far, then make sure the train stops at the exact position
        if (this.totalDistanceTraveled + (-this.pixelMovementForThisFrame) >= this.nextStoppingPosition[this.numberOfStops]) {
            this.pixelMovementForThisFrame = -(this.nextStoppingPosition[this.numberOfStops] - this.totalDistanceTraveled);
            this.currentTrainVelocity = 0f;
            this.numberOfStops++;
            this.changingVelocity = false;
            
            this.gameActivity.fluteButton.post(new Runnable() {
                @Override
                public void run() {
                    GameData.this.gameActivity.onTrainStopEvent();
                }
            });
        }
        
        this.totalDistanceTraveled -= this.pixelMovementForThisFrame;
    }
    
    /** If we are currently chaing velocity, then either accelerate or decelerate the velocity. */
    private synchronized final void performAcceleration() {
        if (this.changingVelocity) {
            // if accelerating
            if (this.deltaVelocity > 0) {
                this.currentTrainVelocity += this.deltaVelocity * this.timeDifference;
                
                if (this.currentTrainVelocity >= GameData.MAX_TRAIN_SPEED) {
                    this.currentTrainVelocity = GameData.MAX_TRAIN_SPEED;
                    this.changingVelocity = false;
                }
            }
            // if decelerating
            else if (this.deltaVelocity < 0) {
                this.currentTrainVelocity += this.deltaVelocity * this.timeDifference;
                
                //minimum reachable velocity. We need a little velocity to get to the nextStoppingPosition
                if (this.currentTrainVelocity <= 0.01f) {
                    this.currentTrainVelocity = 0.01f;
                }
            }
        }
    }
    
    /**
     * Get the total visible travel distance for this game sessions.
     * @param depth to calculate in.
     * @return Visible travel distance.
     */
    public synchronized final float getTotalTravelDistance(float depth) {
        return this.nextStoppingPosition[this.numberOfStations-1] + GlRenderer.getActualWidth(GlRenderer.getActualHeight(depth));
    }
    
    private static final String CURRENT_TRAIN_VELOCITY  = "currentTrainCelocity";
    private static final String CHANGING_VELOCITY       = "changingVelocity";
    //private static final String TOTAL_DISTANCE_TRAVELED = "totalDistanceTraveled";
    //private static final String NUMBER_OF_STOPS         = "numberOfStops";
    //private static final String NEXT_STOPPING_POSITION  = "nextStoppingPosition";
    
    private Bundle bundle;
    
    /** Pause the game. */
    public synchronized final void onPause() {
        this.isPaused = true;
        
        //Save the current instance state
        this.bundle = new Bundle();
        this.bundle.putFloat(GameData.CURRENT_TRAIN_VELOCITY, this.currentTrainVelocity);
        this.bundle.putBoolean(GameData.CHANGING_VELOCITY, this.changingVelocity);
        
        //Then pause the GameData
        this.changingVelocity     = false;
        this.currentTrainVelocity = 0f;
    }
    
    /** Resume the game. */
    public synchronized final void onResume() {
        if (this.bundle != null) {
            //Restore instance state
            this.changingVelocity     = this.bundle.getBoolean(GameData.CHANGING_VELOCITY);
            this.currentTrainVelocity = this.bundle.getFloat(GameData.CURRENT_TRAIN_VELOCITY);
        }
        
        this.isPaused = false;
        this.bundle   = null; //Free memory
    }
    
    /** 
     * Remove the reference to the station and the train object to allow garbage collections.
     * @see #bindGameDrawables(Station, Train) */
    public synchronized final void freeMemory() {
        this.station = null;
        this.train = null;
    }
    
    /** 
     * Output warning if we are sure that the stopping positions are too close to each other.
     * It will cause the train to perform sudden stops rather than smooth ones. */
    public synchronized final void performStoppingPositionsCheck() {
        /* The braking distance is also the distance traveled to perform full acceleration
         * There need to be at least brakingDistance()*2 between stops to perform full smooth acceleration
         * Even if we do not get a warning here, there is still no guarantee that we will get smooth stops,
         * since everything is based on time. */
        for (int i = 1; i < this.nextStoppingPosition.length; i++) {
            if (this.nextStoppingPosition[i] - this.nextStoppingPosition[i-1] < this.brakingDistance()*2) {
                Log.w(GameData.class.getSimpleName(), "Stopping positions are too close together, train will perform sudden stop(s).");
                break;
            }
        }
    }
}
