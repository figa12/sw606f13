package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.R.id;

import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.style.EasyEditSpan;
import android.text.style.RelativeSizeSpan;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.FrameLayout.LayoutParams;

public class GameActivity extends Activity {

	private GlView openGLView;
	private ArrayList<LinearLayout> stationLayouts;
	private ArrayList<LinearLayout> cartsLayouts;
	private LinearLayout stationCategoryLayout;
	private LinearLayout trainDriverLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_game);

		this.createPictogramLayouts(6);

		this.setPictoClass();

		// this.setListeners();

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
	 * 
	 * @param numbersOfPictograms
	 */
	private void createPictogramLayouts(int numbersOfPictograms){
		setLayouts();
		Resources res = getResources();
		
		((FrameLayout)findViewById(id.Cart1LeftLayout)).setOnDragListener(new DragListener());
		((FrameLayout)findViewById(id.Cart1RightLayout)).setOnDragListener(new DragListener());
		((FrameLayout)findViewById(id.Cart2LeftLayout)).setOnDragListener(new DragListener());
		((FrameLayout)findViewById(id.Cart2RightLayout)).setOnDragListener(new DragListener());
		
		for (LinearLayout linear : stationLayouts) {
			for (int j = 0; j < (numbersOfPictograms/2); j++) {
				FrameLayout frameLayout = new FrameLayout(this);
				LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				
				linearLayoutParams.weight = 1;
				frameLayout.setTag("filled");
				frameLayout.setOnDragListener(new DragListener());
				
				linear.addView(frameLayout,linearLayoutParams);
			}
		}
	}

	private void setLayouts() {
		cartsLayouts = new ArrayList<LinearLayout>();
		stationLayouts = new ArrayList<LinearLayout>();
		stationCategoryLayout = (LinearLayout) findViewById(id.StationCategoryLinearLayout);
		trainDriverLayout = (LinearLayout) findViewById(id.TrainDriverLinearLayout);
		stationLayouts.add((LinearLayout) findViewById(id.StationLeftLinearLayout));
		stationLayouts.add((LinearLayout) findViewById(id.StationRightLinearLayout));
		cartsLayouts.add((LinearLayout) findViewById(id.Cart2LinearLayout));
		cartsLayouts.add((LinearLayout) findViewById(id.Cart1LinearLayout));
	}

	private void setPictoClass() {

		// hardcoded for demotest
		LinearLayout[] linear = new LinearLayout[4];
		linear[0] = (LinearLayout) findViewById(id.StationLeftLinearLayout);
		linear[1] = (LinearLayout) findViewById(id.StationRightLinearLayout);
		linear[2] = (LinearLayout) findViewById(id.TrainDriverLinearLayout);
		linear[3] = (LinearLayout) findViewById(id.StationCategoryLinearLayout);
		
		for (int i = 0; i < linear.length; i++) {
			for (int j = 0; j < linear[i].getChildCount(); j++) {
				Pictogram p = PictoFactory.INSTANCE.getPictogram(this, 0);
				p.renderImage();
				p.setOnTouchListener(new TouchListener());
				p.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape)); //to test
				
				FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
				frameLayoutParams.gravity = Gravity.TOP;
				
				((FrameLayout) linear[i].getChildAt(j)).addView(p,frameLayoutParams);
				((FrameLayout) linear[i].getChildAt(j)).setOnDragListener(new DragListener());
				((FrameLayout) linear[i].getChildAt(j)).setTag("filled");
			}
		}
	}

	/*
	 * Sets touch and drag listeners. This is temporary and only for proof of
	 * concept
	 */
	/**
	 * Sets the onDragListeners and onTouchListeners for the pictograms
	 */
	private void setListeners() {
		// TouchListeners
		findViewById(R.id.Cart1LeftImageView).setOnTouchListener(
				new TouchListener());
		findViewById(R.id.Cart1RightImageView).setOnTouchListener(
				new TouchListener());
		findViewById(R.id.Cart2LeftImageView).setOnTouchListener(
				new TouchListener());
		findViewById(R.id.Cart2RightImageView).setOnTouchListener(
				new TouchListener());
		findViewById(R.id.Spot1LeftImageView).setOnTouchListener(
				new TouchListener());
		findViewById(R.id.Spot2RightImageView).setOnTouchListener(
				new TouchListener());
		findViewById(R.id.Spot3LeftImageView).setOnTouchListener(
				new TouchListener());
		findViewById(R.id.Spot4RightImageView).setOnTouchListener(
				new TouchListener());

		// Draglisteners
		findViewById(R.id.Cart1LeftLayout)
				.setOnDragListener(new DragListener());
		findViewById(R.id.Cart1RightLayout).setOnDragListener(
				new DragListener());
		findViewById(R.id.Cart2LeftLayout)
				.setOnDragListener(new DragListener());
		findViewById(R.id.Cart2RightLayout).setOnDragListener(
				new DragListener());
		findViewById(R.id.Spot1LeftLayout)
				.setOnDragListener(new DragListener());
		findViewById(R.id.Spot2RightLayout).setOnDragListener(
				new DragListener());
		findViewById(R.id.Spot3LeftLayout)
				.setOnDragListener(new DragListener());
		findViewById(R.id.Spot4RightLayout).setOnDragListener(
				new DragListener());
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
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
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
			if (event.getLocalState() == null) {
				// do nothing, maybe return false..
				return true;
			}

			View draggedView = (View) event.getLocalState();
			ViewGroup ownerContainer = (ViewGroup) draggedView.getParent();

			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// makes the draggedview invisible in ownerContainer
				draggedView.setVisibility(View.INVISIBLE);
				break;

			case DragEvent.ACTION_DRAG_ENTERED:
				// Change the background of droplayout(purely style)
				v.setBackgroundDrawable(enterShape); // FIXME code is
														// deprecated, use new
				break;

			case DragEvent.ACTION_DRAG_EXITED:
				// Change the background back when exiting droplayout(purely
				// style)
				v.setBackgroundDrawable(normalShape); // FIXME code is
														// deprecated, use new
				break;

			case DragEvent.ACTION_DROP:
				// Dropped, assigns the draggedview to the dropcontainer if
				// the container does not already contain a view.
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
				draggedView.setVisibility(View.VISIBLE);
				break;

			}
			return true;
		}
	}
}
