package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class GameActivity extends Activity {

	private GlView openGLView;
	private ArrayList<LinearLayout> stationLinear;
	private ArrayList<LinearLayout> cartsLinear;
	private LinearLayout stationCategoryLinear;
	private LinearLayout trainDriverLinear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_game);

		this.createPictogramLayouts(6);

		this.openGLView = (GlView) findViewById(R.id.openglview);

		this.getWindow().getDecorView()
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE); // TODO
																			// investigate
																			// API
																			// level
																			// problems
																			// and
																			// write
																			// results
																			// in
																			// comment
	}

	/**
	 * Dynamically adds FrameLayout defined by numbersOfPictograms, The
	 * Framelayout is then filled with pictograms.
	 * 
	 * @param numbersOfPictograms
	 */
	private void createPictogramLayouts(int numbersOfPictograms) {
		setLayouts();
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);

		for (LinearLayout stationlinear : stationLinear) {
			for (int j = 0; j < (numbersOfPictograms / 2); j++) {
				//int height = (stationlinear.getWidth())/ (numbersOfPictograms / 2); // testing
				LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				linearLayoutParams.weight = 1;

				FrameLayout frameLayout = new FrameLayout(this);
				frameLayout.setOnDragListener(new DragListener());
				frameLayout.setBackgroundDrawable(normalShape);
				stationlinear.addView(frameLayout, linearLayoutParams);
			}
		}

		for (LinearLayout cartlinear : cartsLinear) {
			for (int j = 0; j < (numbersOfPictograms / 2); j++) {
				LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				linearLayoutParams.weight = 1;
				FrameLayout frameLayout = new FrameLayout(this);
				frameLayout.setOnDragListener(new DragListener());
				frameLayout.setBackgroundDrawable(normalShape);

				cartlinear.addView(frameLayout, linearLayoutParams);
			}
		}
		
		//frame settings for StationCategory
		LinearLayout.LayoutParams linearLayoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayoutParams2.weight = 1;

		FrameLayout frameLayout2 = new FrameLayout(this);
		frameLayout2.setOnDragListener(new DragListener());
		frameLayout2.setBackgroundDrawable(normalShape);
		stationCategoryLinear.addView(frameLayout2,linearLayoutParams2);
	
		
		//frame setttings for TrainDriver
		LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayoutParams.weight = 1;

		FrameLayout frameLayout = new FrameLayout(this);
		frameLayout.setOnDragListener(new DragListener());
		frameLayout.setBackgroundDrawable(normalShape);
		trainDriverLinear.addView(frameLayout,linearLayoutParams);

		//add pictograms to the frames
		addPictograms();
	}

	/**
	 * Find the LinearLayouts sepcified in activti_game.xml and stores the ref
	 * in different lists.
	 */
	private void setLayouts() {
		//StationLeft and Right
		stationLinear = new ArrayList<LinearLayout>();
		stationLinear.add((LinearLayout) findViewById(R.id.StationLeftLinearLayout));
		stationLinear.add((LinearLayout) findViewById(R.id.StationRightLinearLayout));
		
		//StationCategory
		stationCategoryLinear = (LinearLayout) findViewById(R.id.StationCategoryLinearLayout);
		
		//Carts1 and 2
		cartsLinear = new ArrayList<LinearLayout>();
		cartsLinear.add((LinearLayout) findViewById(R.id.Cart1LinearLayout));
		cartsLinear.add((LinearLayout) findViewById(R.id.Cart2LinearLayout));
		
		//TrainDriver
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
				Pictogram p = PictoFactory.INSTANCE.getPictogram(this, 0);
				p.renderImage();
				p.renderText();
				p.setOnTouchListener(new TouchListener());
				//p.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape)); // to test

				FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.MATCH_PARENT);

				try {
					((FrameLayout) linearPictograms.get(i).getChildAt(j)).addView(p,frameLayoutParams);
					((FrameLayout) linearPictograms.get(i).getChildAt(j)).setTag("filled");
				} catch (Exception e) {
					Log.d(GameActivity.class.getSimpleName(),"Null value, when adding pictograms to FrameLayouts");
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
					v.setBackgroundDrawable(enterShape); // FIXME code is
															// deprecated, use
															// new
					break;

				case DragEvent.ACTION_DRAG_EXITED:
					// Change the background back when exiting droplayout(purely
					// style)
					v.setBackgroundDrawable(normalShape); // FIXME code is
															// deprecated, use
															// new
					break;

				case DragEvent.ACTION_DROP:
					// Dropped, assigns the draggedview to the dropcontainer if
					// the container does not already contain a view.
					ViewGroup ownerContainer = (ViewGroup) draggedView.getParent();
					
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
					 v.setBackgroundDrawable(normalShape); // FIXME code is
					// deprecated, use new
				
					 //The weird bug is solves by this.
					draggedView.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							draggedView.setVisibility(View.VISIBLE);
						}
					});
					break;

				}
				return true;
			} 
			else {
				return false;
			}
		}
	}
}
