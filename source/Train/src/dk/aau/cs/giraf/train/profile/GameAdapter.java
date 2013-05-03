package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.pictogram.PictoFactory;
import dk.aau.cs.giraf.train.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameAdapter extends ArrayAdapter<GameConfiguration> {
    
    private ArrayList<GameConfiguration> gameConfigurations;
    private int selectedPosition = 0;
    private GameConfiguration selectedGameConfiguration;
    
    public GameAdapter(Context context, int textViewResourceId, ArrayList<GameConfiguration> objects) {
        super(context, textViewResourceId, objects);
        super.setNotifyOnChange(false);
        this.gameConfigurations = objects;
    }
    
    public GameConfiguration getSelectedGameConfiguration() {
        return this.selectedGameConfiguration;
    }
    
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        //super.notifyDataSetChanged();
    }
    
    @SuppressWarnings("static-access")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.profile_list_item, null); //Use same as profile
        }
        
        GameConfiguration gameConfiguration = this.gameConfigurations.get(position);
        
        TextView gameNameTextView = (TextView) convertView.findViewById(R.id.profileName);
        gameNameTextView.setText(gameConfiguration.getGameName());
        
        ImageView profilePictureImageView = (ImageView) convertView.findViewById(R.id.profilePic);
        profilePictureImageView.setImageResource(R.drawable.default_profile);
        profilePictureImageView.setImageBitmap(BitmapFactory.decodeFile(PictoFactory.INSTANCE.getPictogram(super.getContext(), gameConfiguration.getStation(0).getCategory()).getImagePath()));
        
        if (position == this.selectedPosition) {
            this.selectedGameConfiguration = gameConfiguration;
            convertView.setBackgroundResource(R.drawable.list_item_selected);
        }
        else {
            convertView.setBackgroundResource(R.drawable.list_item);
        }
        
        return convertView;
    }
}
