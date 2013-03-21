package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class ProfileActivity extends Activity {
	Guardian guardian = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		ArrayList<Art> artList = new ArrayList<Art>();//FIXME Is never used.
		
		/* Initialize the guardian object */
    	guardian = Guardian.getInstance(Data.currentChildID, Data.currentGuardianID, getApplicationContext(), artList);    	
    	guardian.backgroundColor = Data.appBackgroundColor;

		CustomListView listview = (CustomListView) findViewById(R.id.profilelist);
		listview.guardian = guardian;
		listview.loadChildren();
		
	}
}
