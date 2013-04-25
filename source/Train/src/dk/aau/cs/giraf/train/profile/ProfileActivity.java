package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.content.pm.ResolveInfo;

public class ProfileActivity extends Activity {
	
	private ChildrenListView listView;
    private Guardian guardian = null;
	private Intent pictoAdminIntent = new Intent();
	private CustomiseLinearLayout customiseLinearLayout;
	private ProgressDialog progressDialog;
	private AlertDialog errorDialog;
	public static final int ALLOWED_PICTOGRAMS = 6;
	public static final int ALLOWED_STATIONS   = 6;
	
	private GameConfiguration gameConfiguration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_profile);
		
		ArrayList<Art> artList = new ArrayList<Art>();//FIXME Is never used.
		
		/* Initialize the guardian object. */
    	this.guardian = Guardian.getInstance(Data.currentChildID, Data.currentGuardianID, getApplicationContext(), artList);    	
    	this.guardian.backgroundColor = Data.appBackgroundColor;

		ChildrenListView listView = (ChildrenListView) super.findViewById(R.id.profilelist);
		this.listView = listView;
		listView.guardian = this.guardian;
		listView.loadChildren();
		
	    Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background);
	    backgroundDrawable.setColorFilter(Data.appBackgroundColor, PorterDuff.Mode.OVERLAY);
		super.findViewById(R.id.mainProfileLayout).setBackgroundDrawable(backgroundDrawable);
	    
		this.customiseLinearLayout = (CustomiseLinearLayout) super.findViewById(R.id.customiseLinearLayout);
		
		Button addStationButton = (Button) super.findViewById(R.id.addStationButton);
		addStationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Station station = new Station();
                ProfileActivity.this.customiseLinearLayout.addStation(station);
            }
        });
		
		Button saveGameButton = (Button) super.findViewById(R.id.saveGameButton);
		saveGameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 Child selectedChild = ProfileActivity.this.listView.getSelectedChild();
				 GameConfiguration game = new GameConfiguration("testGame", 1, 1);
				 DB db = new DB(ProfileActivity.this);
				 db.saveChild(selectedChild, game);
			}
		});
		
		this.pictoAdminIntent.setComponent(new ComponentName("dk.aau.cs.giraf.pictoadmin","dk.aau.cs.giraf.pictoadmin.PictoAdminMain"));
		
		this.progressDialog = new ProgressDialog(this);
		this.progressDialog.setMessage("Vent venligst");
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Fejl");
        alertDialogBuilder.setMessage("PictoAdmin er ikke installeret p� denne enhed");
        alertDialogBuilder.setNegativeButton("Okay", null);
        this.errorDialog = alertDialogBuilder.create();
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
    
	public void startPictoAdmin(int requestCode, PictogramReceiver pictogramRequester) {
	    if(this.isCallable(this.pictoAdminIntent) == false) {
	        this.errorDialog.show();
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
	    } //TODO Investigate whether our intent gets cleared of these 'extra' objects on return
	    
		super.startActivityForResult(this.pictoAdminIntent, requestCode);
	}
	
	private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
	}
}