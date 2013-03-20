package dk.aau.cs.giraf.train.profile;

import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		CustomListView listview = (CustomListView) findViewById(R.id.profilelist);
		listview.loadChildren();
	}
}
