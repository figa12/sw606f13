package dk.aau.cs.giraf.train.profile;

import java.util.List;

import dk.aau.cs.giraf.train.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GameAdapter extends ArrayAdapter<GameConfiguration> {
    
    private int selectedPosition = 0;
    private GameConfiguration selectedGameConfiguration;
    
    public GameAdapter(Context context, int textViewResourceId, List<GameConfiguration> objects) {
        super(context, textViewResourceId, objects);
    }
    
    public GameConfiguration getSelectedGameConfiguration() {
        return this.selectedGameConfiguration;
    }
    
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        super.notifyDataSetChanged();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.profile_list_item, null); //Use same as profile
        }
        
        GameConfiguration gameConfiguration = super.getItem(position);
        
        TextView gameNameTextView = (TextView) convertView.findViewById(R.id.profileName);
        gameNameTextView.setText(gameConfiguration.getGameName());
        
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
