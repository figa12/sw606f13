package dk.aau.cs.giraf.train.opengl.game;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import dk.aau.cs.giraf.pictogram.Pictogram;
import dk.aau.cs.giraf.train.R;
import dk.aau.cs.giraf.train.opengl.Color;
import dk.aau.cs.giraf.train.opengl.GameDrawer;
import dk.aau.cs.giraf.train.opengl.Square;
import dk.aau.cs.giraf.train.opengl.Texture;

public final class Train extends GameDrawable {

    public Train(GL10 gl, Context context, GameDrawer gameDrawer, GameData gameData) {
        super(gl, context, gameDrawer, gameData);
    }

    private final Texture train = new Texture(1.0f, 1.0f);
    private final Texture wagon = new Texture(1.0f, 1.0f);
    private final Square shaft = new Square(40f, 3f);
    private final Square trainWindow = new Square(95f, 95f);
    
    @Override
    public final void load() {
        //Load the textures
        this.wagon.loadTexture(gl, context, R.drawable.texture_wagon, Texture.AspectRatio.BitmapOneToOne);
        this.train.loadTexture(gl, context, R.drawable.texture_train, Texture.AspectRatio.BitmapOneToOne);
        
        //Add coordinates to the renderables
        this.wagon.addCoordinate(-542.32f, -142.72f, GameData.FOREGROUND);
        this.wagon.addCoordinate(-187.45f, -142.72f, GameData.FOREGROUND);
        this.shaft.addCoordinate(-227.45f, -294.72f, GameData.FOREGROUND);
        this.shaft.addCoordinate(127.42f, -294.72f, GameData.FOREGROUND);
        this.train.addCoordinate(160.42f, -52.37f, GameData.FOREGROUND);
        this.trainWindow.addCoordinate(198.92f, -87f, GameData.FOREGROUND);
    }
    
    private Texture driverPictogram;
    
    public final void setDriverPictogram(Pictogram pictogram) {
        if(pictogram == null) {
            this.driverPictogram = null;
            return;
        }
        
        final float xPosition = 197f;
        final float yPosition = -84f;
        
        final float pictogramWidth = 100f;
        final float pictogramHeight = pictogramWidth;
        
        Bitmap pictogramBitmap = BitmapFactory.decodeFile(pictogram.getImagePath());
        
        this.driverPictogram = new Texture(pictogramWidth, pictogramHeight);
        
        //Load texture relative to pictogram size. We need to maintain the aspect ratio relative to the greatest side.
        if(pictogramBitmap.getWidth() >= pictogramBitmap.getHeight()) {
            this.driverPictogram.loadTexture(super.gl, super.context, pictogramBitmap, Texture.AspectRatio.KeepWidth);
        } else {
            this.driverPictogram.loadTexture(super.gl, super.context, pictogramBitmap, Texture.AspectRatio.KeepHeight);
        }
        
        //Center image in the available space
        float xOffset = (pictogramWidth - this.driverPictogram.getWidth()) / 2;
        float yOffset = (pictogramHeight - this.driverPictogram.getHeight()) / 2;
        
        this.driverPictogram.addCoordinate(xPosition + xOffset, yPosition - yOffset, GameData.FOREGROUND);
        
        pictogramBitmap.recycle();
    }
    
    private Texture[] pictogramTexture = new Texture[6]; //No more than 6 pictograms can be present
    
    public final void setWagonPictograms(Pictogram[] pictograms) {
        int width = 310; //LinearLayout width
        float pictogramWidthSpace;
        float pictogramHeightSpace;
        
        //Set pictogramWidth to the size we have available relative to how many pictograms we need to fit on the provided space.
        pictogramWidthSpace = (pictograms.length <= 4) ? width / 2 : width / 3;
        pictogramHeightSpace = pictogramWidthSpace; //Same height as width
        
        float xPosition = -535f;
        final float yPosition = -151f; //TODO change according to number of pictograms
        
        for (int i = 0; i < pictograms.length; i++, xPosition += pictogramWidthSpace) {
            this.pictogramTexture[i] = null;
            
            if(i == (pictograms.length /2)) {
                xPosition += 45f; //Add offset to get to next LinearLayout
            }
            
            if(pictograms[i] == null) {
                continue;
            }
            
            //Decode bitmap from the image path.
            Bitmap pictogramBitmap = BitmapFactory.decodeFile(pictograms[i].getImagePath());
            
            this.pictogramTexture[i] = new Texture(pictogramWidthSpace, pictogramHeightSpace);
            
            //Load texture relative to pictogram size. We need to maintain the aspect ratio relative to the greatest side.
            if(pictogramBitmap.getWidth() >= pictogramBitmap.getHeight()) {
                this.pictogramTexture[i].loadTexture(super.gl, super.context, pictogramBitmap, Texture.AspectRatio.KeepWidth);
            } else {
                this.pictogramTexture[i].loadTexture(super.gl, super.context, pictogramBitmap, Texture.AspectRatio.KeepHeight);
            }
            
            //Center image in the available space
            float xOffset = (pictogramWidthSpace - this.pictogramTexture[i].getWidth()) / 2;
            float yOffset = (pictogramHeightSpace - this.pictogramTexture[i].getHeight()) / 2;
            
            this.pictogramTexture[i].addCoordinate(xPosition + xOffset, yPosition - yOffset, GameData.FOREGROUND);
            
            pictogramBitmap.recycle();
        }
    }
    
    @Override
    public final void draw() {
        super.translateAndDraw(this.shaft, Color.Black);
        super.translateAndDraw(this.wagon);
        super.translateAndDraw(this.trainWindow, Color.Window);
        super.translateAndDraw(this.train);
        
        if(this.driverPictogram != null) {
            super.translateAndDraw(this.driverPictogram);
        }
        
        //Only draw while train is driving or if we are at the last stop
        if(GameData.currentTrainVelocity != 0f || GameData.numberOfStops == GameData.numberOfStations) {
            for (int i = 0; i < this.pictogramTexture.length; i++) {
                if(this.pictogramTexture[i] != null) {
                    super.translateAndDraw(this.pictogramTexture[i]);
                }
            }
        }
    }
}
