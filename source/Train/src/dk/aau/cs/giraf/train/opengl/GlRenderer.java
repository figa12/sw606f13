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
    
    /* Constants */
    private static final float NEAR_CLIPPING_PLANE_DEPTH = 0.1f;
    private static final float FAR_CLIPPING_PLANE_DEPTH = 100.0f;
    private static final float FIELD_OF_VIEW_ANGLE = 45.0f;
    
    /** The width of the GLSurfaceView */
    private int width;
    /** The height of the GLSurfaceView */
    private int height;
    
    /** Triangle instance */
    private Triangle triangle;
    /** Square instance */
    private Square square;
    
    
    public GlRenderer() {
        triangle = new Triangle();
        square = new Square();
    }
    
    /** Here we do our drawing */
    public void onDrawFrame(GL10 gl) {
        /* Clears the screen to the color we previously decided on @onSurfaceCreated,
         * and clear the depth buffer and reset the scene */
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
        gl.glLoadIdentity(); //Reset The Current Modelview Matrix
        
        gl.glTranslatef(0.345f, 0.0f, -1.2f);	//Move 
        square.draw(gl);
        
        //gl.glTranslatef(0.0f, 2.5f, 0.0f); //Move
        //triangle.draw(gl);
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
            Log.d(GlRenderer.class.getSimpleName(), "FPS: " + Integer.toString(frames)); // write to log
            frames = 1; // also count the frame from the current iteration
        }
        else {
            frames++;
        }
    }
    
    /** If the surface changes, reset the view */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(height == 0) { 						//Prevent A Divide By Zero By
            height = 1; 						//Making Height Equal One
        }
        
        Log.d(GlRenderer.class.getSimpleName(), "Screen size: " + Integer.toString(width) + "x" + Integer.toString(height)); // write to log
        
        this.width = width;
        this.height = height;
        
        gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix, the next lines affects this
        gl.glLoadIdentity(); 					//Reset The Projection Matrix
        
        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, FIELD_OF_VIEW_ANGLE, (float)width / (float)height, NEAR_CLIPPING_PLANE_DEPTH, FAR_CLIPPING_PLANE_DEPTH);
        
        gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix, the next lines affects this
        gl.glLoadIdentity(); 					//Reset The Modelview Matrix
    }
    
    /** 
     * Get the visible height of the GLSurfaceView at the given depth.
     * The height is calculated by the field of view and the depth.
     * 
     * @param depth to calculate the visible height
     */
    public float getActualHeight(float depth) {
        double otherAngles = (180 - this.FIELD_OF_VIEW_ANGLE) / 2; // TODO make better variable name
        double hypotenuse = depth / Math.sin(otherAngles);
        return (float) Math.sqrt(Math.pow(hypotenuse, 2.0) + Math.pow(depth, 2.0));
    }
    
    /** 
     * Get the visible width of the GLSurfaceView.
     * The width is calculated by the height and the aspect ratio.
     * 
     * @param actualHeight is the calculated height from {@link #getActualHeight(float depth)}
     * @see #getActualHeight(float depth)
     */
    public float getActualWidth(float actualHeight) {
        float aspectRatio = this.width / this.height;
        return actualHeight * aspectRatio;
    }
    
    /** The Surface is created/init() */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
        
        /* (Red, green, blue, alpha) Min 0.0f max 1.0f */
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    //Black Background
        
        /* Set up the depth buffer */
        gl.glClearDepthf(1.0f);                     //Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL);             //The Type Of Depth Testing To Do
        
        /* Really Nice Perspective Calculations */
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
    }
}
