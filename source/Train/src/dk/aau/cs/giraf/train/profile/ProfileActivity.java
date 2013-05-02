package dk.aau.cs.giraf.train.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.GameActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.content.pm.ResolveInfo;

public class ProfileActivity extends Activity {
	
    public static final String GAME_CONFIGURATION = "GameConfiguration";
    public static final String SAVEFILE_PATH = "game_configurations.txt";
    
    private Intent gameIntent;
    private Intent saveIntent;
    private Intent pictoAdminIntent = new Intent();
    
    private Guardian guardian = null;
	private ChildrenListView childrenListView;
	private CustomiseLinearLayout customiseLinearLayout;
	
	private ProgressDialog progressDialog;
	private AlertDialog errorDialog;
	
	public static final int ALLOWED_PICTOGRAMS = 6;
	public static final int ALLOWED_STATIONS   = 6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_profile);
		
		this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage(super.getResources().getString(R.string.loading));
        this.progressDialog.setCancelable(true);
        
        //Show progressDialog while loading activity. Set the color to white only one time
        this.progressDialog.show();
        ((TextView) this.progressDialog.findViewById(android.R.id.message)).setTextColor(android.graphics.Color.WHITE);
        
		/* Initialize the guardian object. */
    	this.guardian = Guardian.getInstance(Data.currentChildID, Data.currentGuardianID, this, new ArrayList<Art>());    	
    	this.guardian.backgroundColor = Data.appBackgroundColor;
    	
    	this.childrenListView = (ChildrenListView) super.findViewById(R.id.profilelist);
		this.childrenListView.loadChildren(this.guardian);
		
	    Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background);
	    backgroundDrawable.setColorFilter(Data.appBackgroundColor, PorterDuff.Mode.OVERLAY);
		super.findViewById(R.id.mainProfileLayout).setBackgroundDrawable(backgroundDrawable);
	    
		this.customiseLinearLayout = (CustomiseLinearLayout) super.findViewById(R.id.customiseLinearLayout);
		
		this.gameIntent = new Intent(this, GameActivity.class);
		this.saveIntent = new Intent(this, SaveDialogActivity.class);
		this.pictoAdminIntent.setComponent(new ComponentName("dk.aau.cs.giraf.pictoadmin","dk.aau.cs.giraf.pictoadmin.PictoAdminMain"));
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setNegativeButton(super.getResources().getString(R.string.okay), null);
        this.errorDialog = alertDialogBuilder.create();
        
        
        /* TEST */
        GameConfiguration gameConfiguration = new GameConfiguration("THE game", 2, -3);
        gameConfiguration.addStation(new StationConfiguration(2L));
        gameConfiguration.addStation(new StationConfiguration(4L));
        gameConfiguration.addStation(new StationConfiguration(3L));
        gameConfiguration.getStation(0).addAcceptPictogram(2L);
        gameConfiguration.getStation(1).addAcceptPictogram(4L);
        gameConfiguration.getStation(2).addAcceptPictogram(3L);
        
        this.setGameConfiguration(gameConfiguration);
        
        
        this.progressDialog.dismiss(); //Hide progressDialog after creation is done
	}
	
	public void onClickAddStation(View view) {
	    this.customiseLinearLayout.addStation(new StationConfiguration());
	}
	
	public void onClickSaveGame(View view) throws IOException {
	    if (this.isValidConfiguration()) {            
            super.startActivity(this.saveIntent);
        }
	}
	
	public void onClickStartGame(View view) {
	    if(ProfileActivity.this.isValidConfiguration()) {
            ProfileActivity.this.gameIntent.putExtra(ProfileActivity.GAME_CONFIGURATION, ProfileActivity.this.getGameConfiguration());
            ProfileActivity.this.startActivity(ProfileActivity.this.gameIntent);
        }
	}
	
	private void showAlertMessage(String title, String message) {
	    this.errorDialog.setTitle(title);
        this.errorDialog.setMessage(message);
        
        this.errorDialog.show();
	}
	
	private boolean isValidConfiguration() {
	    GameConfiguration currentGameConfiguration = this.getGameConfiguration();
	    
	    //There needs to be at least one station
	    if(currentGameConfiguration.getStations().size() < 1) {
	        this.showAlertMessage(null, super.getResources().getString(R.string.station_error));
            return false;
        }
	    
	    for (int i = 0; i < currentGameConfiguration.getStations().size(); i++) {
	        if(currentGameConfiguration.getStations().get(i).getCategory() == -1L) {
                this.showAlertMessage(null, super.getResources().getString(R.string.category_error));
                return false;
            } else if (currentGameConfiguration.getStations().get(i).getAcceptPictograms().size() < 1) {
	            this.showAlertMessage(null, super.getResources().getString(R.string.pictogram_error));
	            return false;
	        }
	    }
	    //If we have come this far, then the configuration is valid
	    return true;
	}
	
	private GameConfiguration getGameConfiguration() {
	    GameConfiguration gameConfiguration = new GameConfiguration("the new game", 1337L, 1337L); //TODO Set appropriate IDs
	    gameConfiguration.setStations(this.customiseLinearLayout.getStations());
	    return gameConfiguration;
	}
	
	private void setGameConfiguration(GameConfiguration gameConfiguration) {
	    this.customiseLinearLayout.setStationConfigurations(gameConfiguration.getStations());
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.progressDialog.dismiss();
        
        //If we did not receive any data or the result was not OK, abort
        if(data == null || resultCode != RESULT_OK) {
            return;
        }
        
        long[] checkout = data.getExtras().getLongArray("checkoutIds"); //Pictogram IDs
        
        if(checkout.length > 0) {
            this.pictogramReceiver.receivePictograms(checkout, requestCode);
        }
    }
	
	public static final int RECEIVE_SINGLE = 0;
    public static final int RECEIVE_MULTIPLE = 1;
	private PictogramReceiver pictogramReceiver;
    
	boolean testHest = true;
	
	public void startPictoAdmin(int requestCode, PictogramReceiver pictogramRequester) {
	    if(this.isCallable(this.pictoAdminIntent) == false) {
	        this.showAlertMessage(super.getResources().getString(R.string.error), super.getResources().getString(R.string.picto_error));
	        return;
	    }
	    this.progressDialog.show();
	    
	    this.pictogramReceiver = pictogramRequester;
	    
	    //requestCode defines how many pictograms we want to receive
	    switch(requestCode) {
	    case ProfileActivity.RECEIVE_SINGLE:
	        this.pictoAdminIntent.putExtra("purpose", "single");
	        break;
	    case ProfileActivity.RECEIVE_MULTIPLE:
	        this.pictoAdminIntent.putExtra("purpose", "multi");
	        break;
	    }
	    
		super.startActivityForResult(this.pictoAdminIntent, requestCode);
	}
	
	private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
	}
	
	public boolean saveConfiguration() throws IOException {
		FileOutputStream fos = null; 
		GameConfiguration game = getGameConfiguration();
		
		try {
			fos = this.openFileOutput(SAVEFILE_PATH, Context.MODE_PRIVATE);
			fos.write(game.writeConfiguration().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.flush();
				fos.close();
			}
		}
		
		return true;
	}
	
	public ArrayList<GameConfiguration> loadAllConfigurations() throws IOException {
		FileInputStream fis = null;
		StringWriter sWriter = new StringWriter(1024);
		
		try {
			fis = this.openFileInput(SAVEFILE_PATH);

			int content;
			while ((content = fis.read()) != -1) {
				// convert to char and append to string
				sWriter.append((char) content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		sWriter.close();
		
		ArrayList<GameConfiguration> gameConfigurations = splitConfigurations(sWriter.toString());
		
		return gameConfigurations;
	}
	
	private ArrayList<GameConfiguration> splitConfigurations(String data) {
		ArrayList<GameConfiguration> gameConfigurations = new ArrayList<GameConfiguration>();
		String[] configurations = data.split("\n");
		
		// For each configuration
		for (int i = 0; i < configurations.length; i++) {
			
			String[] parts = configurations[i].split(";");
			String[] game = parts[0].split(",");
			
			long gameID = Long.valueOf(game[0]).longValue();
			long guardianID = Long.valueOf(game[1]).longValue();
			long childID = Long.valueOf(game[2]).longValue();
			String gameName = game[3];
			ArrayList<StationConfiguration> stations = new ArrayList<StationConfiguration>();
			
			// For each station
			for (int k = 1; k < parts.length; k++) {
				StationConfiguration station = new StationConfiguration();
				String[] stationParts = parts[k].split(",");
				
				station.setCategory(Long.valueOf(stationParts[0]).longValue());
				
				// For each accept pictogram of station
				for (int n = 1; n < stationParts.length; n++) {
					station.addAcceptPictogram(Long.valueOf(stationParts[n]).longValue());
				}
				
			}
			
			GameConfiguration gameConf = new GameConfiguration(gameName, gameID, childID, guardianID);
			gameConf.setStations(stations);
			
			gameConfigurations.add(gameConf);
		}
		
		return gameConfigurations;
	}
}