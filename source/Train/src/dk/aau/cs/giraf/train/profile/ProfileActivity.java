package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;

import android.os.Bundle;
import android.app.Activity;

public class ProfileActivity extends Activity {
	Guardian guardian = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		/* Hardcoded game conficurations */
		GameConfiguration game1 = new GameConfiguration(this, "Game 1", 0, -3);
		game1.addStation(game1.new Station(game1.new Category(PictoFactory.INSTANCE.getPictogram(this, 0))));
		
		GameConfiguration game2 = new GameConfiguration(this, "Game 2", 1, -3);
		game2.addStation(game2.new Station(game2.new Category(PictoFactory.INSTANCE.getPictogram(this, 0))));
		
		GameConfiguration game3 = new GameConfiguration(this, "Game 3", 2, -3);
		game3.addStation(game3.new Station(game3.new Category(PictoFactory.INSTANCE.getPictogram(this, 0))));
		
		
		ArrayList<Art> artList = new ArrayList<Art>();//FIXME Is never used.
		
		/* Initialize the guardian object */
    	guardian = Guardian.getInstance(Data.currentChildID, Data.currentGuardianID, getApplicationContext(), artList);    	
    	guardian.backgroundColor = Data.appBackgroundColor;

		CustomListView listview = (CustomListView) findViewById(R.id.profilelist);
		listview.guardian = guardian;
		listview.loadChildren();
		
	}
}
