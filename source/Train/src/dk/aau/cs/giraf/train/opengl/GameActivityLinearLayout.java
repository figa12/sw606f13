package dk.aau.cs.giraf.train.opengl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.R.attr;

public abstract class GameActivityLinearLayout extends LinearLayout {

	public GameActivityLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public void addPictoFrames(int numberOfPictoFrames){
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);
		int height = 300/(numberOfPictoFrames/2);
		for (int j = 0; j < (numberOfPictoFrames / 2); j++) {
			
			
			LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(0,height,1.0f);
			PictoFrameLayout pictoFrameLayout = new PictoFrameLayout(super.getContext());
			pictoFrameLayout.setOnDragListener(new DragListener());
			pictoFrameLayout.setLayoutParams(linearLayoutParams);
			pictoFrameLayout.setBackgroundDrawable(normalShape);
			
			this.addView(pictoFrameLayout,j);
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
				View draggedView = (View) event.getLocalState();

				switch (event.getAction()) {
					case DragEvent.ACTION_DRAG_STARTED:
						// makes the draggedview invisible in ownerContainer
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
						ViewGroup ownerContainer = (ViewGroup) draggedView.getParent();
	
						PictoFrameLayout dropContainer = (PictoFrameLayout) v;
						Object tag = dropContainer.getTag();
	
						if (tag == null) {
							ownerContainer.removeView(draggedView);
							ownerContainer.setTag(null);
							dropContainer.addView(draggedView);
							dropContainer.setTag("filled");
						}
						draggedView.setVisibility(View.VISIBLE);
						break;
	
					case DragEvent.ACTION_DRAG_ENDED:
						// Makes the draggedview visible again after the view has
						// been moved or the drop wasn't valid.
						v.setBackgroundDrawable(normalShape);
						draggedView.setVisibility(View.VISIBLE);
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
