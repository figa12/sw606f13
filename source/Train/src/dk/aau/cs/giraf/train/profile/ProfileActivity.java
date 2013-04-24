package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class ProfileActivity extends Activity {
	private Guardian guardian = null;
	private Intent intent = new Intent(Intent.ACTION_MAIN);
	private CustomiseLinearLayout customiseLinearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_profile);
		
		/* Hardcoded game configurations */
		GameConfiguration game1 = new GameConfiguration(this, "Game 1", 0, -3);
		game1.addStation(game1.new Station(0));
		
		GameConfiguration game2 = new GameConfiguration(this, "Game 2", 1, -3);
		game2.addStation(game2.new Station(0));
		
		GameConfiguration game3 = new GameConfiguration(this, "Game 3", 2, -3);
		game3.addStation(game3.new Station(0));
		
		
		ArrayList<Art> artList = new ArrayList<Art>();//FIXME Is never used.
		
		/* Initialize the guardian object */
    	this.guardian = Guardian.getInstance(Data.currentChildID, Data.currentGuardianID, getApplicationContext(), artList);    	
    	this.guardian.backgroundColor = Data.appBackgroundColor;

		ChildrenListView listview = (ChildrenListView) super.findViewById(R.id.profilelist);
		listview.guardian = this.guardian;
		listview.loadChildren();
		
		
	    this.intent.setComponent(new ComponentName("dk.aau.cs.giraf.pictoadmin","dk.aau.cs.giraf.pictoadmin.PictoAdminMain"));
	    
	    Drawable d = getResources().getDrawable(R.drawable.background);
		d.setColorFilter(Data.appBackgroundColor, PorterDuff.Mode.OVERLAY);
		super.findViewById(R.id.mainProfileLayout).setBackgroundDrawable(d);
	    
		this.customiseLinearLayout = (CustomiseLinearLayout) super.findViewById(R.id.customiseLinearLayout);
		
		Button addStationButton = (Button) super.findViewById(R.id.addStationButton);
		addStationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Station station = new Station();
                ProfileActivity.this.customiseLinearLayout.addStation(station);
            }
        });
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        //If we did not receive any data or the result was not OK, abort
        if(data == null || resultCode != RESULT_OK) {
            return;
        }
        
        Bundle extras = data.getExtras();
        long[] checkout = extras.getLongArray("checkoutIds"); //Pictogram IDs
    }
	
	public void startPictoAdmin(int requestCode) {
	    
	    
	    //requestCode defines how many pictograms we want to receive
	    switch(requestCode) {
	    case ProfileActivity.RECEIVE_SINGLE:
	        this.intent.putExtra("hest", "single");
	        break;
	    case ProfileActivity.RECEIVE_MULTIPLE:
	        this.intent.putExtra("hest", "multi");
	        break;
	    }
	    
		super.startActivityForResult(this.intent, requestCode);
	}
	
	public static final int RECEIVE_SINGLE = 0;
	public static final int RECEIVE_MULTIPLE = 1;
}
