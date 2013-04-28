package dk.aau.cs.giraf.train.opengl.game;

import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.opengl.GameActivity;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
import dk.aau.cs.giraf.train.profile.GameConfiguration;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Data relevant for the game. Everything is synchronized and volatile since the GameData can be accessed from OpenGL thread and GUI thread.
 * @author Jesper Riemer Andersen
 */
public class GameData {
    
    public static final float FOREGROUND   = -907.7443f;
    public static final float MIDDLEGROUND = -1800f;
    public static final float BACKGROUND   = -3000f;
    
    public boolean isPaused = false;
    
    public static final float MAX_TRAIN_SPEED = 1.5f; // pixels per ms // 0.35 is nice
    public volatile static float currentTrainVelocity = 0f; // pixels per ms
    
    public volatile float pixelMovementForThisFrame = 0f; // pixels
    public volatile float totalDistanceTraveled = 0f;
    
    public volatile static int numberOfStations;
    public static final float DISTANCE_BETWEEN_STATIONS = 12000f; // pixel
    public static final float DISTANCE_TO_DEPOT = 5000f; // pixel 
    public volatile static int numberOfStops = 0;
    
    public volatile float timeDifference; // ms
    public volatile long systemTimeLast = System.nanoTime(); // ns
    public volatile long systemTimeNow = 1; // ns
    
    private static boolean changingVelocity = false;
    private static final float ACCELERATION_TIME = 5000f; // ms
    private static float deltaVelocity = GameData.MAX_TRAIN_SPEED / GameData.ACCELERATION_TIME; // pixels per ms^2
    public volatile float[] nextStoppingPosition;
    
    private GameConfiguration gameConfiguration;
    private GameActivity gameActivity;
    private Station station;
    private Train train;
    
    /**
     * Set up game data for the game.
     * @param gameConfiguration is the current game configurations.
     * @see GameConfiguration
     */
    public GameData(GameConfiguration gameConfiguration, Context gameActivityContext) {
        this.gameConfiguration = gameConfiguration;
        GameData.numberOfStations = gameConfiguration.getStations().size() + 1;
        this.gameActivity = (GameActivity)gameActivityContext;
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
        this.station.setPictograms(stationIndex, pictograms);
    }
    
    /**
     * Set the pictograms to be drawn on the wagon.
     * The size of the input array should be either 4 or 6 as a one-to-one correspondence between the slots available.
     * Pictogram indexes where there shouldn't be a pictogram should have null values.
     * @param pictograms the array of pictograms.
     */
    public synchronized final void setWagonPictograms(Pictogram[] pictograms) {
        this.train.setWagonPictograms(pictograms);
    }
    
    /**
     * Set the pictogram to be used as the driver.
     * @param pictogram is the driver pictogram.
     */
    public synchronized final void setDriverPictogram(Pictogram pictogram) {
        this.train.setDriverPictogram(pictogram);
    }
    
    /** Updates all game data. */
    public synchronized final void updateData() {
        this.timeDifference = (this.systemTimeNow - this.systemTimeLast)/1000000.0f;
        
        //Limit timeDifference from game freezes
        if(this.timeDifference > 500f) {
            this.timeDifference = 500f;
        }
        
        //if we are within braking distance of our next stop, then start braking
        if(!GameData.changingVelocity && this.nextStoppingPosition[GameData.numberOfStops] - this.brakingDistance() <= this.totalDistanceTraveled && !this.isPaused) {
            this.decelerateTrain();
        }
        
        this.updateVelocity();
    }
    
    /** Initiate train acceleration. */
    public synchronized static final void accelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.changingVelocity = true;
            GameData.deltaVelocity = Math.abs(GameData.deltaVelocity);
        }
    }
    
    /** Initiate train deceleration. */
    private synchronized final void decelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.changingVelocity = true;
            GameData.deltaVelocity = -Math.abs(GameData.deltaVelocity);
        }
    }
    
    /** Get the braking distance for the train at max velocity. */
    private synchronized final float brakingDistance() {
        return (float) (-Math.pow(GameData.MAX_TRAIN_SPEED, 2.0)) / (2 * -Math.abs(GameData.deltaVelocity));
    }
    
    /** Change the velocity, and update data accordingly. */
    private synchronized final void updateVelocity() {
        this.performAcceleration();
        
        this.pixelMovementForThisFrame = -GameData.currentTrainVelocity * this.timeDifference;
        
        //if we have reached our next stop or are about to go too far, then make sure the train stops at the exact position
        if(this.totalDistanceTraveled + (-this.pixelMovementForThisFrame) >= this.nextStoppingPosition[GameData.numberOfStops]) {
            this.pixelMovementForThisFrame = -(this.nextStoppingPosition[GameData.numberOfStops] - this.totalDistanceTraveled);
            GameData.currentTrainVelocity = 0f;
            GameData.numberOfStops++;
            GameData.changingVelocity = false;
                        //make flute visble
            GameActivity.fluteButton.post(new Runnable() { 
				
				@Override
				public void run() {	
					gameActivity.trainDrive(false);
				}
			});//TODO investigate if this is the right to do it.
        }
        
        this.totalDistanceTraveled -= this.pixelMovementForThisFrame;
    }
    
    /** If we are currently chaing velocity, then either accelerate or decelerate the velocity. */
    private synchronized final void performAcceleration() {
        if(GameData.changingVelocity) {
            // if accelerating
            if (GameData.deltaVelocity > 0) {
                GameData.currentTrainVelocity += GameData.deltaVelocity * this.timeDifference;
                
                if(GameData.currentTrainVelocity >= GameData.MAX_TRAIN_SPEED) {
                    GameData.currentTrainVelocity = GameData.MAX_TRAIN_SPEED;
                    GameData.changingVelocity = false;
                }
            }
            // if decelerating
            else if (GameData.deltaVelocity < 0) {
                GameData.currentTrainVelocity += GameData.deltaVelocity * this.timeDifference;
                
                //minimum reachable velocity. We need a little velocity to get to the nextStoppingPosition
                if(GameData.currentTrainVelocity <= 0.01f) {
                    GameData.currentTrainVelocity = 0.01f;
                }
            }
        }
    }
    
    /**
     * Get the total visible travel distance for this game sessions.
     * @param depth to calculate in.
     * @return Visible travel distance.
     */
    public final float getTotalTravelDistance(float depth) {
        return this.nextStoppingPosition[GameData.numberOfStations-1] + GlRenderer.getActualWidth(GlRenderer.getActualHeight(depth));
    }
    
    private static final String CURRENT_TRAIN_VELOCITY  = "currentTrainCelocity";
    private static final String CHANGING_VELOCITY       = "changingVelocity";
    //private static final String TOTAL_DISTANCE_TRAVELED = "totalDistanceTraveled";
    //private static final String NUMBER_OF_STOPS         = "numberOfStops";
    //private static final String NEXT_STOPPING_POSITION  = "nextStoppingPosition";
    
    private static Bundle bundle;
    
    /** Pause the game. */
    public synchronized final void onPause() {
        this.isPaused = true;
        
        //Save the current instance state
        GameData.bundle = new Bundle();
        GameData.bundle.putFloat(GameData.CURRENT_TRAIN_VELOCITY, GameData.currentTrainVelocity);
        GameData.bundle.putBoolean(GameData.CHANGING_VELOCITY, GameData.changingVelocity);
        
        //Then pause the GameData
        GameData.changingVelocity     = false;
        GameData.currentTrainVelocity = 0f;
    }
    
    /** Resume the game. */
    public synchronized final void onResume() {
        if(GameData.bundle != null) {
            //Restore instance state
            GameData.changingVelocity     = GameData.bundle.getBoolean(GameData.CHANGING_VELOCITY);
            GameData.currentTrainVelocity = GameData.bundle.getFloat(GameData.CURRENT_TRAIN_VELOCITY);
        }
        
        this.isPaused = false;
        GameData.bundle   = null; //Free memory
    }
    
    /** Reset all game data to its start conditions. */
    public synchronized final void resetGameData() {
        GameData.currentTrainVelocity = 0f;
        this.pixelMovementForThisFrame = 0f;
        this.totalDistanceTraveled = 0f;
        this.systemTimeLast = System.nanoTime();
        this.systemTimeNow = 1;
        GameData.changingVelocity = false;
        GameData.numberOfStops = 0;
        this.isPaused = false;
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
            if(this.nextStoppingPosition[i] - this.nextStoppingPosition[i-1] < this.brakingDistance()*2) {
                Log.w(GameData.class.getSimpleName(), "Stopping positions are too close together, train will perform sudden stop(s).");
                break;
            }
        }
    }
}
