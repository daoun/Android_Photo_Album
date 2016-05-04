package cs213.androidproject38;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    private GridView searchResultGV;
    ArrayList<Photo> photolist;
    ArrayList<Photo> searchResult = new ArrayList<>();
    private ImageAdapter<Photo> adapter;
    private int selected = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        photolist = (ArrayList<Photo>) Home.albumList.get(Photos.currAlbum).getPhotolist();
        adapter = new ImageAdapter<>(this, 1, searchResult);

        handleIntent(getIntent());
        photoOpenListener();
    }





    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            showSearchResults(query);
        }
    }

    private void showSearchResults(String query){

        searchResultGV = (GridView) findViewById(R.id.searchResultGV);
        setTitle("Search Result for: " + query);
        query = query.toLowerCase();

        getPhotoList(query);

        searchResultGV.setAdapter(adapter);


    }

    private void getPhotoList(String query){

        for(Photo p : photolist){
            for(String s : p.getLocationTaglist()){
                if(s.toLowerCase().contains(query)){
                    searchResult.add(p);
                    break;
                }
            }

            for(String s : p.getPersonTaglist()){
                if(s.toLowerCase().contains(query)){
                    searchResult.add(p);
                    break;
                }
            }
        }
    }

    public void photoOpenListener() {
        searchResultGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                ViewImage.currPhoto = position;
                startActivity(new Intent(getApplicationContext(), ViewImage.class));
            }
        });
    }

    public class ImageAdapter<T> extends ArrayAdapter<Photo> {
        private Context mContext;
        private List<Photo> list;

        public ImageAdapter(Context c, int res, ArrayList<Photo> list) {
            super(c, res, list);
            mContext = c;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Photo getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv = new ImageView(mContext);

            iv.setImageBitmap(list.get(position).getURL());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(265, 265);
            iv.setLayoutParams(lp);
            iv.setPadding(15, 15, 15, 15);

            return iv;
        }

    }

}
