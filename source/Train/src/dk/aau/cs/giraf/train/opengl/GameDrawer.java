package dk.aau.cs.giraf.train.opengl;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import dk.aau.cs.giraf.train.R;
import android.content.Context;

/**
 * This class handles all drawing.
 * 
 * @author Jesper
 * @see GameDrawer#drawGame()
 * @see GameDrawer#loadAllTexture()
 */
public final class GameDrawer {

	private interface GameDrawable {
		public void draw();
	}

	private interface GameDrawableTexture extends GameDrawable {
		public void loadTexture();
	}

	private GL10 gl;
	private Context context;
	private float visibleWidth = 1.0f;
	private float visibleHeight = 1.0f;
	private final float DRAWING_DEPTH = -907.744f;

	private ArrayList<GameDrawable> gameDrawables = new ArrayList<GameDrawable>();
	private ArrayList<GameDrawableTexture> gameDrawablesWithTexture = new ArrayList<GameDrawableTexture>();

	public GameDrawer(GL10 gl, Context context) {
		this.gl = gl;
		this.context = context;

		// add GameDrawables to the list in the order they should be drawn
		this.addGameDrawable(new Train());
		this.addGameDrawable(new Wheels());
	}

	public void setVisibleLimits(float visibleWidth, float visibleHeight) {
		this.visibleWidth = visibleWidth;
		this.visibleHeight = visibleHeight;
	}

	private final void addGameDrawable(GameDrawable gameDrawable) {
		this.gameDrawables.add(gameDrawable);

		if (gameDrawable instanceof GameDrawableTexture) {
			this.gameDrawablesWithTexture
					.add((GameDrawableTexture) gameDrawable);
		}
	}

	/** Draw everything on screen */
	public final void drawGame() {
		this.gl.glLoadIdentity(); // Reset The Current Modelview Matrix

		for (GameDrawable gameDrawable : this.gameDrawables) {
			gameDrawable.draw();
		}
	}

	/** Loads all texture */
	public final void loadAllTexture() {
		for (GameDrawableTexture gameDrawableTexture : this.gameDrawablesWithTexture) {
			gameDrawableTexture.loadTexture();
		}
		this.gameDrawablesWithTexture.clear();
	}

	private final class Train implements GameDrawableTexture {

		private Texture train = new Texture(1.0f, 1.0f);
		private Texture wagon = new Texture(1.0f, 1.0f);
		private Square shaft = new Square(40f, 3f);

		@Override
		public void draw() {
			gl.glLoadIdentity(); // reset the position

			gl.glTranslatef(-542.32f, -142.72f, DRAWING_DEPTH);
			this.wagon.draw(gl);

			gl.glTranslatef(314.87f, -152f, 0);
			this.shaft.draw(gl, 0, 0, 0, 1);

			gl.glTranslatef(40f, 152f, 0);
			this.wagon.draw(gl);

			gl.glTranslatef(314.87f, -152f, 0);
			this.shaft.draw(gl, 0, 0, 0, 1);

			gl.glTranslatef(33f, 242.35f, 0);
			this.train.draw(gl);
		
	}

		@Override
		public void loadTexture() {
			this.wagon.loadTexture(gl, context, R.drawable.texture_wagon, Texture.AspectRatio.BitmapOneToOne);
			this.train.loadTexture(gl, context, R.drawable.texture_train, Texture.AspectRatio.BitmapOneToOne);

		}
	}
	
	private final class Wheels implements GameDrawableTexture {
		
		private Texture largeWheel = new Texture(1.0f, 1.0f); // wheel diameter 106.4
		private Texture mediumWheel = new Texture(1.0f, 1.0f); // wheel diameter 78.71
		private Texture smallWheel = new Texture(1.0f, 1.0f); // wheel diameter 60.8
		private Texture wheelShaft = new Texture(1.0f, 1.0f);
		//private Texture ground = new Texture(1.0f, 1.0f);

		@Override
		public void draw() {
			
			//Wagon wheels			
			gl.glLoadIdentity(); // reset the position
			
			gl.glTranslatef(-507.08f, -277.04f, DRAWING_DEPTH);
			this.mediumWheel.draw(gl);

			gl.glTranslatef(167.56f, 0f, 0);
			this.mediumWheel.draw(gl);
			
			gl.glTranslatef(187.38f, 0f, 0);
			this.mediumWheel.draw(gl);
			
			gl.glTranslatef(167.56f, 0f, 0);
			this.mediumWheel.draw(gl);
			
			//Train wheels
			gl.glLoadIdentity(); // reset the position
			
			gl.glTranslatef(191.02f, -250.74f, DRAWING_DEPTH);
			this.largeWheel.draw(gl);
			
			gl.glTranslatef(153.56f, -45.6f, 0);
			this.smallWheel.draw(gl);
		
			gl.glTranslatef(79.55f, 0f, 0);
			this.smallWheel.draw(gl);
			
			gl.glTranslatef(-43.17f, -42.48f, 0);
			this.wheelShaft.draw(gl);
			
			/*//Ground
			gl.glLoadIdentity();
			
			gl.glTranslatef(-640f, -356f, DRAWING_DEPTH);
			this.ground.draw(gl);*/	

			
		}

		@Override
		public void loadTexture() {
			this.mediumWheel.loadTexture(gl, context, R.drawable.texture_wheel_medium, Texture.AspectRatio.BitmapOneToOne);
			this.largeWheel.loadTexture(gl, context, R.drawable.texture_wheel_large, Texture.AspectRatio.BitmapOneToOne);
			this.smallWheel.loadTexture(gl, context, R.drawable.texture_wheel_small, Texture.AspectRatio.BitmapOneToOne);
			this.wheelShaft.loadTexture(gl, context, R.drawable.texture_wheel_shaft, Texture.AspectRatio.BitmapOneToOne);
			//this.ground.loadGLTexture(gl, context, R.drawable.texture_ground, Texture.AspectRatio.BitmapOneToOne);
			
		}
		
	}
	
}