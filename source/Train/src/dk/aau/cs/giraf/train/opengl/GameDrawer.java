package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import dk.aau.cs.giraf.train.R;
import android.content.Context;
import android.util.TimingLogger;

/**
 * This class handles all game drawing.
 * 
 * @author Jesper
 * @see GameDrawer#drawGame()
 * @see GameDrawer#loadTexture()
 */
public final class GameDrawer {

	private abstract class DrawableGroup {
	    public abstract void load();
	    public abstract void draw();
		
		public final void translateAndDraw(Renderable renderable) {
		    for (Coordinate coordinate : renderable.getCoordinates()) {
		        moveTo(coordinate);
		        renderable.draw(gl);
		    }
		}
		
		public final void translateAndDraw(Renderable renderable, Color color) {
		    for (Coordinate coordinate : renderable.getCoordinates()) {
                moveTo(coordinate);
                renderable.draw(gl, color);
            }
		}
		
		private final void rotateCenterAndDraw(float angle, Shape shape, Color color) {
	        //first move half width/height. This will be the center of the shape
	        move(shape.getWidth()/2, -shape.getHeight()/2, 0f);
	        
	        //create a new drawing matrix at the shape's center
	        gl.glPushMatrix();
	        
            //then rotate the matrix
            gl.glRotatef(angle, 0f, 0f, 1f);
            
	        //then move the shape, in the new matrix, to align the center of the shape with the center of the matrix
	        gl.glTranslatef(-shape.getWidth()/2, shape.getHeight()/2, 0f);
	        
	        //draw
	        shape.draw(gl, color);
	        
	        //discard the matrix
	        gl.glPopMatrix();
	    }
		
		public final void translateRotateAndDraw(float angle, Shape shape) {
		    this.translateRotateAndDraw(angle, shape, new Color());
		}
		
		public final void translateRotateAndDraw(float angle, Shape shape, Color color) {
            for (Coordinate coordinate : shape.getCoordinates()) {
                moveTo(coordinate);
                this.rotateCenterAndDraw(angle, shape, color);
            }
        }
	}

	private GL10 gl;
	private Context context;
	private float visibleWidth = 1f;
	private float visibleHeight = 1f;
	
	private final float FOREGROUND = -907.744f;
	private final float MIDDLEGROUND = -1300f;
	
	private long systemTimeNow = 1;
	private long systemTimeLast = 0;
	
	private float currentX = 0f;
	private float currentY = 0f;
	private float currentZ = 0f;

	private ArrayList<DrawableGroup> gameDrawables = new ArrayList<DrawableGroup>();
	
	public GameDrawer(GL10 gl, Context context) {
		this.gl = gl;
		this.context = context;
		
		// add GameDrawables to the list in the order they should be drawn
		this.addDrawableGroup(new Middleground());
		this.addDrawableGroup(new Station());
		this.addDrawableGroup(new Train());
		this.addDrawableGroup(new Wheels());
		
		this.addDrawableGroup(new Tester()); // Always draw last
	}

	public void setVisibleLimits(float visibleWidth, float visibleHeight) {
		this.visibleWidth = visibleWidth;
		this.visibleHeight = visibleHeight;
	}

	private final void addDrawableGroup(DrawableGroup drawableGroup) {
		this.gameDrawables.add(drawableGroup);
	}

	/** Draw everything on screen */
	public synchronized final void drawGame() { //FIXME does it give sense to use 'synchronized' keyword here?
	    this.resetPosition();
	    
	    this.systemTimeNow = System.nanoTime();
	    
		for (DrawableGroup drawableGroup : this.gameDrawables) {
			drawableGroup.draw();
			//this.resetPosition();
		}
		
		this.systemTimeLast = this.systemTimeNow;
	}

	/** Loads all texture */
	public final void loadTexture() {
		for (DrawableGroup drawableGroup : this.gameDrawables) {
			drawableGroup.load();
		}
	}
	
	private final void moveTo(Coordinate coordinate) {
	    this.move(coordinate.x - this.currentX, coordinate.y - this.currentY, coordinate.z - this.currentZ);
	}
	
	private final void move(float x, float y, float z) {
	    this.gl.glTranslatef(x, y, z);
        
        this.currentX += x;
        this.currentY += y;
        this.currentZ += z;
	}
	
	private final void resetPosition() {
	    this.gl.glLoadIdentity();
	    this.currentX = 0f;
	    this.currentY = 0f;
	    this.currentZ = 0f;
	}
	
	private final class Station extends DrawableGroup {
		
	    private Texture trainStation = new Texture(1.0f, 1.0f);
		
		@Override
        public final void load() {
		    //Add coordinates to the renderables
		    trainStation.addCoordinate(-588.64f, 376f, FOREGROUND);
		    
		    //Load the textures
            this.trainStation.loadTexture(gl, context, R.drawable.texture_train_station, Texture.AspectRatio.BitmapOneToOne);
        }
		
		@Override
		public final void draw() {
			super.translateAndDraw(this.trainStation);
		}
	}

	private final class Train extends DrawableGroup {

		private Texture train = new Texture(1.0f, 1.0f);
		private Texture wagon = new Texture(1.0f, 1.0f);
		private Square shaft = new Square(40f, 3f);
		
		@Override
        public final void load() {
		    //Add coordinates to the renderables
		    this.wagon.addCoordinate(-542.32f, -142.72f, FOREGROUND);
            this.wagon.addCoordinate(-187.45f, -142.72f, FOREGROUND);
            this.shaft.addCoordinate(-227.45f, -294.72f, FOREGROUND);
            this.shaft.addCoordinate(127.42f, -294.72f, FOREGROUND);
            this.train.addCoordinate(160.42f, -52.37f, FOREGROUND);
            
            //Load the textures
            this.wagon.loadTexture(gl, context, R.drawable.texture_wagon, Texture.AspectRatio.BitmapOneToOne);
            this.train.loadTexture(gl, context, R.drawable.texture_train, Texture.AspectRatio.BitmapOneToOne);
        }
		
		@Override
		public final void draw() {
			super.translateAndDraw(this.shaft, new Color(0f, 0f, 0f, 1f));
			super.translateAndDraw(this.wagon);
			super.translateAndDraw(this.train);
	}

		
	}
	
	private final class Wheels extends DrawableGroup {
		
		private Texture largeWheel = new Texture(1.0f, 1.0f); // wheel diameter 106.4
		private Texture mediumWheel = new Texture(1.0f, 1.0f); // wheel diameter 78.71
		private Texture smallWheel = new Texture(1.0f, 1.0f); // wheel diameter 60.8
		private Texture wheelShaft = new Texture(1.0f, 1.0f);
		private Texture ground = new Texture(1280.0f, 21.0f);
		
		private Float rotation = 0f;
		
		@Override
        public final void load() {
		    //Add coordinates to the renderables
		    this.mediumWheel.addCoordinate(-507.08f, -277.04f, FOREGROUND);
            this.mediumWheel.addCoordinate(-339.52f, -277.04f, FOREGROUND);
            this.mediumWheel.addCoordinate(-152.14f, -277.04f, FOREGROUND);
            this.mediumWheel.addCoordinate(15.42f, -277.04f, FOREGROUND);
            this.largeWheel.addCoordinate(191.02f, -250.74f, FOREGROUND);
            this.smallWheel.addCoordinate(344.58f, -296.34f, FOREGROUND);
            this.smallWheel.addCoordinate(424.13f, -296.34f, FOREGROUND);
            //this.wheelShaft.addCoordinate(380.96f, -338.82f, DRAWING_DEPTH);
            this.wheelShaft.addCoordinate(370.83f, -321.84f, FOREGROUND);
            this.ground.addCoordinate(-640f, -356f, FOREGROUND);
		    
            //Load the textures
            this.mediumWheel.loadTexture(gl, context, R.drawable.texture_wheel_medium, Texture.AspectRatio.BitmapOneToOne);
            this.largeWheel.loadTexture(gl, context, R.drawable.texture_wheel_large, Texture.AspectRatio.BitmapOneToOne);
            this.smallWheel.loadTexture(gl, context, R.drawable.texture_wheel_small, Texture.AspectRatio.BitmapOneToOne);
            this.wheelShaft.loadTexture(gl, context, R.drawable.texture_wheel_shaft, Texture.AspectRatio.BitmapOneToOne);
            this.ground.loadTexture(gl, context, R.drawable.texture_ground_mini);
        }
		
		@Override
		public final void draw() {
		    super.translateRotateAndDraw(rotation, this.mediumWheel);
		    super.translateRotateAndDraw(rotation, this.largeWheel);
            super.translateRotateAndDraw(rotation, this.smallWheel);
            
            // draw the wheel shaft
            moveTo(this.wheelShaft.getCoordinates().get(0));
            gl.glPushMatrix();
            gl.glRotatef(this.rotation, 0f, 0f, 1f);
            gl.glTranslatef(20f, 0f, 0f);
            gl.glRotatef(-this.rotation, 0f, 0f, 1f);
            this.wheelShaft.draw(gl);
            gl.glPopMatrix();
            
            super.translateAndDraw(this.ground);
		    
		    this.rotation -= 0.2f * (systemTimeNow - systemTimeLast)/1000000.0f;
		}
	}
	
	private final class Middleground extends DrawableGroup {
	    
	    private ScrollableSequence sequence = new ScrollableSequence();
	    private Square square1 = new Square(100f, 100f);
	    private Square square2 = new Square(100f, 100f);
        
	    
        @Override
        public void load() {
            this.sequence.addCoordinate(800f, 300f, MIDDLEGROUND);
            Color color1 = new Color(1f, 0f, 0f, 1f);
            Color color2 = new Color(0f, 1f, 0f, 1f);
            this.sequence.addScrollableItem(square1, new Coordinate(0f, 0f, 0f), color1);
            this.sequence.addScrollableItem(square2, new Coordinate(100f, 0f, 0f), color2);
            this.sequence.addScrollableItem(square1, new Coordinate(200f, 0f, 0f), color2);
            this.sequence.addScrollableItem(square2, new Coordinate(300f, 0f, 0f), color1);
        }

        @Override
        public void draw() {
            super.translateAndDraw(this.sequence);
        }
	}

	private final class Tester extends DrawableGroup {
	    
	    private Square horizontalAxis = new Square(1280f, 1.1f);
	    private Square verticalAxis = new Square(1.1f, 752f);
	    
        @Override
        public void load() {
            this.horizontalAxis.addCoordinate(-this.horizontalAxis.getWidth()/2f, this.horizontalAxis.getHeight()/2f, FOREGROUND);
            this.verticalAxis.addCoordinate(-this.verticalAxis.getWidth()/2f, this.verticalAxis.getHeight()/2f, FOREGROUND);
        }

        @Override
        public void draw() {
            Color color = new Color(0, 0, 0, 25);
            
            super.translateAndDraw(this.horizontalAxis, color);
            super.translateAndDraw(this.verticalAxis, color);
        }
	}
}