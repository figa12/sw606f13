package dk.aau.cs.giraf.train.profile;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.GameActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
	
    public static final String SAVEFILE_PATH = "game_configurations.txt";
    public static final String GAME_CONFIGURATION = "GameConfiguration";
    public static final String GAME_CONFIGURATIONS = "GameConfigurations";
    public static final String SELECTED_CHILD_ID = "selectedChildId";
    public static final String SELECTED_CHILD_NAME = "selectedChildName";
    
	public static final int RECEIVE_SINGLE = 0;
    public static final int RECEIVE_MULTIPLE = 1;
    public static final int RECEIVE_GAME_NAME = 2;
    
    private Intent gameIntent;
    private Intent saveIntent;
    private Intent pictoAdminIntent = new Intent();
    
    private Guardian guardian = null;
	private ChildrenListView childrenListView;
	private GameLinearLayout gameLinearLayout;
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
        
        this.gameLinearLayout = ((GameLinearLayout) findViewById(R.id.gamelist));
        this.gameLinearLayout.loadAllConfigurations();
        
		/* Initialize the guardian object. */
    	this.guardian = Guardian.getInstance(Data.currentChildID, Data.currentGuardianID, getApplicationContext(), new ArrayList<Art>());    	
    	this.guardian.backgroundColor = Data.appBackgroundColor;
    	
    	this.childrenListView = (ChildrenListView) super.findViewById(R.id.profilelist);
		this.childrenListView.loadChildren(this.guardian);
		
		if(this.childrenListView.getCount() == 0) {
		    //Don't allow saving if the current guardian has no associated children
		    ((Button) super.findViewById(R.id.saveGameButton)).setEnabled(false);
		}
		
	    Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background);
	    backgroundDrawable.setColorFilter(Data.appBackgroundColor, PorterDuff.Mode.OVERLAY);
		super.findViewById(R.id.mainProfileLayout).setBackgroundDrawable(backgroundDrawable);
	    
		this.customiseLinearLayout = (CustomiseLinearLayout) super.findViewById(R.id.customiseLinearLayout);
		
		this.gameIntent = new Intent(this, GameActivity.class);
		this.saveIntent = new Intent(this, SaveDialogActivity.class);
		this.pictoAdminIntent.setComponent(new ComponentName("dk.aau.cs.giraf.pictosearch", "dk.aau.cs.giraf.pictosearch.PictoAdminMain"));
		
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
        
        //this.setGameConfiguration(gameConfiguration);
        
        
        this.progressDialog.dismiss(); //Hide progressDialog after creation is done
	}
	
	public void onClickAddStation(View view) {
	    this.customiseLinearLayout.addStation(new StationConfiguration());
	}
	
	public void onClickSaveGame(View view) throws IOException {
	    if (this.isValidConfiguration()) {
	    	this.saveIntent.putExtra(ProfileActivity.GAME_CONFIGURATIONS, this.gameLinearLayout.getGameConfigurations());
	    	
	    	this.saveIntent.putExtra(ProfileActivity.SELECTED_CHILD_NAME, this.childrenListView.getSelectedChild().name);
	    	this.saveIntent.putExtra(ProfileActivity.SELECTED_CHILD_ID, this.childrenListView.getSelectedChild().getProfileId());
	    	
            super.startActivityForResult(this.saveIntent, ProfileActivity.RECEIVE_GAME_NAME);
        }
	}
	
	public void onClickStartGame(View view) {
	    if(this.isValidConfiguration()) {
            this.gameIntent.putExtra(ProfileActivity.GAME_CONFIGURATION, this.getGameConfiguration("the new game", 1337L, 1337L));
            this.startActivity(this.gameIntent);
        }
	}
	
	private void showAlertMessage(String message) {
        this.errorDialog.setMessage(message);
        
        this.errorDialog.show();
	}
	
	private boolean isValidConfiguration() {
	    ArrayList<StationConfiguration> currentStation = this.customiseLinearLayout.getStations();
	    
	    //There needs to be at least one station
	    if(currentStation.size() < 1) {
	        this.showAlertMessage(super.getResources().getString(R.string.station_error));
	        currentStation = null; //Free memory
            return false;
        }
	    
	    for (int i = 0; i < currentStation.size(); i++) {
	        if(currentStation.get(i).getCategory() == -1L) {
                this.showAlertMessage(super.getResources().getString(R.string.category_error));
                currentStation = null; //Free memory
                return false;
            } else if (currentStation.get(i).getAcceptPictograms().size() < 1) {
	            this.showAlertMessage(super.getResources().getString(R.string.pictogram_error));
	            currentStation = null; //Free memory
	            return false;
	        }
	    }
	    currentStation = null; //Free memory
	    //If we have come this far, then the configuration is valid
	    return true;
	}
	
	private GameConfiguration getGameConfiguration(String gameName, long gameID, long childID) {
	    GameConfiguration gameConfiguration = new GameConfiguration(gameName, gameID, childID); //TODO Set appropriate IDs
	    gameConfiguration.setStations(this.customiseLinearLayout.getStations());
	    return gameConfiguration;
	}
	
	public void setGameConfiguration(GameConfiguration gameConfiguration) {
	    ArrayList<StationConfiguration> newReference = new ArrayList<StationConfiguration>();
	    for (int i = 0; i < gameConfiguration.getStations().size(); i++) {
	        newReference.add(new StationConfiguration(gameConfiguration.getStation(i)));
	    }
	    this.customiseLinearLayout.setStationConfigurations(newReference);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.progressDialog.dismiss();
        
        //If we did not receive any data or the result was not OK, abort
        if(data == null || resultCode != RESULT_OK) {
            return;
        }
        
        long[] checkout;
        
        switch(requestCode) {
        case ProfileActivity.RECEIVE_SINGLE:
        	checkout = data.getExtras().getLongArray("checkoutIds"); //Pictogram IDs
            
            if(checkout.length > 0) {
                this.pictogramReceiver.receivePictograms(checkout, requestCode);
            }
        	break;
        case ProfileActivity.RECEIVE_MULTIPLE:
        	checkout = data.getExtras().getLongArray("checkoutIds"); //Pictogram IDs
            
            if(checkout.length > 0) {
                this.pictogramReceiver.receivePictograms(checkout, requestCode);
            }
        	break;
        case ProfileActivity.RECEIVE_GAME_NAME:
        	String gameName = data.getExtras().getString(SaveDialogActivity.GAME_NAME);
        	GameConfiguration gameConfiguration = getGameConfiguration(gameName, 1337L, childrenListView.getSelectedChild().getProfileId());
        	this.gameLinearLayout.addGameConfiguration(gameConfiguration);
			try {
				this.saveAllConfigurations(this.gameLinearLayout.getGameConfigurations());
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, "Kan ikke gemme", Toast.LENGTH_SHORT).show();
			}
        	break;
        }
    }
	
	private PictogramReceiver pictogramReceiver;
	
	public void startPictoAdmin(int requestCode, PictogramReceiver pictogramRequester) {
	    if(this.isCallable(this.pictoAdminIntent) == false) {
	        this.showAlertMessage(super.getResources().getString(R.string.picto_error));
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
        
        this.pictoAdminIntent.putExtra("currentChildID", this.childrenListView.getSelectedChild().getProfileId());
        this.pictoAdminIntent.putExtra("currentGuardianID", Data.currentGuardianID);

        super.startActivityForResult(this.pictoAdminIntent, requestCode);
	}
	
	private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
	}
	
	public void saveAllConfigurations(ArrayList<GameConfiguration> gameConfigurations) throws IOException {
		FileOutputStream fos = null;
		
		try {
			fos = this.openFileOutput(SAVEFILE_PATH, Context.MODE_PRIVATE);
			for (GameConfiguration game : gameConfigurations) {
				fos.write(game.writeConfiguration().getBytes());
			}
		} catch(FileNotFoundException e) {
		    return;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.flush();
				fos.close();
			}
		}
	}
	
	public boolean saveConfiguration() throws IOException {
		FileOutputStream fos = null; 
		GameConfiguration game = getGameConfiguration("the new game", 1337L, 1337L);
		
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
}