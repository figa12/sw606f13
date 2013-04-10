package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.GlRenderer;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Wheels extends RenderableGroup {
    
    public Wheels(GL10 gl, Context context, GameDrawer gameDrawer) {
        super(gl, context, gameDrawer);
    }

    private Texture largeWheel = new Texture(1.0f, 1.0f); // wheel diameter 106.39
    private Texture mediumWheel = new Texture(1.0f, 1.0f); // wheel diameter 78.71
    private Texture smallWheel = new Texture(1.0f, 1.0f); // wheel diameter 60.8
    private Texture wheelShaft = new Texture(1.0f, 1.0f);
    private Texture ground;
    
    private float[] rotation = { 0f, 0f, 0f }; // rotation number for each wheel size
    private final double[] wheelDiameter = {
            106.39f, // large wheel
            78.71f,  // medium wheel
            60.8f    // small wheel
    };
    private final int largeWheelIndex = 0;
    private final int mediumWheelIndex = 1;
    private final int smallWheelIndex = 2;
    
    private float calculateRotation(int wheelIndex) { //TODO investigate garbage
        if(wheelIndex - 1 > this.wheelDiameter.length) { //perform error check
            return 0;
        }
        
        double circumference = this.wheelDiameter[wheelIndex] * Math.PI;
        double degreePerPixel = 360.0 / circumference;
        this.rotation[wheelIndex] += (float) degreePerPixel * GameData.pixelMovementForThisFrame;
        return this.rotation[wheelIndex];
    }
    
    @Override
    public final void load() {
        //Create ground object
        this.ground = new Texture(GlRenderer.getActualWidth(GlRenderer.getActualHeight(GameData.FOREGROUND)), 21.0f);
        
        //Add coordinates to the renderables
        this.mediumWheel.addCoordinate(-507.08f, -277.04f, GameData.FOREGROUND);
        this.mediumWheel.addCoordinate(-339.52f, -277.04f, GameData.FOREGROUND);
        this.mediumWheel.addCoordinate(-152.14f, -277.04f, GameData.FOREGROUND);
        this.mediumWheel.addCoordinate(15.42f, -277.04f, GameData.FOREGROUND);
        this.largeWheel.addCoordinate(191.02f, -250.74f, GameData.FOREGROUND);
        this.smallWheel.addCoordinate(344.58f, -296.34f, GameData.FOREGROUND);
        this.smallWheel.addCoordinate(424.13f, -296.34f, GameData.FOREGROUND);
        this.wheelShaft.addCoordinate(370.83f, -321.84f, GameData.FOREGROUND);
        this.ground.addCoordinate(-GlRenderer.getActualWidth(GlRenderer.getActualHeight(GameData.FOREGROUND))/2, -356f, GameData.FOREGROUND);
        
        //Load the textures
        this.mediumWheel.loadTexture(gl, context, R.drawable.texture_wheel_medium, Texture.AspectRatio.BitmapOneToOne);
        this.largeWheel.loadTexture(gl, context, R.drawable.texture_wheel_large, Texture.AspectRatio.BitmapOneToOne);
        this.smallWheel.loadTexture(gl, context, R.drawable.texture_wheel_small, Texture.AspectRatio.BitmapOneToOne);
        this.wheelShaft.loadTexture(gl, context, R.drawable.texture_wheel_shaft, Texture.AspectRatio.BitmapOneToOne);
        this.ground.loadTexture(gl, context, R.drawable.texture_ground_mini);
    }
    
    @Override
    public final void draw() {
        super.translateRotateAndDraw(this.calculateRotation(this.mediumWheelIndex), this.mediumWheel);
        super.translateRotateAndDraw(this.calculateRotation(this.largeWheelIndex), this.largeWheel);
        super.translateRotateAndDraw(this.calculateRotation(this.smallWheelIndex), this.smallWheel);
        
        // draw the wheel shaft
        super.gameDrawer.translateTo(this.wheelShaft.getCoordinates().get(0));
        gl.glPushMatrix();
        gl.glRotatef(this.rotation[this.smallWheelIndex], 0f, 0f, 1f);
        gl.glTranslatef(22f, 0f, 0f);
        gl.glRotatef(-this.rotation[this.smallWheelIndex], 0f, 0f, 1f);
        this.wheelShaft.draw(gl, super.gameDrawer.currentPosition);
        gl.glPopMatrix();
        
        super.translateAndDraw(this.ground);
    }
}
