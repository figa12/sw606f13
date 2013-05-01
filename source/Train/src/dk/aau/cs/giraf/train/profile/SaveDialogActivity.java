package dk.aau.cs.giraf.train.profile;

import dk.aau.cs.giraf.train.Data;
import dk.aau.cs.giraf.train.R;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class SaveDialogActivity extends Activity {
    
    private EditText editText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_save_dialog);
        
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background_with_shape);
        backgroundDrawable.setColorFilter(Data.appBackgroundColor, PorterDuff.Mode.OVERLAY);
        super.findViewById(R.id.profileSaveDialog).setBackgroundDrawable(backgroundDrawable);
        
        this.editText = (EditText) super.findViewById(R.id.editText);
    }
    
    public void onClickSave(View view) {
        if (this.isValidFileName()) {
            
        }
    }
    
    public void onClickCancel(View view) {
        super.finish();
    }
    
    private boolean isValidFileName() {
        //TODO check for valid file name
        return false;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.finish();
    }
}
