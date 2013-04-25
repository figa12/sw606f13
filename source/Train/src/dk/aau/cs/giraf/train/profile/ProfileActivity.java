package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.game.GameData;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class ProfileActivity extends Activity {
	
    private Guardian guardian = null;
	private Intent intent = new Intent();
	private CustomiseLinearLayout customiseLinearLayout;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_profile);
		
		ArrayList<Art> artList = new ArrayList<Art>();//FIXME Is never used.
		
		/* Initialize the guardian object */
    	this.guardian = Guardian.getInstance(Data.currentChildID, Data.currentGuardianID, getApplicationContext(), artList);    	
    	this.guardian.backgroundColor = Data.appBackgroundColor;

		ChildrenListView listview = (ChildrenListView) super.findViewById(R.id.profilelist);
		listview.guardian = this.guardian;
		listview.loadChildren();
		
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
		
		this.intent.setComponent(new ComponentName("dk.aau.cs.giraf.pictoadmin","dk.aau.cs.giraf.pictoadmin.PictoAdminMain"));
		
		this.progressDialog = new ProgressDialog(this);
		this.progressDialog.setMessage("Vent venligst");
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
	    this.progressDialog.show();
	    
	    this.pictogramReceiver = pictogramRequester;
	    
	    //requestCode defines how many pictograms we want to receive
	    switch(requestCode) {
	    case ProfileActivity.RECEIVE_SINGLE:
	        this.intent.putExtra("purpose", "single");
	        break;
	    case ProfileActivity.RECEIVE_MULTIPLE:
	        this.intent.putExtra("purpose", "multi");
	        break;
	    } //TODO Investigate whether our intent gets cleared of these 'extra' objects on return
	    
		super.startActivityForResult(this.intent, requestCode);
	}
}
