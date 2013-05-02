package dk.aau.cs.giraf.train.profile;

import java.util.ArrayList;

import dk.aau.cs.giraf.train.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class GameListView extends ListView {
    
    private GameAdapter adapter;
    
    public GameListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        super.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                GameListView.this.adapter.setSelectedPosition(position);
            }
        });
    }
    
    public void loadGames() {
        ArrayList<GameConfiguration> gameConfigurations = new ArrayList<GameConfiguration>();
        
        this.adapter = new GameAdapter(super.getContext(), R.drawable.list_item, gameConfigurations);
        super.setAdapter(this.adapter);
    }
}
