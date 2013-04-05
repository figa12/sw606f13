package dk.aau.cs.giraf.train.opengl.game;

public class GameData {
    
    public static final float maxTrainSpeed = 0.2f;
    public static float currentTrainVelocity = 0f; // pixels per ms
    public static float pixelMovementForThisFrame = 0f; // timeDifference*currentTrainSpeed //TODO better name.
    private static float totalDistanceTraveled = 0f;
    
    public static float timeDifference;
    public static long systemTimeLast = System.nanoTime();
    public static long systemTimeNow = 1;
    
    private static boolean changingVelocity = false;
    private static final float timeToBringToFullStop = 4000f; // ms. Brug konstant deceleration i stedet
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
            GameData.deltaVelocity = GameData.currentTrainVelocity / GameData.timeToBringToFullStop;
        }
    }
    
    private static float brakingStarted = 0f;
    
    public static final void decelerateTrain() {
        if(!GameData.changingVelocity) {
            GameData.brakingStarted = GameData.totalDistanceTraveled;
            GameData.changingVelocity = true;
            GameData.deltaVelocity = -GameData.currentTrainVelocity / GameData.timeToBringToFullStop;
        }
    }
    
    private static final void updateVelocity() {
        if(GameData.changingVelocity) {
            GameData.currentTrainVelocity += GameData.deltaVelocity * GameData.timeDifference;
            
            if(GameData.currentTrainVelocity <= 0f) {
                GameData.currentTrainVelocity = 0f;
                
                float temp = GameData.totalDistanceTraveled - GameData.brakingStarted;
                
                GameData.changingVelocity = false;
            }
            else if(GameData.currentTrainVelocity >= GameData.maxTrainSpeed) {
                GameData.currentTrainVelocity = GameData.maxTrainSpeed;
                GameData.changingVelocity = false;
            }
        }
    }
}
