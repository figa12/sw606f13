package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
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
		
		long guardianId;
		long childId;
		int color;
		
		/* Get data from launcher */
		Bundle extras = getIntent().getExtras();
		if (extras != null) {        	
        	guardianId = extras.getLong("currentGuardianID");
        	childId = extras.getLong("currentChildID");
        	color = extras.getInt("appBackgroundColor");
        } else {
        	guardianId = -1;
        	childId = -3;
        	color = 0xFFFFBB55;
        }
		
		ArrayList<Art> artList = new ArrayList<Art>();//FIXME Is never used.
		
		/* Initialize the guardian object */
    	guardian = Guardian.getInstance(childId, guardianId, getApplicationContext(), artList);    	
    	guardian.backgroundColor = color;

		CustomListView listview = (CustomListView) findViewById(R.id.profilelist);
		listview.guardian = guardian;
		listview.loadChildren();
		
	}
}
