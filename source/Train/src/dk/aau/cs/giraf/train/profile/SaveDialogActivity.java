package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class SaveDialogActivity extends Activity {
    
    public static final String GAME_NAME = "gameName";
    private Intent resultIntent = new Intent();
    private EditText editText;
    private AlertDialog errorDialog;
    
    private ArrayList<GameConfiguration> currentGameConfigurations;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_save_dialog);
        
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background_with_shape);
        backgroundDrawable.setColorFilter(Data.appBackgroundColor, PorterDuff.Mode.OVERLAY);
        super.findViewById(R.id.profileSaveDialog).setBackgroundDrawable(backgroundDrawable);
        
        this.editText = (EditText) super.findViewById(R.id.editText);
        
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setNegativeButton(super.getResources().getString(R.string.okay), null);
        this.errorDialog = alertDialogBuilder.create();
        
        Bundle extras = super.getIntent().getExtras();
        if(extras != null) {
            this.currentGameConfigurations = extras.getParcelableArrayList(ProfileActivity.GAME_CONFIGURATIONS);
        }
    }
    
    private void showAlertMessage(String message) {
        this.errorDialog.setMessage(message);
        this.errorDialog.show();
    }
    
    public void onClickSave(View view) {
        String gameName = this.editText.getText().toString();
        
        if (this.isValidGameName(gameName)) {
            this.resultIntent.putExtra(SaveDialogActivity.GAME_NAME, gameName);
            
            super.setResult(Activity.RESULT_OK, this.resultIntent);
            super.finish();
        }
    }
    
    public void onClickCancel(View view) {
        super.setResult(Activity.RESULT_CANCELED, this.resultIntent);
        super.finish();
    }
    
    private boolean isValidGameName(String gameName) {
        //Make sure game name doesn't already exist
        for (GameConfiguration configuration : this.currentGameConfigurations) {
            if (configuration.getGameName().equals(gameName)) {
                this.showAlertMessage(super.getResources().getString(R.string.name_exists));
                return false;
            }
        }
        
        //Check for illegal symbols
        if (gameName.contains(",") || gameName.contains(";")) {
            this.showAlertMessage(super.getResources().getString(R.string.invalid_symbols));
            return false;
        }
        
        return true;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.setResult(Activity.RESULT_CANCELED, this.resultIntent);
        super.finish();
    }
}
