package com.example.train;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;



public class OpenGLView extends GLSurfaceView {
	public class GL20Renderer implements GLSurfaceView.Renderer {
		
	    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
	        // Set the background frame color
	        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	    }

	    public void onDrawFrame(GL10 unused) {
	        // Redraw background color
	        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	    }

	    public void onSurfaceChanged(GL10 unused, int width, int height) {
	        GLES20.glViewport(0, 0, width, height);
	    }
	}
	
	public OpenGLView(Context context, AttributeSet attrs) {
		super(context);
		
		// Set the Renderer for drawing on the GLSurfaceView. Should always be the first thing to do
		setRenderer(new GL20Renderer());
		
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
		
		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
}
