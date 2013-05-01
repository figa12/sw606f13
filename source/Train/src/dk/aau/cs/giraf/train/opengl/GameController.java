package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import java.util.Iterator;

import android.view.View;
import android.webkit.WebView.PictureListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.opengl.game.GameData;
import dk.aau.cs.giraf.train.profile.GameConfiguration;
import dk.aau.cs.giraf.train.profile.StationConfiguration;

public class GameController {

	public GameActivity gameActivity;
	private GameConfiguration gameConfiguration;
	public boolean IsTrainStopped;
	
	public GameController(GameActivity gameActivity, GameConfiguration gameConfiguration){
		this.gameActivity = gameActivity;
		this.gameConfiguration = gameConfiguration;
	}
	
	
	
	private boolean checkPictogramsIsOnStation(StationConfiguration station, ArrayList<StationLinearLayout> stationLinear){
		boolean answer = false;
		int acceptedPics = 0;
		int totalPictogramsOnStaion = 0;
		for (StationLinearLayout stationLin : stationLinear) {
			totalPictogramsOnStaion += stationLin.getPictograms().size();
		}
		
		for (Long pictoId : station.getAcceptPictograms()){
			boolean foundPic = false;
			for(StationLinearLayout stationLin : stationLinear){
				for (PictoFrameLayout pictoFrame : stationLin.getPictoframes()) {	
					if(pictoFrame.getChildCount() > 0){
						if(pictoId == pictoFrame.getPictogram().getPictogramID() && pictoFrame.getPictogram().getTag() != "found"){		
							acceptedPics++;
							pictoFrame.getPictogram().setTag("found");
							foundPic = true;
							break;
						}
					}
					
				}	
				if(foundPic == true){
					break;
				}
			}
		}
		
		if(acceptedPics == station.getAcceptPictograms().size() && station.getAcceptPictograms().size() == totalPictogramsOnStaion ){
			answer = true;
		}
		else{
			for (StationLinearLayout stationLin : stationLinear) {
				for (PictoFrameLayout pictoFrame : stationLin.getPictoframes()) {
					if(pictoFrame.getChildCount() > 0){
						pictoFrame.getPictogram().setTag(null);
					}
				}
			}
		}
		
		return answer;
	}
	
	public void trainDrive(ArrayList<StationLinearLayout> stationLinear){
		if(GameData.currentTrainVelocity == 0f && GameData.numberOfStops < GameData.numberOfStations + 1) {//pga. remise
			boolean readyToGo = true;
			if(GameData.numberOfStops + 1 == 1){
				for (LinearLayout lin : stationLinear) {
					for (int i = 0; i< lin.getChildCount();i++) {
						FrameLayout frame = (FrameLayout)lin.getChildAt(i);
						if(frame.getChildAt(0) != null){
							readyToGo = false;
						}
					}
				}
			}
			else {
				//check if it is the correct pictogram on the right station.
				if(checkPictogramsIsOnStation(gameConfiguration.getStation(GameData.numberOfStops - 1), stationLinear) ==  false){
					readyToGo = false;
				}
			}
			
			if(readyToGo){
				//Draw pictograms with opengl
				gameActivity.HideLinearLayouts();
				
				gameActivity.deletePictogramsFromStation();
				
				gameActivity.getGameData().accelerateTrain();
				
				GameActivity.streamId = GameActivity.soundPool.play(GameActivity.sound, 1f, 1f, 0, 0, 0.5f);
			}
        }
	}

	
	public void TrainIsStopping(){
		gameActivity.ShowLinearLayouts();
		IsTrainStopped = true;
	}
	
	
}
