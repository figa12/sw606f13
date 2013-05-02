package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class SaveDialogActivity extends Activity {
    
    public static final String GAME_NAME = "gameName";
    private Intent resultIntent = new Intent();
    private EditText editText;
    
    private ArrayList<GameConfiguration> currentGameConfigurations;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_save_dialog);
        
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background_with_shape);
        backgroundDrawable.setColorFilter(Data.appBackgroundColor, PorterDuff.Mode.OVERLAY);
        super.findViewById(R.id.profileSaveDialog).setBackgroundDrawable(backgroundDrawable);
        
        this.editText = (EditText) super.findViewById(R.id.editText);
        
        Bundle configurationBundle = super.getIntent().getExtras();
        if(configurationBundle != null) {
            this.currentGameConfigurations = configurationBundle.getParcelableArrayList("gameConfiguration"); //FIXME USE STATIC KEY FROM PROFILE
        }
    }
    
    public void onClickSave(View view) {
        if (this.isValidFileName()) {
            String gameName = this.editText.getText().toString();

            this.resultIntent.putExtra(SaveDialogActivity.GAME_NAME, gameName);
            
            super.setResult(Activity.RESULT_OK, this.resultIntent);
            super.finish();
        }
    }
    
    public void onClickCancel(View view) {
        super.setResult(Activity.RESULT_CANCELED, this.resultIntent);
        super.finish();
    }
    
    private boolean isValidFileName() {
        //TODO check for valid file name
        //TODO check if game name already exist in this.currentGameConfigurations
        return true;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.setResult(Activity.RESULT_CANCELED, this.resultIntent);
        super.finish();
    }
}
