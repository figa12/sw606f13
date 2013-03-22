package dk.aau.cs.giraf.train.opengl;

import dk.aau.cs.giraf.train.R;


import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

public class GameActivity extends Activity {
    
    private GlView openGLView;	
    private Integer[] cart1 = {};
    private Integer[] cart2 = {};
    private Integer[] station = {R.drawable.nej,R.drawable.mig,R.drawable.se,R.drawable.bade};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.activity_game);
    	
    	//this.setListeners();
    	
        this.openGLView = (GlView)findViewById(R.id.openglview);
        
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE); // TODO investigate API level problems and write results in comment
    }
    
    /* Sets touch and drag listeners. This is temporary and only for proof of concept */
    private void setListeners() {
    	
        findViewById(R.id.Cart1LeftImageView).setOnTouchListener(new TouchListener());
        findViewById(R.id.Cart1RightImageView).setOnTouchListener(new TouchListener());
        findViewById(R.id.Cart2LeftImageView).setOnTouchListener(new TouchListener());
        findViewById(R.id.Cart2RightImageView).setOnTouchListener(new TouchListener());
        /*findViewById(R.id.myimage5).setOnTouchListener(new TouchListener());
        findViewById(R.id.myimage6).setOnTouchListener(new TouchListener());
        findViewById(R.id.myimage7).setOnTouchListener(new TouchListener());
        findViewById(R.id.myimage8).setOnTouchListener(new TouchListener());
        findViewById(R.id.myimage9).setOnTouchListener(new TouchListener());
        */
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
     * A touch listener that starts a drag event.
     * There should also be a receiver implementing {@link OnDragListener}.
     * 
     *  @see DragListener
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
    
    /** A drag listner implementing an onDrag() method that runs when something is dragged to it. */
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
        	if(event.getLocalState() != null){
        	View draggedView = (View) event.getLocalState();
        	ViewGroup ownerContainer = (ViewGroup) draggedView.getParent();
	            switch (event.getAction()) {
	                case DragEvent.ACTION_DRAG_STARTED:
	                	//makes the draggedview invisible in ownerContainer
	                	draggedView.setVisibility(View.INVISIBLE);
	                    break;
	                    
	                case DragEvent.ACTION_DRAG_ENTERED:
	                	//Change the background of droplayout(purely style)
	                    v.setBackgroundDrawable(enterShape); //FIXME code is deprecated, use new
	                    break;
	                
	                case DragEvent.ACTION_DRAG_EXITED:
	                	//Change the background back when exiting droplayout(purely style)
	                    v.setBackgroundDrawable(normalShape); // FIXME code is deprecated, use new
	                    break;
	                    
	                case DragEvent.ACTION_DROP:
	                    // Dropped, assigns the draggedview to the dropcontainer if the container does not already contain a view.  
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
	                	//Makes the draggedview visible again after the view has been moved or the drop wasn't valid.
	                	 v.setBackgroundDrawable(normalShape); // FIXME code is deprecated, use new
		                 draggedView.setVisibility(View.VISIBLE);
	                    break;
	            }
        	}
            return true;
        }
    }
}
