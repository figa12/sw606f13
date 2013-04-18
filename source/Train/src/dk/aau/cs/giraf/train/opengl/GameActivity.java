package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.res.Configuration;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.game.GameData;
import dk.aau.cs.giraf.train.profile.GameConfiguration;

public class GameActivity extends Activity {

	private GlView openGLView;
	private static ArrayList<LinearLayout> stationLinear;
	private ArrayList<LinearLayout> cartsLinear;
	private static LinearLayout stationCategoryLinear;
	private LinearLayout trainDriverLinear;
	public static ImageButton fluteButton;
	private final static SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    private static int sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_game);
		
		GameActivity.sound = soundPool.load(this, R.raw.koere, 1);
		
		this.createPictogramLayouts(6);
		
		GameData.resetGameData();
		this.openGLView = (GlView) findViewById(R.id.openglview);

		this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	/**
	 * Dynamically adds FrameLayout defined by numbersOfPictograms, The
	 * Framelayout is then filled with pictograms.
	 * 
	 * @param numbersOfPictograms
	 */
	private void createPictogramLayouts(int numbersOfPictograms) {
		findandSetLayouts();
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);
		int height = 300/(numbersOfPictograms/2);
		
		for (LinearLayout stationlinear : stationLinear) {
			for (int j = 0; j < (numbersOfPictograms / 2); j++) {
				LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0,height);
				
				linearLayoutParams.gravity = Gravity.CENTER;
				linearLayoutParams.weight = 1.0f;
				
				FrameLayout frameLayout = new FrameLayout(this);
				frameLayout.setOnDragListener(new DragListener());
				frameLayout.setBackgroundDrawable(normalShape);
				
				stationlinear.addView(frameLayout, linearLayoutParams);
			}
		}

		for (LinearLayout cartlinear : cartsLinear) {
			for (int j = 0; j < (numbersOfPictograms / 2); j++) {
				LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0,height);
				linearLayoutParams.weight = 1.0f;
				linearLayoutParams.gravity = Gravity.CENTER;
				
				FrameLayout frameLayout = new FrameLayout(this);
				frameLayout.setOnDragListener(new DragListener());
				frameLayout.setBackgroundDrawable(normalShape);

				cartlinear.addView(frameLayout, linearLayoutParams);
			}
		}

		// frame settings for StationCategory
		LinearLayout.LayoutParams linearLayoutParams2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayoutParams2.weight = 1;

		FrameLayout frameLayout2 = new FrameLayout(this);
		frameLayout2.setBackgroundDrawable(normalShape);
		stationCategoryLinear.addView(frameLayout2, linearLayoutParams2);

		// frame setttings for TrainDriver
		LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayoutParams.weight = 1;

		FrameLayout frameLayout = new FrameLayout(this);
		frameLayout.setBackgroundDrawable(normalShape);
		trainDriverLinear.addView(frameLayout, linearLayoutParams);

		// add pictograms to the frames
		addPictograms();
	}

	/**
	 * Find the LinearLayouts sepcified in activti_game.xml and stores the ref
	 * in different lists.
	 */
	private void findandSetLayouts() {
		// StationLeft and Right
		stationLinear = new ArrayList<LinearLayout>();
		stationLinear.add((LinearLayout) findViewById(R.id.StationLeftLinearLayout));
		stationLinear.add((LinearLayout) findViewById(R.id.StationRightLinearLayout));

		// StationCategory
		stationCategoryLinear = (LinearLayout) findViewById(R.id.StationCategoryLinearLayout);

		// FluteButton
		fluteButton = (ImageButton) findViewById(R.id.FluteImageButton);
		fluteButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				trainDrive(true);
			}
		});
		// Carts1 and 2
		cartsLinear = new ArrayList<LinearLayout>();
		cartsLinear.add((LinearLayout) findViewById(R.id.Cart1LinearLayout));
		cartsLinear.add((LinearLayout) findViewById(R.id.Cart2LinearLayout));

		// TrainDriver
		trainDriverLinear = (LinearLayout) findViewById(R.id.TrainDriverLinearLayout);

	}
	
	/**
	 * Adds pictograms to Station, StationCategory and TrainDriver
	 */
	private void addPictograms() {
		List<LinearLayout> linearPictograms = new ArrayList<LinearLayout>();
		linearPictograms.addAll(stationLinear);
		linearPictograms.add(stationCategoryLinear);
		linearPictograms.add(trainDriverLinear);

		for (int i = 0; i < linearPictograms.size(); i++) {
			for (int j = 0; j < linearPictograms.get(i).getChildCount(); j++) {
				Pictogram p = PictoFactory.INSTANCE.getPictogram(this, 1L);
				p.renderImage();
				p.renderText();
				int test = R.id.TrainDriverLinearLayout;
				int bla = linearPictograms.get(i).getId();
				if(bla == test || bla == R.id.StationCategoryLinearLayout){
					//Do not add TouchListener on Traindriver and StationCategory
				}
				else {
					p.setOnTouchListener(new TouchListener());
				}

				FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.MATCH_PARENT);
				try {
					((FrameLayout) linearPictograms.get(i).getChildAt(j)).addView(p, frameLayoutParams);
					((FrameLayout) linearPictograms.get(i).getChildAt(j)).setTag("filled");
					((FrameLayout) linearPictograms.get(i).getChildAt(j)).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));
				} catch (Exception e) {
					Log.d(GameActivity.class.getSimpleName(),
							"Null value, when adding pictograms to FrameLayouts");
				}
			}
		}
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
	
	public static void trainDrive(boolean drive){
		if (drive) {
			if(GameData.currentTrainVelocity == 0f && GameData.numberOfStops < GameData.numberOfStations - 1) {
				boolean readyToGo = true;
				if(GameData.numberOfStops == 0){
					for (LinearLayout lin : stationLinear) {
						for (int i = 0; i< lin.getChildCount();i++) {
							FrameLayout frame = (FrameLayout)lin.getChildAt(i);
							if(frame.getChildAt(0) != null){
								readyToGo = false;
							}
						}
					}
				}
				else{
					//check if it is the correct pictogram on the right station.
				}
				
				if(readyToGo){
					for (LinearLayout lin : stationLinear) {
						lin.setVisibility(View.INVISIBLE);
					}
	
					stationCategoryLinear.setVisibility(View.INVISIBLE);
					
					//check if all stations are empty so train is ready to go.
					
					GameData.accelerateTrain();
					soundPool.play(sound, 1f, 1f, 0, 0, 0.75f);
	                //Make LinearLayouts invisble or animate
					fluteButton.setVisibility(View.INVISIBLE);
				}
            }
			
		} else {
			for (LinearLayout lin : stationLinear) {
				lin.setVisibility(View.VISIBLE);
			}

			stationCategoryLinear.setVisibility(View.VISIBLE);
			fluteButton.setVisibility(View.VISIBLE);
		}
	}


	/**
	 * A touch listener that starts a drag event. There should also be a
	 * receiver implementing {@link OnDragListener}.
	 * 
	 * @see DragListener
	 */
	private final class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * A drag listner implementing an onDrag() method that runs when something
	 * is dragged to it.
	 */
	private final class DragListener implements OnDragListener {
		private Drawable enterShape;
		private Drawable normalShape;

		public DragListener() {
			Resources resources = getResources();

			this.enterShape = resources.getDrawable(R.drawable.shape_droptarget);
			this.normalShape = resources.getDrawable(R.drawable.shape);
		}

		@Override
		public boolean onDrag(View v, DragEvent event) {
			if (event.getLocalState() != null) {
				// do nothing, maybe return false..
				final View draggedView = (View) event.getLocalState();

				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					// makes the draggedview invisible in ownerContainer
					draggedView.setVisibility(View.INVISIBLE);
					break;

				case DragEvent.ACTION_DRAG_ENTERED:
					// Change the background of droplayout(purely style)
					v.setBackgroundDrawable(enterShape);
					break;

				case DragEvent.ACTION_DRAG_EXITED:
					// Change the background back when exiting droplayout(purely
					// style)
					v.setBackgroundDrawable(normalShape);					
					break;

				case DragEvent.ACTION_DROP:
					// Dropped, assigns the draggedview to the dropcontainer if
					// the container does not already contain a view.
					ViewGroup ownerContainer = (ViewGroup) draggedView
							.getParent();

					FrameLayout dropContainer = (FrameLayout) v;
					Object tag = dropContainer.getTag();

					if (tag == null) {
						ownerContainer.removeView(draggedView);
						ownerContainer.setTag(null);
						dropContainer.addView(draggedView);
						dropContainer.setTag("filled");
					}
					break;

				case DragEvent.ACTION_DRAG_ENDED:
					// Makes the draggedview visible again after the view has
					// been moved or the drop wasn't valid.
					v.setBackgroundDrawable(normalShape);

					// The weird bug is solves by this.
					draggedView.post(new Runnable() {
						@Override
						public void run() {
							draggedView.setVisibility(View.VISIBLE);
						}
					});
					break;

				}
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    GameData.resetGameData();
	}

	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Stop the user from unexpected back presses
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        GameData.onPause();
	        
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            //myAlertDialog.setTitle("Title");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Er du sikker p� at du vil afslutte?");
            alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //'Ja' button is clicked
                    finish();
                }
            });
            alertDialog.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //'Annuller' button is clicked
                    GameData.onResume();
                }
            });
            
            alertDialog.show();
	        return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
