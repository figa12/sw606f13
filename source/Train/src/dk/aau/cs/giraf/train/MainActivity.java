package dk.aau.cs.giraf.train;

import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.GameActivity;
import dk.aau.cs.giraf.train.profile.ProfileActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;


public class MainActivity extends Activity {
    
    private Intent intentToGame;
    private Intent intentToProfile;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/* Get data from launcher */
		Bundle extras = getIntent().getExtras();
		if (extras != null) {        	
        	Data.currentGuardianID = extras.getLong("currentGuardianID");
        	Data.currentChildID = extras.getLong("currentChildID");
        	Data.appBackgroundColor = extras.getInt("appBackgroundColor");
        } else {
        	Data.currentGuardianID = 1;
        	Data.currentChildID = -3;
        	Data.appBackgroundColor = 0xFFFFBB55;
        }
		
		Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background);
        backgroundDrawable.setColorFilter(Data.appBackgroundColor, PorterDuff.Mode.OVERLAY);
        super.findViewById(R.id.mainActivity).setBackgroundDrawable(backgroundDrawable);
        
        this.intentToGame = new Intent(this, GameActivity.class);
        this.intentToProfile = new Intent(this, ProfileActivity.class);
	}

	public void startGame(View view) {
		super.startActivity(this.intentToGame);
	}
	
	public void startMenu(View view) {
		super.startActivity(this.intentToProfile);
	}

}
