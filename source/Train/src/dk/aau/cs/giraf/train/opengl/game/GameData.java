package dk.aau.cs.giraf.train.opengl.game;

public class GameData {
    
    public static final float maxTrainSpeed = 0.325f;
    public static float currentTrainVelocity = 0f; // pixels per ms
    public static float pixelMovementForThisFrame = 0f; // timeDifference*currentTrainSpeed
    public static float totalDistanceTraveled = 0f;
    public static int numberOfStations = 4;
    public static float distanceBetweenStations = 10000f;
    
    public static float timeDifference;
    public static long systemTimeLast = System.nanoTime();
    public static long systemTimeNow = 1;
    
    private static boolean changingVelocity = false;
    private static final float timeToBringToFullStop = 4000f;
    private static float deltaVelocity = 0f;
    
    public static final void updateData() {
        GameData.timeDifference = (GameData.systemTimeNow - GameData.systemTimeLast)/1000000.0f; //FIXME follow the trace of this for the first drawn frame. It's dangerous.
        GameData.pixelMovementForThisFrame = -GameData.currentTrainVelocity * GameData.timeDifference;
        GameData.totalDistanceTraveled -= GameData.pixelMovementForThisFrame;
        
        GameData.updateVelocity();
    }
    
    public static final void accelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.changingVelocity = true;
            GameData.deltaVelocity = GameData.maxTrainSpeed / GameData.timeToBringToFullStop;
        }
    }
    
    public static final void decelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.changingVelocity = true;
            GameData.deltaVelocity = -GameData.maxTrainSpeed / GameData.timeToBringToFullStop;
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
    
    public static final float brakingDistance() {
        return (float) -Math.pow(GameData.currentTrainVelocity, 2.0)/2*GameData.deltaVelocity;
    }
    
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
