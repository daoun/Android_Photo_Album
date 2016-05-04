package cs213.androidproject38;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Christine on 5/4/16.
 */
public class MovePhotoAlbumList extends AppCompatActivity {

    ListView list;
    public static int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_photo_albumlist);

        list = (ListView) findViewById(R.id.movePhotoAlbumList);

        for(int i = 0; i < Home.albumList.size(); i++){
            TextView textView = new TextView(this);
            textView.setTextSize(30);
            textView.setText(Home.albumList.get(i).getName());
            list.addView(textView);
        }

        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
