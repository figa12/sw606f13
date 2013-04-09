package dk.aau.cs.giraf.train.opengl.game;

import android.os.Bundle;

/**
 * This class holds static data relevant for the game.
 * @author Jesper
 * @see GameDrawer
 */
public class GameData {
    
    public static final float FOREGROUND = -907.7443f;
    public static final float MIDDLEGROUND = -2500f;
    public static final float BACKGROUND = -3000f;
    
    public static boolean isPaused = false;
    
    public static final float maxTrainSpeed = 0.35f; // pixels per ms // 0.325 is nice
    public static float currentTrainVelocity = 0f; // pixels per ms
    
    public static float pixelMovementForThisFrame = 0f; // pixels
    public static float totalDistanceTraveled = 0f;
    
    public static int numberOfStations = 4;
    public static float distanceBetweenStations = 10000f; // pixel
    public static int numberOfStops = 0;
    
    public static float timeDifference; // ms
    public static long systemTimeLast = System.nanoTime(); // ns
    public static long systemTimeNow = 1; // ns
    
    private static boolean changingVelocity = false;
    private static final float accelerationTime = 5000f; // ms
    private static float deltaVelocity = GameData.maxTrainSpeed / GameData.accelerationTime; // pixels per ms^2
    public static float[] nextStoppingPosition = new float[GameData.numberOfStations];
    
    /** Updates all game data. */
    public static final void updateData() {
        GameData.timeDifference = (GameData.systemTimeNow - GameData.systemTimeLast)/1000000.0f;
        
        //Limit timeDifference from game freezes
        if(GameData.timeDifference > 1000f) {
            GameData.timeDifference = 1000f;
        }
        
        //if we are within braking distance of our next stop, then start braking
        if(!GameData.changingVelocity && GameData.nextStoppingPosition[GameData.numberOfStops] - GameData.brakingDistance() <= GameData.totalDistanceTraveled) {
            GameData.decelerateTrain();
        }
        
        GameData.updateVelocity();
    }
    
    /** Initiate train acceleration. */
    public static final void accelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.changingVelocity = true;
            GameData.deltaVelocity = Math.abs(GameData.deltaVelocity);
        }
    }
    
    /** Initiate train deceleration. */
    private static final void decelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.changingVelocity = true;
            GameData.deltaVelocity = -Math.abs(GameData.deltaVelocity);
        }
    }
    
    private static final float brakingDistance() {
        return (float) (-Math.pow(GameData.maxTrainSpeed, 2.0)) / (2 * -Math.abs(GameData.deltaVelocity));
    }
    
    private static final void updateVelocity() {
        if(GameData.changingVelocity) {
            
            // if accelerating
            if (GameData.deltaVelocity > 0) {
                GameData.currentTrainVelocity += GameData.deltaVelocity * GameData.timeDifference;
                
                if(GameData.currentTrainVelocity >= GameData.maxTrainSpeed) {
                    GameData.currentTrainVelocity = GameData.maxTrainSpeed;
                    GameData.changingVelocity = false;
                }
            }
            // if decelerating
            else if (GameData.deltaVelocity < 0) {
                GameData.currentTrainVelocity += GameData.deltaVelocity * GameData.timeDifference;
                
                //minimum reachable velocity. We need a little velocity to get to the nextStoppingPosition
                if(GameData.currentTrainVelocity <= 0.0065f) {
                    GameData.currentTrainVelocity = 0.0065f;
                }
            }
        }
        
        //update the pixel movement
        GameData.pixelMovementForThisFrame = -GameData.currentTrainVelocity * GameData.timeDifference;
        
        //if we have reached our next stop or are about to go too far, then make sure the train stops at the exact position
        if(GameData.totalDistanceTraveled + (-GameData.pixelMovementForThisFrame) >= GameData.nextStoppingPosition[GameData.numberOfStops]) {
            GameData.pixelMovementForThisFrame = -(GameData.nextStoppingPosition[GameData.numberOfStops] - GameData.totalDistanceTraveled);
            GameData.currentTrainVelocity = 0f;
            GameData.changingVelocity = false;
            GameData.numberOfStops++;
        }
        
        //update trip computer
        GameData.totalDistanceTraveled -= GameData.pixelMovementForThisFrame;
    }
    
    /** Reset all game data to its start conditions. */
    public static final void resetGameData() {
        GameData.currentTrainVelocity = 0f;
        GameData.pixelMovementForThisFrame = 0f;
        GameData.totalDistanceTraveled = 0f;
        GameData.systemTimeLast = System.nanoTime();
        GameData.systemTimeNow = 1;
        GameData.changingVelocity = false;
        GameData.numberOfStops = 0;
        GameData.nextStoppingPosition = new float[GameData.numberOfStations];
        GameData.isPaused = false;
    }
    
    private static final String CURRENT_TRAIN_VELOCITY  = "currentTrainCelocity";
    private static final String TOTAL_DISTANCE_TRAVELED = "totalDistanceTraveled";
    private static final String CHANGING_VELOCITY       = "changingVelocity";
    private static final String NUMBER_OF_STOPS         = "numberOfStops";
    private static final String NEXT_STOPPING_POSITION  = "nextStoppingPosition";
    
    private static Bundle bundle;
    
    /** Pause the game. */
    public static final void onPause() {
        GameData.isPaused = true;
        GameData.onSaveInstanceState(new Bundle()); //Save this GameData state
        
        //Then pause the GameData
        GameData.currentTrainVelocity  = 0f;
        GameData.changingVelocity      = false;
    }
    
    /** Resume the game. First resume when the user is ready. */
    public static final void onResume() {
        if(GameData.bundle == null) {
            return;
        }
        
        // Restore state each time we resume
        GameData.currentTrainVelocity  = GameData.bundle.getFloat(GameData.CURRENT_TRAIN_VELOCITY);
        GameData.totalDistanceTraveled = GameData.bundle.getFloat(GameData.TOTAL_DISTANCE_TRAVELED);
        GameData.systemTimeLast        = System.nanoTime(); // the old value of systemTimeLast is useless now, get new
        GameData.changingVelocity      = GameData.bundle.getBoolean(GameData.CHANGING_VELOCITY);
        GameData.numberOfStops         = GameData.bundle.getInt(GameData.NUMBER_OF_STOPS);
        GameData.nextStoppingPosition  = GameData.bundle.getFloatArray(NEXT_STOPPING_POSITION);
        GameData.isPaused              = false;
    }
    
    /**
     * Save this instance of game data.
     * @param savedInstanceState
     */
    public static final void onSaveInstanceState(Bundle savedInstanceState) {
        GameData.bundle = savedInstanceState;
        savedInstanceState.putFloat(GameData.CURRENT_TRAIN_VELOCITY, GameData.currentTrainVelocity);
        savedInstanceState.putFloat(GameData.TOTAL_DISTANCE_TRAVELED, GameData.totalDistanceTraveled);
        savedInstanceState.putBoolean(GameData.CHANGING_VELOCITY, GameData.changingVelocity);
        savedInstanceState.putInt(GameData.NUMBER_OF_STOPS, GameData.numberOfStops);
        savedInstanceState.putFloatArray(GameData.NEXT_STOPPING_POSITION, GameData.nextStoppingPosition);
    }
    
    /**
     * Restore this instance of game data.
     * @param savedInstanceState
     */
    public static final void onRestoreInstanceState(Bundle savedInstanceState) {
        GameData.bundle = new Bundle(savedInstanceState); // override old GameDate.bundle because this is the relevant one
        
        /* After this, GameData.onResume() is called, this is where the state is restored */
    }
}
