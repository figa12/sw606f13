package dk.aau.cs.giraf.train.opengl.game;

public class GameData {
    
    public static final float FOREGROUND = -907.7443f;
    public static final float MIDDLEGROUND = -1300f;
    public static final float BACKGROUND = -2500f;
    
    public static final float maxTrainSpeed = 0.325f; // pixels per ms
    public static float currentTrainVelocity = 0f; // pixels per ms
    public static float pixelMovementForThisFrame = 0f; // pixels
    public static float totalDistanceTraveled = 0f;
    public static int numberOfStations = 4;
    public static float distanceBetweenStations = 10000f; // pixel
    
    public static float timeDifference; // ms
    public static long systemTimeLast = System.nanoTime(); // ns
    public static long systemTimeNow = 1; // ns
    
    private static boolean changingVelocity = false;
    private static final float accelerationTime = 4000f; // ms
    private static float deltaVelocity = 0f; // pixels per ms^2
    
    /** Updates all game data. */
    public static final void updateData() {
        GameData.timeDifference = (GameData.systemTimeNow - GameData.systemTimeLast)/1000000.0f; //FIXME follow the trace of this for the first drawn frame. It's dangerous.
        GameData.pixelMovementForThisFrame = -GameData.currentTrainVelocity * GameData.timeDifference;
        GameData.totalDistanceTraveled -= GameData.pixelMovementForThisFrame;
        
        GameData.updateVelocity();
    }
    
    /** Initiate train acceleration. */
    public static final void accelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.changingVelocity = true;
            GameData.deltaVelocity = GameData.maxTrainSpeed / GameData.accelerationTime;
        }
    }
    
    /** Initiate train deceleration. */
    public static final void decelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.changingVelocity = true;
            GameData.deltaVelocity = -GameData.maxTrainSpeed / GameData.accelerationTime;
        }
    }
    
    private static final void updateVelocity() {
        if(GameData.changingVelocity) {
            GameData.currentTrainVelocity += GameData.deltaVelocity * GameData.timeDifference;
            
            if(GameData.currentTrainVelocity <= 0f) {
                GameData.currentTrainVelocity = 0f;
                GameData.changingVelocity = false;
            }
            else if(GameData.currentTrainVelocity >= GameData.maxTrainSpeed) {
                GameData.currentTrainVelocity = GameData.maxTrainSpeed;
                GameData.changingVelocity = false;
            }
        }
    }
    
    /** Calculate the train's braking distance. */
    public static final float brakingDistance() {
        return (float) -Math.pow(GameData.currentTrainVelocity, 2.0)/2*GameData.deltaVelocity;
    }
    
    /** Reset all game data to its start conditions. */
    public static final void resetGameData() { // recreate start conditions
        GameData.currentTrainVelocity = 0f;
        GameData.pixelMovementForThisFrame = 0f;
        GameData.totalDistanceTraveled = 0f;
        GameData.systemTimeLast = System.nanoTime();
        GameData.systemTimeNow = 1;
        GameData.changingVelocity = false;
        GameData.deltaVelocity = 0f;
    }
}
