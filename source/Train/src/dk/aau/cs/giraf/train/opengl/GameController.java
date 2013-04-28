package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.profile.StationConfiguration;

public class GameController {

	public boolean checkPictogramsIsOnStation(StationConfiguration station, ArrayList<LinearLayout> stationLinear){
		boolean answer = false;
		int acceptedPics = 0;
		
		for (LinearLayout lin : stationLinear) {
			for (int i = 0; i < lin.getChildCount(); i++) {
				if(((FrameLayout)lin.getChildAt(i)).getChildCount() > 0){
					boolean foundAccPic = false;
					for (Long pictogramId : station.getAcceptPictograms()) {
						if(pictogramId == ((Pictogram)((FrameLayout)lin.getChildAt(i)).getChildAt(0)).getPictogramID() ){
							foundAccPic = true;
							acceptedPics++;
						}
					}
					
					if(foundAccPic == false){
						answer = false;
						return answer;
					}
				}
			}
		}
		if(acceptedPics == station.getAcceptPictograms().size()){
			answer = true;
		}
		return answer;
	}
}
