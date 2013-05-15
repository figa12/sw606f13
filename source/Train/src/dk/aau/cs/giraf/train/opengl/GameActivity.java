package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;

import org.apache.http.conn.routing.RouteInfo.LayerType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dk.aau.cs.giraf.train.GameConfiguration;
import dk.aau.cs.giraf.train.MainActivity;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.StationConfiguration;
import dk.aau.cs.giraf.train.R.id;
import dk.aau.cs.giraf.train.opengl.game.GameData;

public class GameActivity extends Activity {

	private GlView openGLView;
	private GameData gameData;
	
	private ArrayList<StationLinearLayout> stationLinear;
	private ArrayList<WagonLinearLayout> cartsLinear;
	private LinearLayout trainDriverLinear;
	private GameConfiguration gameConfiguration;
	private GameController gameController;
	
	public ImageButton fluteButton;
	
	public SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    public int sound;
    public int streamId;
    
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage(super.getResources().getString(R.string.loading));
        this.progressDialog.setCancelable(true);
        this.progressDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				for (StationLinearLayout stationLin : stationLinear) {
					for (PictoFrameLayout pictoFrame : stationLin.getPictoframes()) {
						pictoFrame.setVisibility(View.VISIBLE);
					}
				}
			}
		});
        
        this.progressDialog.setOnShowListener(new OnShowListener() {
			public void onShow(DialogInterface dialog) {
				for (StationLinearLayout stationLin : stationLinear) {
					for (PictoFrameLayout pictoFrame : stationLin.getPictoframes()) {
						pictoFrame.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
        
        this.progressDialog.show();
        ((TextView) progressDialog.findViewById(android.R.id.message)).setTextColor(android.graphics.Color.WHITE);
		
		this.setContentView(R.layout.activity_game);
		
		this.sound = soundPool.load(this, R.raw.train_whistle, 1);
		
		Bundle configurationBundle = super.getIntent().getExtras();
		if(configurationBundle != null) {
		    this.gameConfiguration = configurationBundle.getParcelable(MainActivity.GAME_CONFIGURATION);
		} else {
            gameConfiguration = new GameConfiguration("Game 3", 2, -3);
            gameConfiguration.addStation(new StationConfiguration(2L));
            gameConfiguration.addStation(new StationConfiguration(4L));
            gameConfiguration.addStation(new StationConfiguration(3L));
            gameConfiguration.getStation(0).addAcceptPictogram(2L);
            gameConfiguration.getStation(1).addAcceptPictogram(4L);
            gameConfiguration.getStation(1).addAcceptPictogram(4L);
            gameConfiguration.getStation(2).addAcceptPictogram(3L);
            gameConfiguration.getStation(2).addAcceptPictogram(2L);
		}
		
		this.gameData = new GameData(this, gameConfiguration);
        
		this.openGLView = (GlView) findViewById(R.id.openglview);
        this.openGLView.bindGameData(this.gameData); //The GlView is instantiated by the system, bind here instead of through constructor.
		
        this.gameController = new GameController(this, gameConfiguration, this.gameData);
		this.addFrameLayoutsAndPictograms(gameConfiguration.getNumberOfPictogramsOfStations());
		
		this.alertDialog = this.createAlertDialog();
		
		this.hideSystemUI();
	}
	
	@SuppressLint("InlinedApi")
    public void hideSystemUI() {
	    this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}
	
	private AlertDialog createAlertDialog() {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //myAlertDialog.setTitle("Title");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(R.string.close_dialog);
        alertDialogBuilder.setPositiveButton(super.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //'Ja' button is clicked
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton(super.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //'Annuller' button is clicked
                GameActivity.this.gameData.onResume();
                GameActivity.this.soundPool.resume(GameActivity.this.streamId);
            }
        });
        return alertDialogBuilder.create();
	}
	
	public void showProgressDialog(){
		this.progressDialog.show();
	}
	
	public void dismissProgressDialog(){
		this.progressDialog.dismiss();
	}
	
	public void onTrainStopEvent() {
	    //This is an event/interface from gameData
	    this.gameController.TrainIsStopping();
	}
	
	public GameController getGameController(){
		return this.gameController;
	}
	
	/***
	 * Adds a pictogram to linearlayout and returns the linearlayout.
	 * @param linearLayout
	 * @return
	 */
	@SuppressWarnings("deprecation")
    private LinearLayout addSingleFrameToLinearLayout(LinearLayout linearLayout){
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);
				LayoutParams categoryParams = new LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				
				FrameLayout frame = new FrameLayout(this);
				frame.setBackgroundDrawable(normalShape);
				linearLayout.addView(frame, categoryParams);
		return linearLayout;
	}
	
	/**
	 * Dynamically adds FrameLayout defined by numbersOfPictograms, The
	 * Framelayout is then later filled with pictograms.
	 * 
	 * @param numbersOfFrameLayouts
	 */
	private void addFrameLayoutsAndPictograms(int numberOfPictoFrames) {
		initLayouts();
		numberOfPictoFrames = (numberOfPictoFrames <= 4) ? 4:6;
		
		for (StationLinearLayout station : stationLinear) {
			station.addPictoFrames(numberOfPictoFrames);
		}
		
		for (WagonLinearLayout wagon : cartsLinear) {
			wagon.addPictoFrames(numberOfPictoFrames);
		}
		
		trainDriverLinear = addSingleFrameToLinearLayout(trainDriverLinear);
		
		ArrayList<Long> pictogramIdsToAdd = gameConfiguration.getIdOfAllPictograms();
		int nextPicId = 0;
		
		for (StationLinearLayout station : stationLinear){
			for (PictoFrameLayout pictoFrame: station.getPictoframes() ){
				if(nextPicId < pictogramIdsToAdd.size()){
					pictoFrame.addPictogramsToFrames(pictogramIdsToAdd.get(nextPicId));
					nextPicId++;
				}
			}
		}
	}

	/**
	 * Find the LinearLayouts sepcified in activty_game.xml and stores the ref
	 * in different lists.
	 */
	private void initLayouts() {
		// StationLeft and Right
		stationLinear = new ArrayList<StationLinearLayout>();
		stationLinear.add((StationLinearLayout) findViewById(R.id.StationLeftLinearLayout));
		stationLinear.add((StationLinearLayout) findViewById(R.id.StationRightLinearLayout));
		
		// FluteButton
		fluteButton = (ImageButton) findViewById(R.id.FluteImageButton);
		fluteButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gameController.trainDrive(stationLinear);
			}
		});
		// Carts1 and 2
		cartsLinear = new ArrayList<WagonLinearLayout>();
		cartsLinear.add((WagonLinearLayout) findViewById(R.id.Cart1LinearLayout));
		cartsLinear.add((WagonLinearLayout) findViewById(R.id.Cart2LinearLayout));

		// TrainDriver
		trainDriverLinear = (LinearLayout) findViewById(R.id.TrainDriverLinearLayout);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.openGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.openGLView.onResume();
	}
	
	/***
	 * Deletes the pictograms from the station that has just been successfully completet.
	 */
	public void deletePictogramsFromStation() {		
		for (LinearLayout lin : stationLinear) {
			for (int i = 0; i < lin.getChildCount(); i++) {
				((FrameLayout)lin.getChildAt(i)).removeAllViews();
				((FrameLayout)lin.getChildAt(i)).setTag(null);
			}
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    GameActivity.this.soundPool.stop(GameActivity.this.streamId);
	    super.finish();
	}

	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Stop the user from unexpected back presses
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        this.gameData.onPause();
	        GameActivity.this.soundPool.pause(GameActivity.this.streamId);
	        
            this.alertDialog.show();
	        return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	public void showStationLinearLayouts() {
		for (StationLinearLayout lin : stationLinear) {
			lin.setVisibility(View.VISIBLE);
		}
		fluteButton.setVisibility(View.VISIBLE);
	}
	
	public void hideStationLinearLayouts(){
		for (StationLinearLayout lin : stationLinear) {
			lin.setVisibility(View.INVISIBLE);
		}
		fluteButton.setVisibility(View.INVISIBLE);
	}
	
	public void hideAllLinearLayouts(){
		for (StationLinearLayout lin : stationLinear) {
			lin.setVisibility(View.INVISIBLE);
		}
		
		for(LinearLayout lin : cartsLinear){
			lin.setVisibility(View.INVISIBLE);
		}
		
		fluteButton.setVisibility(View.INVISIBLE);
		
		trainDriverLinear.setVisibility(View.INVISIBLE);
	}

	public GameData getGameData() {
		return this.gameData;
	}

	public void addAndShowEndButton() {
		Button endButton = new Button(this);
		//endButton.setTextAppearance(this, R.style.ButtonFontStyle);
		endButton.setTextSize(44f);
		RelativeLayout.LayoutParams relaLayout = new RelativeLayout.LayoutParams(400, 150);
		relaLayout.setMargins(440, 150, 0, 0);
		endButton.setLayoutParams(relaLayout);
		endButton.setText("Godt gået!");
		
		
		endButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));
		endButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		RelativeLayout root =  (RelativeLayout)findViewById(id.RootLayout);
		root.addView(endButton);
	}	
}
