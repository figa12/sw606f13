package dk.aau.cs.giraf.train;

import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.GameActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void startGame(View view) {
		Intent intentToGame = new Intent(this, GameActivity.class);
		startActivity(intentToGame);
	}
	
	public void startMenu(View view) {
		Intent intentToProfile = new Intent(this, ProfileActivity.class);
		startActivity(intentToProfile);
	}

}
