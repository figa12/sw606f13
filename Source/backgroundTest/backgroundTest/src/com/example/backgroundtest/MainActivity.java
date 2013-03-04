package com.example.backgroundtest;

import android.app.Activity;
import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.myimage1).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.myimage2).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.myimage3).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.myimage4).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.myimage5).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.myimage6).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.myimage7).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.myimage8).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.myimage9).setOnTouchListener(new MyTouchListener());
    findViewById(R.id.StationLayout).setOnDragListener(new MyDragListener());
    findViewById(R.id.Cart1LayoutLeft).setOnDragListener(new MyDragListener());
    findViewById(R.id.Cart1LayoutRight).setOnDragListener(new MyDragListener());
    findViewById(R.id.Cart2LayoutLeft).setOnDragListener(new MyDragListener());
    findViewById(R.id.Cart2LayoutRight).setOnDragListener(new MyDragListener());
    findViewById(R.id.Platform1Layout).setOnDragListener(new MyDragListener());
    findViewById(R.id.Platform2Layout).setOnDragListener(new MyDragListener());
    findViewById(R.id.Platform3Layout).setOnDragListener(new MyDragListener());
    findViewById(R.id.Platform4Layout).setOnDragListener(new MyDragListener());
    findViewById(R.id.TrainLayout).setOnDragListener(new MyDragListener());
 
    Resources res = getResources();
    Drawable drawable = res.getDrawable(R.drawable.rhino);
    
    
    
    
  }

  private final class MyTouchListener implements OnTouchListener {
    public boolean onTouch(View view, MotionEvent motionEvent) {
    	
      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        ClipData data = ClipData.newPlainText("", "");
        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
        view.setVisibility(View.INVISIBLE);
        	return true;
       } 
      else {
        return false;
      }
    }
  }

  class MyDragListener implements OnDragListener {
    Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
    Drawable normalShape = getResources().getDrawable(R.drawable.shape);

    @Override
    public boolean onDrag(View v, DragEvent event) {
      boolean validDrop = false;
//      int action = event.getAction();
      View view = (View) event.getLocalState();
      ViewGroup ownerContainer = (ViewGroup) view.getParent();
      switch (event.getAction()) 
      {
      
      case DragEvent.ACTION_DRAG_STARTED:
        // Do nothing
        break;
        
      case DragEvent.ACTION_DRAG_ENTERED:
        v.setBackgroundDrawable(enterShape);
        break;
      
      case DragEvent.ACTION_DRAG_EXITED:
        v.setBackgroundDrawable(normalShape);
        break;
        
      case DragEvent.ACTION_DROP:
        // Dropped, reassign View to ViewGroup  
    	validDrop = true;
        LinearLayout dropContainer = (LinearLayout) v;
        
        Object tag = dropContainer.getTag();
       /* if (!v.getClass().equals(LinearLayout.class))
  	  	{
    		findViewById(view.getId()).setVisibility(View.VISIBLE);
	        ownerContainer.setTag(view.getId());
  	  	}*/
    		if (tag == null) {
    			
    			ownerContainer.removeView(view);
    			ownerContainer.setTag(null);
		        
    			dropContainer.addView(view);
    			dropContainer.setTag(view.getId());
		        view.setVisibility(View.VISIBLE); 
		        
		       		        
    		}
    		else {
		        findViewById(view.getId()).setVisibility(View.VISIBLE);
		        ownerContainer.setTag(view.getId());
    		}
    		
        break;
      case DragEvent.ACTION_DRAG_ENDED:
        v.setBackgroundDrawable(normalShape);
        if (validDrop == false)
        {
        	findViewById(view.getId()).setVisibility(View.VISIBLE);
	        ownerContainer.setTag(view.getId());
        }
      default:
        break;
      }
      return true;
    }
  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
