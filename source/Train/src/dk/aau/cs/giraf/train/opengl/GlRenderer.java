package dk.aau.cs.giraf.train.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

/** The renderer which draws on GLSurfaceView.
 * The code comes from {@link http://insanitydesign.com/wp/projects/nehe-android-ports/}
 */
public class GlRenderer implements Renderer {
    
    /** Triangle instance */
    private Triangle triangle;
    /** Square instance */
    private Square square;
    
    
    public GlRenderer() {
        triangle = new Triangle();
        square = new Square();
    }
    
    /**
     * The Surface is created/init()
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
        
        /* (Red, green, blue, alpha) Min 0.0f max 1.0f */
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
        
        /* Set up the depth buffer */
        gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
        
        /* Really Nice Perspective Calculations */
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
    }
    
    /**
     * Here we do our drawing
     */
    public void onDrawFrame(GL10 gl) {
        /* Clears the screen to the color we previously decided on @onSurfaceCreated,
         * and clear the depth buffer and reset the scene */
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
        gl.glLoadIdentity();					//Reset The Current Modelview Matrix
        
        /* Drawing */
        gl.glTranslatef(0.0f, -1.2f, -6.0f);	//Move 
        square.draw(gl);						//Draw the square
        
        gl.glTranslatef(0.0f, 2.5f, 0.0f);      //Move
        triangle.draw(gl);                      //Draw the triangle
        
        measureFps();
    }
    
    /** A timestamp for the last time fps was written to the log  */
    private long timestamp = 0;
    /** The current number of frames counted this second */
    private int frames = 1;
    
    /** Measures FPS and prints it in the log */
    private void measureFps() {
        if(System.currentTimeMillis() >= timestamp + 1000) {
            timestamp = System.currentTimeMillis();
            Log.d(GlRenderer.class.getSimpleName() + " FPS: ", Integer.toString(frames)); // write to log
            frames = 1; // also count the frame from the current iteration
        }
        else {
            frames++;
        }
    }
    
    /**
     * If the surface changes, reset the view
     */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(height == 0) { 						//Prevent A Divide By Zero By
            height = 1; 						//Making Height Equal One
        }
        
        gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix, the next lines affects this
        gl.glLoadIdentity(); 					//Reset The Projection Matrix
        
        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        
        gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix, the next lines affects this
        gl.glLoadIdentity(); 					//Reset The Modelview Matrix
    }
}
